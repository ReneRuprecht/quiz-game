package groupd.quiz.question.validator;

import groupd.quiz.question.request.QuestionCreateRequest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuestionValidator {

    public boolean isQuestionCreateRequestValid(QuestionCreateRequest questionCreateRequest) {

        boolean status = true;

        String question = questionCreateRequest.getQuestion();

        if (question == null || question.isEmpty() || question.trim().isEmpty()) {
            return false;
        }

        String answerOne = questionCreateRequest.getAnswerOne();

        if (answerOne == null || answerOne.isEmpty() || answerOne.trim().isEmpty()) {
            return false;
        }
        String answerTwo = questionCreateRequest.getAnswerTwo();

        if (answerTwo == null || answerTwo.isEmpty() || answerTwo.trim().isEmpty()) {
            return false;
        }
        String answerThree = questionCreateRequest.getAnswerThree();

        if (answerThree == null || answerThree.isEmpty() || answerThree.trim().isEmpty()) {
            return false;
        }
        String answerFour = questionCreateRequest.getAnswerFour();

        if (answerFour == null || answerFour.isEmpty() || answerFour.trim().isEmpty()) {
            return false;
        }
        String answerCorrect = questionCreateRequest.getAnswerCorrect();

        if (answerCorrect == null || answerCorrect.isEmpty() || answerCorrect.trim().isEmpty()) {
            return false;
        }

        return status;
    }
}
