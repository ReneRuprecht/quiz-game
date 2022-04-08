package groupd.quiz.game.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GameUsernameRequest {
    @JsonProperty("username")
    private String username;

}
