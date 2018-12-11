package settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import kitajce.Main;

import java.io.IOException;

public class SettingsController {

    public void back() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Main.fxml"));
        Main.root = loader.load();
        Main.primaryStage.setScene(new Scene(Main.root));
        Main.primaryStage.setMaximized(true);
    }

}
