package groupd.quiz.user;

import groupd.quiz.user.response.UserProfilePicturePathResponse;
import groupd.quiz.user.response.UserWithProfilePictureResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(
        path = "api/v1/user"
)
@AllArgsConstructor

public class UserController {

    private UserService userService;


    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/get"
    )
    public ResponseEntity<User> printUser(Principal principal) {

        String username = principal.getName();


        return userService.getUserInfo(username);
    }


    @RequestMapping(
            method = RequestMethod.POST,
            value = "/update"
    )
    public ResponseEntity<User> updateUser(
            @RequestParam("password") String password,
            Principal principal
    ) {


        return userService.updateUser(
                password,
                principal.getName());
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/upload-profile-picture"
    )
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<UserProfilePicturePathResponse> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            Principal principal) throws IOException {

        return userService.uploadProfilePicture(file, principal);
    }


    @RequestMapping("/get-user-with-profile-picture")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public ResponseEntity<UserWithProfilePictureResponse> getUserWithProfilePictureBase64(
            @RequestParam("username") String username) {

        return userService.getUserWithProfilePictureBase64(username);

    }


    //TODO: just for testing
    /*
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/updateScore"
    )
    public String updateUserScore(
            @RequestBody ScoreRequest scoreRequest, Principal principal, HttpServletResponse response
    ) throws IOException {

        User user = userService.loadUserByUsername(principal.getName());

        String scoreResult =scoreController.updateScore(user.getId(),scoreRequest.isWon(),scoreRequest.getScoreValue());

        if (!scoreResult.isEmpty())
        {
            return scoreResult;
        }

        response.setStatus(422);
        response.getWriter().write("Der User konnte nicht bearbeitet werden");
        response.getWriter().flush();
        return "";
    }

*/


}
