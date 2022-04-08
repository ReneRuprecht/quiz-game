package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Behandelt das Hochladen eines Profilbilds in der profile.fxml.
 * @author Leonard Strauß
 */
public class ProfileController {

    @FXML
    private Button uploadpfpBtn;

    @FXML
    private Label pointslabel;

    @FXML
    private Label avgpointslabel;

    /**
     * Liest das hochgeladenen Profilbild ein und sendet dieses an den Server. *TO DO*
     */
    public void uploadpfpAction() throws IOException {
        MainController mainController = MainController.getInstance();

        try {
            URL url = new URL("http://localhost:3000/api/v1/user/upload-profile-picture");
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Authorization", LoginController.activeUser.jsonWebToken);
            con.setRequestProperty("Content-Type", "image/jpeg");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.connect();

            BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int i;
            while ((i = bis.read()) > 0) {
                bos.write(i);
            }
            bis.close();
            bos.close();

            InputStream inputStream;
            int responseCode = con.getResponseCode();
            if ((responseCode >= 200) && (responseCode <= 202)) {
                inputStream = con.getInputStream();
                int j;
                while ((j = inputStream.read()) > 0) {
                    System.out.println(j);
                }

            } else {
                inputStream = con.getErrorStream();
            }
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
