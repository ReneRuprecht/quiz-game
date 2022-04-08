package groupd.quiz.gameplay;

import groupd.quiz.game.Game;
import groupd.quiz.game.request.GameIdAndUsernameRequest;
import groupd.quiz.game.request.GamePlayAnswerRequest;
import groupd.quiz.game.response.GamePlayResponseHeaderMessage;
import groupd.quiz.game.response.GamePlayResponseHeaderType;
import groupd.quiz.game.response.GameStatus;
import groupd.quiz.lobby.LobbyStorage;
import groupd.quiz.user.User;
import groupd.quiz.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GamePlayService {
    private UserService userService;


    /**
     * handles the whole gameplay
     * checks if the game exists in server storage
     * checks if the player2 is set, otherwise the game cant be played
     * checks if the game status is already finished
     * checks if the user who wants to make a move, is in the game
     * checks the answer from the user
     * checks if there is a winner
     *
     * @param gamePlayAnswerRequest is an object with gameId, user and the answer
     * @return ResponseEntity with status code and header
     */
    public ResponseEntity<Game> gamePlay(GamePlayAnswerRequest gamePlayAnswerRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!LobbyStorage.getInstance().getGames().containsKey(gamePlayAnswerRequest.getGameId())) {

            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.game_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
        log.info("load game from storage");
        Game game = LobbyStorage.getInstance().getGames().get(gamePlayAnswerRequest.getGameId());


        if (!game.playerTwoExists()) {

            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.game_not_ready.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }


        if (game.getGameStatus().equals(GameStatus.FINISHED)) {

            responseHeaders.set(GamePlayResponseHeaderType.Error.toString()
                    , GamePlayResponseHeaderMessage.game_already_finished.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        log.info("get user");
        User user = game.getMyUser(gamePlayAnswerRequest.getSenderUsername());

        if (user == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.user_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }


        // check correct answer
        log.info("check correct answer");


        if (!game.isAnswerCorrect(gamePlayAnswerRequest.getAnswer())) {
            log.info("answer wrong");
            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.answer_incorrect.toString());
            return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);
        }

        log.info("answer correct");


        log.info("answer correct {}", user.getUsername());
        // update current score +1
        user.getScoreObject().setCurrentScoreValue(
                user.getScoreObject().getCurrentScoreValue() + 1);


        log.info("check winner");
        User winner = checkWinner(user, game);
        if (winner != null) {
            log.info("we got a winner");

            User looser;
            // find looser
            if (winner.getUsername().equals(game.getPlayerOne().getUsername())) {
                looser = game.getPlayerTwo();
            } else {
                looser = game.getPlayerOne();
            }

            log.info("gameover");
            game.setWinner(winner.getUsername());
            game.setGameStatus(GameStatus.FINISHED);
            LobbyStorage.getInstance().deleteGame(game.getGameId());


            gameOver(game, winner, looser);


            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.game_over.toString());

            return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);
        }

        if (game.isQuestionsListEmpty()) {

            game.setGameStatus(GameStatus.FINISHED);
            game.setWinner(null);

            gameOverDraw(game, game.getPlayerOne(), game.getPlayerTwo());


            responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                    , GamePlayResponseHeaderMessage.game_over.toString());
            return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);

        }

        log.info("load next question");
        game.loadNextQuestion();
        responseHeaders.set(GamePlayResponseHeaderType.Info.toString()
                , GamePlayResponseHeaderMessage.answer_correct.toString());
        return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);

    }

    /**
     * check if the current user is the winner of the game
     *
     * @param currentPlayer contains the current player
     * @param game          contains the current game
     * @return a user if there is a winner or null
     */
    public User checkWinner(User currentPlayer, Game game) {

        User otherPlayer;
        if (currentPlayer.getUsername().equals(game.getPlayerOne().getUsername())) {
            otherPlayer = game.getPlayerTwo();
        } else {
            otherPlayer = game.getPlayerOne();
        }


        if (game.getListOfQuestions().size() == 0 &&
                currentPlayer.getScoreObject().getCurrentScoreValue() > otherPlayer.getScoreObject().getCurrentScoreValue()) {
            return currentPlayer;
        }

        if (game.getListOfQuestions().size() == 0 &&
                otherPlayer.getScoreObject().getCurrentScoreValue() > currentPlayer.getScoreObject().getCurrentScoreValue()) {
            return otherPlayer;
        }

        return null;

    }

    /**
     * updates both users with draw values
     *
     * @param game      contains the game
     * @param playerOne contains the first player
     * @param playerTwo contains the second player
     */
    private void gameOverDraw(Game game, User playerOne, User playerTwo) {
        playerOne = game.draw(playerOne);
        playerTwo = game.draw(playerTwo);
        userService.save(playerOne);
        userService.save(playerTwo);
    }

    /**
     * updates both users but with winner and looser values
     *
     * @param game   contains the game
     * @param winner contains the winner
     * @param looser contains the looser
     */
    private void gameOver(Game game, User winner, User looser) {

        winner = game.won(winner);
        looser = game.lost(looser);
        userService.save(winner);
        userService.save(looser);
    }

    /**
     * CURRENTLY NOT USED
     * is used to get the info of a game
     *
     * @param gameIdAndUsernameRequest contains gameid and the username
     * @return ResponseEntity with the game or null
     */
    public ResponseEntity<Game> gameInfo(GameIdAndUsernameRequest gameIdAndUsernameRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if (!LobbyStorage.getInstance().getGames().containsKey(gameIdAndUsernameRequest.getGameId())) {

            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.game_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
        log.info("load game from storage");
        Game game = LobbyStorage.getInstance().getGames().get(gameIdAndUsernameRequest.getGameId());


        if (gameIdAndUsernameRequest.getUsername() == null) {
            responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                    GamePlayResponseHeaderMessage.user_not_found.toString());
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
        }

        if (game.getPlayerOne().getUsername().equals(gameIdAndUsernameRequest.getUsername())) {
            responseHeaders.set(GamePlayResponseHeaderType.Info.toString(),
                    GamePlayResponseHeaderMessage.game_info.toString());
            return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);
        }


        if (game.getPlayerTwo() != null) {
            if (game.getPlayerTwo().getUsername().equals(gameIdAndUsernameRequest.getUsername())) {
                responseHeaders.set(GamePlayResponseHeaderType.Info.toString(),
                        GamePlayResponseHeaderMessage.game_info.toString());
                return new ResponseEntity<>(game, responseHeaders, HttpStatus.OK);
            }
        }


        responseHeaders.set(GamePlayResponseHeaderType.Error.toString(),
                GamePlayResponseHeaderMessage.user_not_found.toString());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);


    }

}
