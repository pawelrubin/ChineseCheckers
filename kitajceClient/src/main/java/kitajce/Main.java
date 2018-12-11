package kitajce;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import layout.Board;
import layout.Field;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Stage primaryStage;
    public static Parent root;

    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("../Main.fxml"));
        primaryStage.setTitle("Kitajce");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}
