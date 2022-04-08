package groupd.quiz.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity(name = "Question")
@Table(
        name = "question"

)
@Setter
@NoArgsConstructor
public class Question {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "question",
            nullable = false

    )
    private String question;
    @Column(
            name = "answer_one",
            nullable = false

    )
    private String answerOne;
    @Column(
            name = "answer_two",
            nullable = false

    )
    private String answerTwo;
    @Column(
            name = "answer_three",
            nullable = false

    )
    private String answerThree;
    @Column(
            name = "answer_four",
            nullable = false

    )
    private String answerFour;
    @JsonIgnore
    @Column(
            name = "answer_correct",
            nullable = false

    )
    private String correctAnswer;

    public Question(String question,
                    String answerOne,
                    String answerTwo,
                    String answerThree,
                    String answerFour,
                    String correctAnswer) {
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
        this.correctAnswer = correctAnswer;
    }



}
