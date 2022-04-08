package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.SocketClient;
import org.json.JSONObject;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * Behandelt das Vorbereiten des Spiels in der game.fxml.
 * Behandelt das Abschicken einer ausgewählten Antwort.
 * @author Leonard Strauß
 */
public class GameController {

    @FXML
    private Label question;

    @FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

    @FXML
    private Button answer4;

    @FXML
    private Label name1;

    @FXML
    private Label name2;

    @FXML
    private Label points1;

    @FXML
    private Label points2;

    @FXML
    private Label gameover;

    @FXML
    private Button closegame;

    public SocketClient socketClient;
    public StompSession stompSession;
    public ListenableFuture<StompSession> f;

    /**
     *Bereitet das Spiel vor, indem es die Namen der Spieler und die erste Frage mit den Antworten darstellt.
     *Startet auch die Verbindung zum Websocket Server.
     */
    public void startAction() throws ExecutionException, InterruptedException {
        MainController mainController = MainController.getInstance();

        String dst = "/topic/game-" + ConnectController.gameId1;
        this.socketClient = new SocketClient();
        this.f = socketClient.connect();
        this.stompSession = f.get();
        socketClient.subscribeGreetings(stompSession, dst);

        question.setText(ConnectController.currentQuestion.question);
        answer1.setText(ConnectController.currentQuestion.answer1);
        answer2.setText(ConnectController.currentQuestion.answer2);
        answer3.setText(ConnectController.currentQuestion.answer3);
        answer4.setText(ConnectController.currentQuestion.answer4);
        name1.setText(ConnectController.opponents.user1);
        name2.setText(ConnectController.opponents.user2);
        points1.setText(String.valueOf(ConnectController.opponents.points1));
        points2.setText(String.valueOf(ConnectController.opponents.points2));

    }

    /**
     * Schickt die vom Spieler ausgewählte Antwort zum Websocket Server.
     * @param actionEvent Der angeklickte Button mit der Antwort
     */
    public void answerAction(ActionEvent actionEvent) throws InterruptedException {
        MainController mainController = MainController.getInstance();
        Button button = (Button) actionEvent.getSource();
        String answer = button.getText();
        String gameId = ConnectController.gameId1;

        String dst = "/app/gameplay-" + ConnectController.gameId1;
        String msg = "{\"gameId\":\"" + gameId + "\",\"senderUsername\":\"" + LoginController.activeUser.name + "\",\"answer\":\"" + answer + "\"}";
        socketClient.sendMsg(stompSession, msg, dst);

        Thread.sleep(500);

        String response = SocketClient.msg;
        System.out.println(response);
        JSONObject obj = new JSONObject(response);
        String correctness = obj.getJSONObject("headers").getJSONArray("Info").getString(0);
        String over = obj.getJSONObject("headers").getJSONArray("Info").getString(0);

        if (!"game_over".equals(over)) {
            if(correctness.equals("answer_correct")) {
                question.setText(obj.getJSONObject("body").getJSONObject("currentQuestion").getString("question"));
                answer1.setText(obj.getJSONObject("body").getJSONObject("currentQuestion").getString("answerOne"));
                answer2.setText(obj.getJSONObject("body").getJSONObject("currentQuestion").getString("answerTwo"));
                answer3.setText(obj.getJSONObject("body").getJSONObject("currentQuestion").getString("answerThree"));
                answer4.setText(obj.getJSONObject("body").getJSONObject("currentQuestion").getString("answerFour"));

                points1.setText(String.valueOf(obj.getJSONObject("body").getJSONObject("playerOne").getJSONObject("scoreObject").getInt("currentScoreValue")));
                points2.setText(String.valueOf(obj.getJSONObject("body").getJSONObject("playerTwo").getJSONObject("scoreObject").getInt("currentScoreValue")));
            } else {
                System.out.println("Antwort nicht korrekt! Gegner ist an der Reihe!");
            }
        } else if(over.equals("game_over")) {
            points1.setText(String.valueOf(obj.getJSONObject("body").getJSONObject("playerOne").getJSONObject("scoreObject").getInt("currentScoreValue")));
            points2.setText(String.valueOf(obj.getJSONObject("body").getJSONObject("playerTwo").getJSONObject("scoreObject").getInt("currentScoreValue")));
            System.out.println("Das Spiel ist zu Ende");
            System.out.println("Der Gewinner ist: " + (obj.getJSONObject("body").getString("winner")));;
            gameover.setText("SPIELENDE! DER GEWINNER IST: " + (obj.getJSONObject("body").getString("winner")));
            gameover.setVisible(true);
            closegame.setVisible(true);
        }
    }

    public void closegameAction() {
        MainController mainController = MainController.getInstance();

        mainController.changeView("mainmenu");
    }
}
