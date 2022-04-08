package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Behandelt das Einloggen eines Spielers mit seinen eingegebenen Daten in der login.fxml.
 * Behandelt das Weiterleiten an den RegisterController, wenn der Spieler sich registrieren möchte.
 * @author Leonard Strauß
 */
public class LoginController {

    public static User activeUser;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginFailure;

    /**
     * Liest die eingegebenen Login-Daten aus und schickt diese mit dem JSON Web Token an den Server zur Überprüfung.
     */
    public void loginAction() throws IOException {

        MainController mainController = MainController.getInstance();

        String body = "{\"username\":\"" + username.getText().toString() + "\",\"password\":\"" + password.getText().toString() + "\"}";
        URL url = new URL("http://localhost:3000/api/v1/login");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);

            int status = con.getResponseCode();
            if (status == 200) {
                String header = con.getHeaderField("Authorization");
                activeUser = new User(username.getText().toString(), header);
                mainController.profilBtn.setVisible(true);
                mainController.logoutBtn.setVisible(true);
                mainController.backBtn.setVisible(true);
                mainController.changeView("mainmenu");
            } else {
                System.out.println("Error");
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Es gab Fehler!");
        }
    }

    /**
     * Leitet den Spieler bei Wunsch zur Registrierung weiter.
     */
    public void registerAction() throws IOException {
        MainController mainController = MainController.getInstance();

        mainController.backBtn.setVisible(true);

        mainController.changeView("register");
    }
}
