package groupd.quiz.game;

import groupd.quiz.game.request.GameIdAndUsernameRequest;
import groupd.quiz.game.request.GameIdRequest;
import groupd.quiz.game.request.GameIdUsernameReadyRequest;
import groupd.quiz.game.request.GameUsernameRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(
        path = "api/v1/game"
)
@AllArgsConstructor
@Slf4j
public class GameController {
    private GameService gameService;


    @RequestMapping(
            method = RequestMethod.GET,
            value = "/getGames"
    )
    public ResponseEntity<Map<String, Game>> getGames() {
        return gameService.getAllGames();
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/getGame"
    )
    public ResponseEntity<Game> getGame(
            @RequestBody GameIdRequest gameIdRequest
    ) {

        return gameService.getGame(gameIdRequest);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/create"
    )
    public ResponseEntity<Game> createGame(
            @RequestBody GameUsernameRequest gameUsernameRequest
    ) {

        return gameService.createGame(gameUsernameRequest);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/connect"
    )
    public ResponseEntity<Game> connectToGame(
            @RequestBody GameIdAndUsernameRequest gameIdAndUsernameRequest
    ) {
        return gameService.connectToGame(gameIdAndUsernameRequest);
    }


    @RequestMapping(
            method = RequestMethod.POST,
            value = "/delete"
    )
    public ResponseEntity<Game> deleteGame(
            @RequestBody GameIdRequest gameIdRequest
    ) {

        return gameService.deleteGame(gameIdRequest);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/ready"
    )
    public ResponseEntity<Boolean> setPlayerReady(@RequestBody GameIdUsernameReadyRequest gameIdUsernameReadyRequest) {

        return gameService.setPlayerReady(gameIdUsernameReadyRequest);

    }


    @RequestMapping(
            method = RequestMethod.POST,
            value = "/disconnect"
    )
    public ResponseEntity<Game> disconnect(@RequestBody GameIdAndUsernameRequest gameIdAndUsernameRequest) {

        return gameService.disconnect(gameIdAndUsernameRequest);

    }




}
