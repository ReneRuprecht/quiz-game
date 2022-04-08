package groupd.quiz.game.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GamePlayAnswerRequest {
    @JsonProperty("gameId")
    private String gameId;
    @JsonProperty("senderUsername")
    private String senderUsername;
    @JsonProperty("answer")
    private String answer;
}
