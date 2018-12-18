package kitajce;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import layout.Board;

import static java.lang.Math.sqrt;

public class MainController {
  @FXML
  public static Board board;

  @FXML
  private BorderPane borderPane;

  public static int xOfChosenPawn = 0;
  public static int yOfChosenPawn = 0;

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
        board.getField(i, j + board.getOffset(i)).setRadius(15);
        board.getField(i, j + board.getOffset(i)).setFill(Color.GRAY);

        board.getChildren().addAll(board.getField(i, j + board.getOffset(i)));
      }
    }
    drawPawns();
//    board.movePone("YELLOW");
  }

  private void drawPawns() {
    for (int i = 0; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getHeight(); j++) {
        if (board.getPawn(i, j) != null) {
          double x = board.getField(i, j).getCenterX();
          double y = board.getField(i, j).getCenterY();
          board.getPawn(i, j).setCenterX(x);
          board.getPawn(i, j).setCenterY(y);
          board.getPawn(i, j).setRadius(14);

          //choosing color
          switch (board.getPawn(i, j).getColor()) {
            case "GREEN": {
              board.getPawn(i, j).setFill(Color.GREEN);
              break;
            }
            case "RED": {
              board.getPawn(i, j).setFill(Color.RED);
              break;
            }
            case "BLUE": {
              board.getPawn(i, j).setFill(Color.BLUE);
              break;
            }
            case "WHITE": {
              board.getPawn(i, j).setFill(Color.WHITE);
              break;
            }
            case "BLACK": {
              board.getPawn(i, j).setFill(Color.BLACK);
              break;
            }
            case "YELLOW": {
              board.getPawn(i, j).setFill(Color.YELLOW);
              break;
            }
          }
          board.getChildren().addAll(board.getPawn(i, j));
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
