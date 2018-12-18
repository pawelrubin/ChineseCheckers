package kitajce;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

  private static Stage primaryStage;
  private static Parent root;

  public void start(Stage primaryStage) throws Exception {
    Main.primaryStage = primaryStage;
    root = FXMLLoader.load(getClass().getResource("../Main.fxml"));
    primaryStage.setTitle("Kitajce");
    primaryStage.setScene(new Scene(root));
    primaryStage.setMaximized(false);
    primaryStage.setResizable(true);
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch();
  }
}
