package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * Behandelt das Erstellen eines Spiels in der mainmenu.fxml.
 * Verwaltet das Hauptmenü und bietet das Wechseln der Sicht zu verschiedenen Funktionen an.
 * @author Leonard Strauß
 */
public class MainMenuController {

    @FXML
    private Button creategameBtn;

    @FXML
    private Button entergameBtn;

    @FXML
    private Button submitQBtn;

    /**
     * Erstellt in neues Spiel mit dem Nutzernamen des aktuellen Spielers und sendet diese Daten an den Server.
     */
    public void creategameAction() throws IOException {
        MainController mainController = MainController.getInstance();

        String username = LoginController.activeUser.name;
        String body = "{\"username\":\"" + username + "\",\"gameId\":\"\"}";
        URL url = new URL("http://localhost:3000/api/v1/game/create");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", LoginController.activeUser.jsonWebToken);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);

            int status = con.getResponseCode();
            if (status == 200) {

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONObject object = new JSONObject(String.valueOf(response));

                mainController.waitlabel.setVisible(true);
                mainController.logoutBtn.setVisible(false);
                mainController.changeView("game");
                con.disconnect();

                String gameId = object.getString("gameId");

                JSONObject obj = printJSONObject("username", LoginController.activeUser.name, "gameId", gameId, "ready", true);


                String body2 = "{\"username\":\"" + obj.getString("username") + "\",\"gameId\":\"" + obj.getString("gameId") + "\",\"ready\":" + obj.getBoolean("ready") + "}";

                URL url2 = new URL("http://localhost:3000/api/v1/game/ready");
                HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                con2.setRequestMethod("POST");
                con2.setRequestProperty("Authorization", LoginController.activeUser.jsonWebToken);
                con2.setRequestProperty("Content-Type", "application/json; utf-8");
                con2.setRequestProperty("Accept", "application/json");
                con2.setDoOutput(true);

                try (OutputStream os2 = con2.getOutputStream()) {
                    byte[] input2 = body2.getBytes("utf-8");
                    os2.write(input2, 0, input2.length);

                    con2.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt ein JSONObject und füllt es mit den mitgegebenen Werten.
     * @return Das erstellte JSONObject
     */
    public JSONObject printJSONObject(String key1, String value1, String key2, String value2, String key3, boolean value3) {
        JSONObject jsonObject = new JSONObject();
        try {
            Field changeMap = jsonObject.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(jsonObject, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        JSONObject obj = new JSONObject();
        obj.put(key1, value1);
        obj.put(key2, value2);
        obj.put(key3, value3);

        return obj;
    }

    /**
     * Wechselt die Sicht zur connect.fxml.
     */
    public void connectAction() throws IOException {
        MainController mainController = MainController.getInstance();


        mainController.changeView("connect");
    }

    /**
     * Wechselt die Sicht zur createquestion.fxml
     */
    public void submitQAction() {
        MainController mainController = MainController.getInstance();

        mainController.changeView("createquestion");
    }
}


