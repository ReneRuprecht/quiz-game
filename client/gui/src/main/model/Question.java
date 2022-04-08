package main.model;

/**
 * Stellt eine Frage mit dazugehörigen vier Antworten dar.
 * @author Leonard Strauß
 */
public class Question {

    public String question;
    public String answer1;
    public String answer2;
    public String answer3;
    public String answer4;

    public Question(String question, String answer1, String answer2, String answer3, String answer4) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }
}
