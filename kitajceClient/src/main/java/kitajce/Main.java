package kitajce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../Main.fxml"));
    primaryStage.setTitle("Kitajce");
    primaryStage.setScene(new Scene(root));
    primaryStage.setMaximized(false);
    primaryStage.setResizable(true);
    primaryStage.setMinHeight(720);
    primaryStage.setMinWidth(1280);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
