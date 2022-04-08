package groupd.quiz.registration;

import groupd.quiz.registration.request.RegistrationRequest;
import groupd.quiz.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(
        path = "api/v1/registration"
)
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;



    @RequestMapping(
            method = RequestMethod.POST,
            value = "/register"
    )
    public ResponseEntity<User> register(
            @RequestBody RegistrationRequest registrationRequest
    ) {

        return registrationService.register(registrationRequest);
    }
}
