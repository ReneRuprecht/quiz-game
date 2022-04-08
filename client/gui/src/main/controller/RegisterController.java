package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Behandelt das Registrieren eines neuen Nutzers in der register.fxml.
 * @author Leonard Strauß
 */
public class RegisterController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password1;

    @FXML
    private Button pfpBtn;

    @FXML
    private Button registerBtn;

    /**
     * Sendet die eingegebenen Daten eines neuen Nutzers an den Server und registriert diesen.
     * Wechselt nach Registrierung die Sicht zum Login.
     */
    public void registerAction() throws IOException {

        MainController mainController = MainController.getInstance();


        String body = "{\"username\":\"" + username.getText().toString() + "\",\"password\":\"" + password1.getText().toString() + "\"}";
        URL url = new URL("http://localhost:3000/api/v1/registration/register");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
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
            con.disconnect();
            mainController.profilBtn.setVisible(true);
            mainController.logoutBtn.setVisible(true);

            mainController.changeView("login");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Es gab Fehler!");
        }


    }
}
