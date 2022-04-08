package groupd.quiz.game.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class GameIdRequest {

    @JsonProperty("gameId")
    private  String gameId;
}
