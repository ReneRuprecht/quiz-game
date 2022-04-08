package groupd.quiz.scores;

import groupd.quiz.scores.request.ScoreRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class ScoreService {


    private final ScoreRepository scoreRepository;


    /**
     * updates the score object
     *
     * @param scoreRequest contains the score object from a user
     * @return entity of type score or null and status code if object is null
     */
    public ResponseEntity<Score> updateScore(ScoreRequest scoreRequest) {
        Score score = loadScoreById(scoreRequest.getUserId());
        if (score == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        if (scoreRequest.isWon()) {
            score.setWin(score.getWin() + 1);
        } else {
            score.setLose(score.getLose() + 1);
        }

        score.setTotalScore(score.getTotalScore() + scoreRequest.getScoreValue());
        score.setGamesPlayed(score.getGamesPlayed() + 1);

        double avg = score.getTotalScore() / score.getGamesPlayed();

        score.setAvg(avg);

        scoreRepository.save(score);

        return new ResponseEntity<>(score, HttpStatus.OK);


    }

    /**
     * gets the score object from the repository
     *
     * @param id contains the object id
     * @return score object or null
     */
    public Score loadScoreById(Long id) {

        Optional<Score> score = scoreRepository.findById(id);

        if (score.isEmpty()) {
            return null;
        }


        return score.get();

    }
}
