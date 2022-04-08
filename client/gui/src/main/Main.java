package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Behandelt das Erstellen der Applikation und stellt diese dem Nutzer dar.
 * @author Leonard Strauß
 */
public class Main extends Application {

    public static final String APP_TITLE = "Quiz";

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Beschreibt die Größe und den Titel der Applikation und zeigt diesem dem Nutzer.
     */
    public void start(Stage stage) throws ExecutionException, InterruptedException {
        Node main = loadFXML("resources/main.fxml");
        Scene scene = new Scene((Parent) main, 1024, 768);
        stage.setScene(scene);
        stage.setTitle(APP_TITLE);
        stage.setMinWidth(1366);
        stage.setMinHeight(768);
        stage.show();
    }

    public static Node loadFXML(String fxmlFilename) {
        try {
            return FXMLLoader.load(Main.class.getClassLoader().getResource(fxmlFilename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
