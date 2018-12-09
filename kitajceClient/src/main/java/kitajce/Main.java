package kitajce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../Main.fxml"));
        primaryStage.setTitle("Kitajce");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
