package groupd.quiz.game.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class GameIdAndUsernameRequest {
    @JsonProperty("username")
    private  String username;
    @JsonProperty("gameId")
    private  String gameId;
}
