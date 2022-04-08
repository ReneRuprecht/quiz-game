package groupd.quiz.question.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class QuestionCreateRequest {
    @JsonProperty("question")
    private String question;
    @JsonProperty("answerOne")
    private String answerOne;
    @JsonProperty("answerTwo")
    private String answerTwo;
    @JsonProperty("answerThree")
    private String answerThree;
    @JsonProperty("answerFour")
    private String answerFour;
    @JsonProperty("answerCorrect")
    private String answerCorrect;
}
