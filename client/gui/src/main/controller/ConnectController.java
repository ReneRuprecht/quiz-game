package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import main.model.Opponents;
import main.model.Question;
import org.json.JSONException;
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
 * Behandelt das Laden der bestehenden Spiele in die Spieleliste der connect.fxml.
 * Behandelt das Verbinden mit dem ausgewählten Spiel.
 * @author Leonard Strauß
 */
public class ConnectController {

    public static Question currentQuestion;

    public static Opponents opponents;

    public static String gameId1;

    @FXML
    private ListView gameslist;

    /**
     * Lädt alle bestehenden Spiele in die ListView Spieleliste.
     */
    public void loadgamesAction() throws IOException, JSONException {
        URL url = new URL("http://localhost:3000/api/v1/game/getGames");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", LoginController.activeUser.jsonWebToken);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        int status2 = con.getResponseCode();
        if (status2 == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            JSONObject object = new JSONObject(String.valueOf(response));

            if (object.names().length() == 0) {
                System.out.println("Keine Spiele gefunden!");
            } else {
                for (int i = 0; i < object.names().length(); i++) {
                    String value = object.get(object.names().getString(i)).toString();
                    JSONObject valueString = new JSONObject(value);
                    String username = valueString.getJSONObject("playerOne").getString("username");
                    String gameId = valueString.getString("gameId");
                    String body = "{\"username\":\"" + username + "\",\"gameId\":\"" + gameId + "\"}";
                    gameslist.getItems().add(body);
                }
            }
            con.disconnect();
        }
    }

    /**
     * Verbindet den aktuellen Spieler mit dem in der Liste ausgewählten Spiel.
     * Setzt den Status des Spielers auf ready = true.
     */
    public void connectAction() throws IOException, JSONException {
        MainController mainController = MainController.getInstance();

        String game = gameslist.getSelectionModel().getSelectedItem().toString();
        JSONObject gameIdValue = new JSONObject(game);
        String gameId = gameIdValue.getString("gameId");
        gameId1 = gameId;
        String username = LoginController.activeUser.name;
        String body = "{\"username\":\"" + username + "\",\"gameId\":\"" + gameId + "\"}";

        URL url = new URL("http://localhost:3000/api/v1/game/connect");
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

                String currentquestion = object.getJSONObject("currentQuestion").getString("question");
                String answer1 = object.getJSONObject("currentQuestion").getString("answerOne");
                String answer2 = object.getJSONObject("currentQuestion").getString("answerTwo");
                String answer3 = object.getJSONObject("currentQuestion").getString("answerThree");
                String answer4 = object.getJSONObject("currentQuestion").getString("answerFour");

                opponents = new Opponents(object.getJSONObject("playerOne").getString("username"), LoginController.activeUser.name, 0, 0);
                currentQuestion = new Question(currentquestion, answer1, answer2, answer3, answer4);

                mainController.waitlabel.setVisible(false);
                mainController.logoutBtn.setVisible(false);
                mainController.changeView("game");
                con.disconnect();

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

                    int status2 = con2.getResponseCode();
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
    public JSONObject printJSONObject(String key1, String value1, String key2, String value2, String key3,
                                      boolean value3) {
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
}