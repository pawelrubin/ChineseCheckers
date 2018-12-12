package kitajce;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import layout.Board;
import layout.Field;
import layout.Player;
import layout.Pone;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class MainController {
  @FXML
  private Board board;

  @FXML
  private BorderPane borderPane;

  @FXML
  public void drawBoard() {
    board = new Board(6);
    borderPane.setCenter(board);

    for (int i = 0; i < board.getHeight(); i++) {
      double posY = ((i * 40) * sqrt(3) / 2 + 50);
      int offset = 0;
      for (int j = 0; j < board.widths[i]; j++) {
        if (j == 0) {
          for (int k = 0; k < board.offsetDraw[i]; k++) {
            offset = k * 40;
          }
        }
        int posX = j * 40 + 50;
        if ((i % 2) == 1) {
          board.getField(i, j + board.getOffset(i)).setCenterX(posX + offset + 20);
        } else {
          board.getField(i, j + board.getOffset(i)).setCenterX(posX + offset);
        }
        board.getField(i, j + board.getOffset(i)).setCenterY(posY);
        board.getField(i, j + board.getOffset(i)).setRadius(10);
        board.getField(i, j + board.getOffset(i)).setFill(Color.GRAY);

        board.getChildren().addAll(board.getField(i, j + board.getOffset(i)));
      }
    }
    drawPones();
  }

  private void drawPones() {
    for (int i = 0; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getHeight(); j++) {
        if (board.getPone(i, j) != null) {
          double x = board.getField(i, j).getCenterX();
          double y = board.getField(i, j).getCenterY();
          board.getPone(i, j).setCenterX(x);
          board.getPone(i, j).setCenterY(y);
          board.getPone(i, j).setRadius(9);

          //choosing color
          switch (board.getPone(i, j).getColor()) {
            case "GREEN": {
              board.getPone(i, j).setFill(Color.GREEN);
              break;
            }
            case "RED": {
              board.getPone(i, j).setFill(Color.RED);
              break;
            }
            case "BLUE": {
              board.getPone(i, j).setFill(Color.BLUE);
              break;
            }
            case "WHITE": {
              board.getPone(i, j).setFill(Color.WHITE);
              break;
            }
            case "BLACK": {
              board.getPone(i, j).setFill(Color.BLACK);
              break;
            }
            case "YELLOW": {
              board.getPone(i, j).setFill(Color.YELLOW);
              break;
            }
          }
          board.getChildren().addAll(board.getPone(i, j));
        }
      }
    }
  }

  @FXML
  private void startClient() {
    // create new thread to handle network communication
    new Thread(() -> {
      System.out.println("Kitajce client started.");
      String serverAddress = "localhost";
      try {
        Client client = new Client(serverAddress);
        client.play();
      } catch (Exception ex) {
        System.out.println("Connection Error: " + ex);
      }
    }).start();
  }
}
