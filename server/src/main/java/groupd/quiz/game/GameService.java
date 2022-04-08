package groupd.quiz.game;

import groupd.quiz.game.request.GameIdAndUsernameRequest;
import groupd.quiz.game.request.GameIdRequest;
import groupd.quiz.game.request.GameIdUsernameReadyRequest;
import groupd.quiz.game.request.GameUsernameRequest;
import groupd.quiz.game.response.GamePlayResponseHeaderMessage;
import groupd.quiz.game.response.GamePlayResponseHeaderType;
import groupd.quiz.lobby.LobbyStorage;
import groupd.quiz.question.QuestionService;
import groupd.quiz.user.User;
import groupd.quiz.user.UserService;
import groupd.quiz.websocket.WebsocketSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class GameService {
    private UserService userService;
    private QuestionService questionService;
    private WebsocketSender websocketSender;

    /**
     * handles the creation of a game action
     * checks if the user exists that wants to create a game
     * check if the game is already open
     * creates the game and sets the user to it
     *
     * @param gameUsernameRequest contains the username
     * @return ResponseEntity with the game or null
     */
    public ResponseEntity<Game> createGame(GameUsernameRequest gameUsernameRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        User player = userService.loadUserByUsername(gameUsernameRequest.getUsername());
        if (player == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.user_not_found.toString());

            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        // check if game already open
        for (Map.Entry<String, Game> game : LobbyStorage.getInstance().getGames().entrySet()) {
            if (game.getValue().getPlayerOne().getUsername().equals(gameUsernameRequest.getUsername())) {
                Game gameFromLobby = LobbyStorage.getInstance().getGames().get(game.getKey());
                return new ResponseEntity<>(gameFromLobby, HttpStatus.OK);
            }
        }

        Game game = new Game();
        game.createGame(player);
        LobbyStorage.getInstance().setGame(game);
        log.info("create: " + game);

        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    /**
     * handles the connection request for a player to a game
     * checks if the game exists in the storage
     * checks if the user that wants to connect is the host of the game
     * checks if the game is already fully populated
     * checks if the player who wants to join, exists
     * updates the game with the second player
     *
     * @param gameIdAndUsernameRequest contains gameid and username
     * @return ResponseEntity with the game or null
     */
    public ResponseEntity<Game> connectToGame(GameIdAndUsernameRequest gameIdAndUsernameRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpHeaders websocketHeaders = new HttpHeaders();

        Game game = LobbyStorage.getInstance().getGames().get(gameIdAndUsernameRequest.getGameId());

        log.info("connect: " + game);

        // check if game exists
        if (game == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.game_not_found.toString());

            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }


        // check if the user already created a game
        if (game.getPlayerOne().getUsername().equals(gameIdAndUsernameRequest.getUsername())) {
            return new ResponseEntity<>(game, HttpStatus.OK);
        }

        // check if game is already full
        if (game.getPlayerTwo() != null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.game_already_full.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        User player = userService.loadUserByUsername(gameIdAndUsernameRequest.getUsername());

        if (player == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.user_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        // set playerTwo and prepare the game
        game.addPlayerTwoToGame(player);
        game.setListOfQuestions(questionService.getQuestions());
        game.finalPrepare();
        LobbyStorage.getInstance().setGame(game);


        // set websocket response and send it
        websocketHeaders.set(GamePlayResponseHeaderType.Info.toString()
                , GamePlayResponseHeaderMessage.user_joined.toString());
        ResponseEntity<Game> connectResponse = new ResponseEntity<>(game, websocketHeaders, HttpStatus.OK);
        websocketSender.sendMessageWithDelay("/topic/game-" + game.getGameId(), connectResponse, 0);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    /**
     * gets all the games from the lobby storage and returns them
     *
     * @return Map<String, Game>> of all games from storage
     */
    public ResponseEntity<Map<String, Game>> getAllGames() {
        return new ResponseEntity<>(LobbyStorage.getInstance().getGames(), HttpStatus.OK);
    }

    /**
     * deletes a game from the lobby storage
     *
     * @param gameIdRequest contains the gameId in json format
     * @return ResponseEntity with games or null
     */
    public ResponseEntity<Game> deleteGame(GameIdRequest gameIdRequest) {

        Map<String, Game> currentGames = LobbyStorage.getInstance().getGames();

        // checks the gameid
        if (currentGames.get(gameIdRequest.getGameId()) == null) {
            return new ResponseEntity<>(currentGames.get(gameIdRequest.getGameId()), HttpStatus.NOT_FOUND);
        }

        currentGames = LobbyStorage.getInstance().deleteGame(gameIdRequest.getGameId());

        // checks if the game is still there
        if (LobbyStorage.getInstance().getGames().containsKey(gameIdRequest.getGameId())) {
            log.info("Game still exists " + gameIdRequest.getGameId());
            return new ResponseEntity<>(currentGames.get(gameIdRequest.getGameId()), HttpStatus.CONFLICT);
        }

        // successfully deleted
        log.info("Game deleted successfully " + gameIdRequest.getGameId());
        return new ResponseEntity<>(currentGames.get(gameIdRequest.getGameId()), HttpStatus.OK);


    }

    /**
     * updates the game with the status of the player
     * checks if the game exists
     * checks if the user exists
     * updates the game with the new ready value of the player
     * checks if the game is ready to start
     * sends a websocket message if the game is ready after 10 seconds
     *
     * @param gameIdUsernameReadyRequest contains gameid , username and readystate
     * @return ResponseEntity with true if the status got changed of the player or false
     */
    public ResponseEntity<Boolean> setPlayerReady(GameIdUsernameReadyRequest gameIdUsernameReadyRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Game game = LobbyStorage.getInstance().getGames().get(gameIdUsernameReadyRequest.getGameId());

        if (game == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.game_not_ready.toString());
            return new ResponseEntity<>(false, responseHeaders, HttpStatus.NOT_FOUND);
        }

        User user = game.getMyUser(gameIdUsernameReadyRequest.getUsername());

        if (user == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.user_not_found.toString());
            return new ResponseEntity<>(false, responseHeaders, HttpStatus.NOT_FOUND);
        }
        game.setPlayerReady(gameIdUsernameReadyRequest.getUsername(), gameIdUsernameReadyRequest.isReady());

        if (game.arePlayerReady()) {

            log.info("Game is ready");
            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.game_ready.toString());

            ResponseEntity<Game> gameResponse = new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);


            websocketSender.sendMessageWithDelay("/topic/game-" + game.getGameId(), gameResponse, 10000);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        log.info("Game is not ready yet");
        return new ResponseEntity<>(true, HttpStatus.OK);

    }

    /**
     * disconnects the sender from the game
     *
     * @param gameIdAndUsernameRequest contains gameid and username
     * @return game object or null
     */
    public ResponseEntity<Game> disconnect(GameIdAndUsernameRequest gameIdAndUsernameRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpHeaders websocketHeaders = new HttpHeaders();
        Game game = LobbyStorage.getInstance().getGames().get(gameIdAndUsernameRequest.getGameId());

        if (game == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.game_not_ready.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        User user = game.getMyUser(gameIdAndUsernameRequest.getUsername());

        if (user == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.user_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        if (game.getPlayerOne().getUsername().equals(user.getUsername())) {

            LobbyStorage.getInstance().deleteGame(game.getGameId());
            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.user_left.toString());


            // set websocket response and send it
            websocketHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.user_left.toString());
            ResponseEntity<Game> connectResponse = new ResponseEntity<>(null, websocketHeaders, HttpStatus.OK);
            websocketSender.sendMessageWithDelay("/topic/game-" + game.getGameId(), connectResponse, 0);
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);

        }
        if (game.getPlayerTwo().getUsername().equals(user.getUsername())) {
            game.addPlayerTwoToGame(null);
            LobbyStorage.getInstance().setGame(game);
            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.user_left.toString());

            // set websocket response and send it
            websocketHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.user_left.toString());
            ResponseEntity<Game> connectResponse = new ResponseEntity<>(game, websocketHeaders, HttpStatus.OK);
            websocketSender.sendMessageWithDelay("/topic/game-" + game.getGameId(), connectResponse, 0);
            return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);
        }

        responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                , GamePlayResponseHeaderMessage.user_not_found.toString());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);

    }

    /**
     * gets information for a single game
     *
     * @param gameIdRequest needs the game id
     * @return game or null with statuscodes
     */
    public ResponseEntity<Game> getGame(GameIdRequest gameIdRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Game game = LobbyStorage.getInstance().getGames().get(gameIdRequest.getGameId());

        if (game == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.game_not_ready.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(game, HttpStatus.OK);

    }


}
