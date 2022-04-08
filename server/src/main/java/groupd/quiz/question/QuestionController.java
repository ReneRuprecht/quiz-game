package groupd.quiz.question;

import groupd.quiz.question.request.QuestionCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(
        path = "api/v1/question"
)
@AllArgsConstructor
public class QuestionController {

    private QuestionService questionService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/create"
    )
    public ResponseEntity<Question> createQuestion(
            @RequestBody QuestionCreateRequest questionCreateRequest
    ) {

        return questionService.createQuestion(questionCreateRequest);
    }

    public List<Question> getQuestions(){
        return questionService.getQuestions();
    }

}
