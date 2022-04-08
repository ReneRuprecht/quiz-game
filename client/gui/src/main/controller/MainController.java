package main.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import main.Main;

/**
 * Behandelt das Wechseln der Sicht in der main.fxml.
 * Verwaltet auch die aktuelle Instanz der Sicht und diese wird bei jedem weiteren Controller verwendet.
 * @author Leonard Strauß
 */
public class MainController {

    private static MainController instance;

    public static MainController getInstance() {
        return instance;
    }

    @FXML
    StackPane viewHolder;

    @FXML
    Button profilBtn;
    @FXML
    Button logoutBtn;
    @FXML
    Label waitlabel;

    @FXML
    Button backBtn;

    @FXML
    public void initialize() {
        instance = this;
    }

    /**
     * Wechselt zur Sicht, welche als Parameter mitgegeben wird.
     * @param fxmlFilename Name der gewünschten Sicht.
     */
    public void changeView(String fxmlFilename) {
        Node view = Main.loadFXML("resources/" + fxmlFilename + ".fxml");
        viewHolder.getChildren().setAll(view);
    }

    public void logoutAction() {
        profilBtn.setVisible(false);
        logoutBtn.setVisible(false);
        backBtn.setVisible(false);

        changeView("login");
    }


    public void profilAction() {
        profilBtn.setVisible(false);

        changeView("profile");
    }
}

