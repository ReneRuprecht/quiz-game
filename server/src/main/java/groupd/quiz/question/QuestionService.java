package groupd.quiz.question;

import groupd.quiz.question.request.QuestionCreateRequest;
import groupd.quiz.question.validator.QuestionValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionService {

    private QuestionRepository questionRepository;
    private QuestionValidator questionValidator;

    /**
     * Creates a question and saves it inside the database
     *
     * @param questionCreateRequest contains the question object
     * @return question or null with status codes and header information
     */
    public ResponseEntity<Question> createQuestion(QuestionCreateRequest questionCreateRequest) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!questionValidator.isQuestionCreateRequestValid(questionCreateRequest)) {

            responseHeaders.set("Error", "Request war unvollständig");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }

        Question question = new Question(
                questionCreateRequest.getQuestion(),
                questionCreateRequest.getAnswerOne(),
                questionCreateRequest.getAnswerTwo(),
                questionCreateRequest.getAnswerThree(),
                questionCreateRequest.getAnswerFour(),
                questionCreateRequest.getAnswerCorrect()
        );

        questionRepository.save(question);

        Optional<Question> questionFromRepository = questionRepository.findById(question.getId());

        if (questionFromRepository.isEmpty()) {

            responseHeaders.set("Error", "Request war unvollständig");
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(question, HttpStatus.CREATED);

    }

    /**
     * gets all the questions from the repository
     *
     * @return list of questions
     */
    public List<Question> getQuestions() {

        return questionRepository.findAll();

    }


}
