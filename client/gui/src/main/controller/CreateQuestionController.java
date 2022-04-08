package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Behandelt das Erstellen von Fragen und speichert diese in der Datenbank in der createquestion.fxml.
 * @author Leonard Strauß
 */
public class CreateQuestionController {

    @FXML
    private TextField question;

    @FXML
    private TextField correctanswer;

    @FXML
    private TextField answer1;

    @FXML
    private TextField answer2;

    @FXML
    private TextField answer3;

    @FXML
    private TextField answer4;

    @FXML
    private Button submitBtn;

    /**
     *Lässt den Spieler mithilfe eines Formulars neue Fragen erstellen.
     */
    public void submitQAction() throws IOException {
        MainController mainController = MainController.getInstance();

        String body = "{\"question\":\"" + question.getText().toString() + "\",\"answerOne\":\"" + answer1.getText().toString() + "\",\"answerTwo\":\"" + answer2.getText().toString() + "\",\"answerThree\":\"" + answer3.getText().toString() + "\",\"answerFour\":\"" + answer4.getText().toString() + "\",\"answerCorrect\":\"" + correctanswer.getText().toString() + "\"}";
        URL url = new URL("http://localhost:3000/api/v1/question/create");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", LoginController.activeUser.jsonWebToken);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int status = con.getResponseCode();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            if (status == 201) {
                con.disconnect();
                mainController.changeView("mainmenu");
            } else {
                System.out.println("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Es gab Fehler!");
        }
    }
}
