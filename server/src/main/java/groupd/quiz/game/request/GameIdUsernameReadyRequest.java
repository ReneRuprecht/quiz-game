package groupd.quiz.game.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GameIdUsernameReadyRequest {

    @JsonProperty("username")
    private  String username;
    @JsonProperty("gameId")
    private  String gameId;
    @JsonProperty("ready")
    private boolean ready;
}
