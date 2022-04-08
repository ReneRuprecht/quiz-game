package groupd.quiz;

import groupd.quiz.question.QuestionController;
import groupd.quiz.question.request.QuestionCreateRequest;
import groupd.quiz.registration.RegistrationController;
import groupd.quiz.registration.request.RegistrationRequest;
import groupd.quiz.scores.ScoreController;
import groupd.quiz.user.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(

)
@Slf4j
@EnableWebSocketMessageBroker
public class QuizApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }


    /**
     * just for creating dummy data
     */

    @Autowired
    RegistrationController registrationController;

    @Autowired
    ScoreController scoreController;

    @Autowired
    UserController userController;
    @Autowired
    QuestionController questionController;


    @Override
    public void run(String... args) {


        createDummyUser();
        createDummyQuestions();


    }

    /**
     * creates dummy user
     */
    private void createDummyUser() {
        List<RegistrationRequest> registrationRequests = Arrays.asList(
                new RegistrationRequest("username1", "1234"),
                new RegistrationRequest("username2", "1234")
        );

        for (RegistrationRequest r : registrationRequests) {
            log.info("Register User: %s with Password: %s".formatted(r.getUsername(), r.getPassword()));
            registrationController.register(r);
        }

    }

    /**
     * creates dummy questions
     */
    private void createDummyQuestions() {
        List<QuestionCreateRequest> questionCreateRequestsList = Arrays.asList(
                new QuestionCreateRequest(
                        "Wobei handelt es sich um ein beliebtes Getränk an kalten Tagen?",
                        "bin die Banane",
                        "nennt mich Kirsche",
                        "heiße Zitrone",
                        "call me strawberry",
                        "heiße Zitrone"),
                new QuestionCreateRequest(
                        "Was kommt in Ostasien häufig auf den Tisch?",
                        "Sonicht",
                        "Sovielleicht",
                        "Soschoneher",
                        "Soja",
                        "Soja"),
                new QuestionCreateRequest(
                        "Die Dinosaurier lebten ...?",
                        "auf Pump",
                        "mit Schulden",
                        "in der Kreide",
                        "knietief im Dispo",
                        "in der Kreide"),
                new QuestionCreateRequest(
                        "Wofür steht HTML",
                        "Hyper Text Markup Language",
                        "Home Tool Markup Language",
                        "Hyperlinks and Text Markup Language",
                        "Hyperscript Text Markup Language",
                        "Hyper Text Markup Language")

        );


        for (QuestionCreateRequest q : questionCreateRequestsList) {
            log.info("Create Question: " + questionController.createQuestion(q).toString());
        }


        log.info("Total Questions: " + questionController.getQuestions().toString());
    }
}
