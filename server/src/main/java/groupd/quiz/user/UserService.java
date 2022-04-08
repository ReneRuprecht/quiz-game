package groupd.quiz.user;

import groupd.quiz.user.response.UserProfilePicturePathResponse;
import groupd.quiz.user.response.UserWithProfilePictureResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j


public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * gets the user from the repository.
     *
     * @param username contains username
     * @return entity of type user or null
     */
    public User loadUserByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();

    }

    /**
     * gets the username from a username
     *
     * @param username contains the username
     * @return entity of type user or null and status code and header if object is null
     */
    public ResponseEntity<User> getUserInfo(String username) {
        HttpHeaders responseHeaders = new HttpHeaders();
        User user = loadUserByUsername(username);

        if (user == null) {
            responseHeaders.set("Error", "User nicht gefunden");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    /**
     * updates the user with a new password
     *
     * @param password contains the password
     * @param username contains the username
     * @return entity of type user or null and status code and header if object is null
     */
    public ResponseEntity<User> updateUser(String password,
                                           String username) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if (password.equals("") || password.contains(" ")) {
            responseHeaders.set("Error", "Passwort ist ungültig");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.BAD_REQUEST);
        }

        User updatedUser = loadUserByUsername(username);

        String encodedPassword = getEncodedPassword(password);
        updatedUser.setPassword(encodedPassword);

        userRepository.save(updatedUser);

        User user = loadUserByUsername(username);

        if (user == null ||
                !encodedPassword.equals("") && !user.getPassword().equals(encodedPassword)) {
            responseHeaders.set("Error", "User existiert nicht oder Passwort ist falsch");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    private String getEncodedPassword(String password) {
        return bCryptPasswordEncoder
                .encode(password);
    }


    /**
     * gets called from the registration.
     * creates the user if the username is not taken and saves it to the database
     *
     * @param user contains the user object
     * @return entity of user or null and status code and header if object is null
     */
    public ResponseEntity<User> signUpUser(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();


        boolean userExists = userRepository.findByUsername(
                        user.getUsername())
                .isPresent();

        if (userExists) {
            responseHeaders.set("Error", "Username ist bereits vorhanden");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }
        String encodedPassword = getEncodedPassword(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * @param file      contains the file of the picture
     * @param principal is the name from the jwt token
     * @return entity of type UserProfilePicturePathResponse or null and status code and header if object is null
     */
    public ResponseEntity<UserProfilePicturePathResponse> uploadProfilePicture(
            MultipartFile file,
            Principal principal) throws IOException {

        HttpHeaders responseHeaders = new HttpHeaders();


        User user = loadUserByUsername(principal.getName());

        String path = "user-photos/" + user.getUsername();

        Path uploadPath = Paths.get("opt/" + path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            responseHeaders.set("Error", "Datei nicht gefunden");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfilePicture(path + "/" + filename);
            userRepository.save(user);

            return new ResponseEntity<>(
                    new UserProfilePicturePathResponse(user.getProfilePicture()), HttpStatus.OK);
        } catch (IOException ioe) {
            responseHeaders.set("Error", "Upload fehler");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);

        }

    }


    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * gets the user and the profilepicture base64 encoded
     *
     * @param username contains the username
     * @return entity of type UserWithProfilePictureResponse or null
     */
    public ResponseEntity<UserWithProfilePictureResponse> getUserWithProfilePictureBase64(String username) {
        HttpHeaders responseHeaders = new HttpHeaders();

        User user = loadUserByUsername(username);

        if (user == null) {
            responseHeaders.set("Error", "User nicht gefunden");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }

        if (!(user.getProfilePicture().length() > 0)) {
            responseHeaders.set("Error", "Profilbild nicht gefunden");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.NOT_FOUND);
        }
        File file = new File("/opt/" + user.getProfilePicture());

        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {

            fis.read(bytes);

            String encodedString = Base64.getEncoder().encodeToString(bytes);

            UserWithProfilePictureResponse userWithProfilePictureResponse = new UserWithProfilePictureResponse(
                    user,
                    encodedString
            );

            return new ResponseEntity<>(userWithProfilePictureResponse, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            responseHeaders.set("Error", "Fehler beim lesen des Profilbilds");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }


    }
}
