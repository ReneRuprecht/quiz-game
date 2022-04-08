package groupd.quiz.user.response;

import groupd.quiz.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserWithProfilePictureResponse {
    User user;
    String profilePictureBase64;

}
