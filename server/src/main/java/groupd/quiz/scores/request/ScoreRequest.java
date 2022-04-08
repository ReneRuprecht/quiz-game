package groupd.quiz.scores.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ScoreRequest {
    private long userId;
    private int scoreValue;
    private boolean won;

}
