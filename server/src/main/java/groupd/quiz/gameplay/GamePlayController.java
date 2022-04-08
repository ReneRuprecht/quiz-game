package groupd.quiz.gameplay;

import groupd.quiz.game.Game;
import groupd.quiz.game.request.GameIdAndUsernameRequest;
import groupd.quiz.game.request.GamePlayAnswerRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
@Slf4j
public class GamePlayController {

    private GamePlayService gamePlayService;

    @MessageMapping("/gameplay-{gameId}")
    @SendTo("/topic/game-{gameId}")
    public ResponseEntity<Game> gamePlay(
            @RequestBody GamePlayAnswerRequest gamePlayAnswerRequest
    ) {
        log.info("gameplay: {}", gamePlayAnswerRequest);

        return gamePlayService.gamePlay(gamePlayAnswerRequest);

    }

/*
    @MessageMapping("/game-info-{gameId}")
    @SendTo("/topic/game-{gameId}")
    public ResponseEntity<Game> getGameInfo(
            @RequestBody GameIdAndUsernameRequest gameIdAndUsernameRequest
    ) {
        log.info("Game info: {}", gameIdAndUsernameRequest);

        return gamePlayService.gameInfo(gameIdAndUsernameRequest);
    }
    */
}
