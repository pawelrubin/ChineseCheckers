package kitajce;

import game.GameMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class MainController {
    @FXML private TextFlow textFlow;
    @FXML public VBox menu;

    private FXMLLoader loader;
    
    public void exitButton() {
        System.exit(0);
    }

    public void summonGameMenu() throws IOException {
        loader = new FXMLLoader(getClass().getResource("../Game.fxml"));
        BorderPane elo = (BorderPane) Main.root;
        elo.setCenter((VBox)loader.load());
    }

    public void summonSettingsMenu() throws IOException {
        loader = new FXMLLoader(getClass().getResource("../Settings.fxml"));
        BorderPane elo = (BorderPane) Main.root;
        elo.setCenter((VBox)loader.load());
    }
}
