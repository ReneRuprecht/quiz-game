package groupd.quiz.scores;

import groupd.quiz.scores.request.ScoreRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class ScoreController {


    private final ScoreService scoreService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/update"
    )
    public ResponseEntity<Score> updateScore(@RequestBody ScoreRequest scoreRequest) {


        return scoreService.updateScore(scoreRequest);
    }
}
