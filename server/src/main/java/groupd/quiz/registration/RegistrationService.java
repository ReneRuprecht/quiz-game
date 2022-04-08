package groupd.quiz.registration;

import groupd.quiz.registration.request.RegistrationRequest;
import groupd.quiz.registration.validator.UsernameValidator;
import groupd.quiz.scores.Score;
import groupd.quiz.user.User;
import groupd.quiz.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UsernameValidator usernameValidator;
    private final UserService userService;

    /**
     * registers a user and saves it into the database
     *
     * @param registrationRequest contains username and password
     * @return the user object or null with status codes
     */
    public ResponseEntity<User> register(RegistrationRequest registrationRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();

        boolean isValidUsername = usernameValidator
                .test(registrationRequest.getUsername());

        if (!isValidUsername) {
            responseHeaders.set("Error", "Username ist ungültig");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }

        // create fresh score
        Score score = new Score();


        User user = new User(
                registrationRequest.getUsername(),
                registrationRequest.getPassword(),
                ""
        );

        // set score object to user
        user.setScoreObject(score);
        // set user object to the score object for mapping
        user.getScoreObject().setUser(user);


        return userService.signUpUser(user);
    }
}
