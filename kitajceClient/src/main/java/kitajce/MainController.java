package kitajce;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import layout.Board;
import layout.Field;
import layout.Pawn;

import static java.lang.Math.sqrt;

public class MainController {
  @FXML
  private static Board board;
  @FXML
  private BorderPane borderPane;
  @FXML
  private Label label;

  private Client client;
  private int xOfChosenPawn = 0;
  private int yOfChosenPawn = 0;
  private String currentPlayer;
  private int moveCount = 0;
  private final String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};

  @FXML
  private void drawBoard() {
    board = new Board(6);
    currentPlayer = "GREEN";
    moveCount = 0;
    borderPane.setCenter(board);
    label.setText(currentPlayer);
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

        Field field = board.getField(i, j + board.getOffset(i));

        if ((i % 2) == 1) {
          field.setCenterX(posX + offset + 20);
        } else {
          field.setCenterX(posX + offset);
        }

        field.setCenterY(posY);
        field.setRadius(15);
        field.setFill(Color.GRAY);

        field.setOnMouseClicked(event -> {
          System.out.println("a field has been clicked.");
          System.out.println("x: " + field.getX() + ", y: " + field.getY());
          System.out.println(xOfChosenPawn + " --- " + yOfChosenPawn);
          if (!(xOfChosenPawn == 0 && yOfChosenPawn == 0)) {
            int tempX = xOfChosenPawn;
            int tempY = yOfChosenPawn;
            System.out.println("temp: x y : " + tempX + " | " + tempY);
            Pawn pawn = board.getPawn(tempX, tempY);

            pawn.setX(field.getX());
            pawn.setY(field.getY());
            pawn.repaint(field);
            pawn.setChosen(false);

            board.movePawn(tempX, tempY, field.getX(), field.getY());
            nextPlayer();
            xOfChosenPawn = 0;
            yOfChosenPawn = 0;
          }
        });

        board.getChildren().addAll(field);
      }
    }
    drawPawns();
  }

  private void drawPawns() {
    for (int i = 0; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getHeight(); j++) {
        if (board.getPawn(i, j) != null) {
          double x = board.getField(i, j).getCenterX();
          double y = board.getField(i, j).getCenterY();
          Pawn pawn = board.getPawn(i, j);
          pawn.setCenterX(x);
          pawn.setCenterY(y);
          pawn.setRadius(14);

          //choosing color
          switch (pawn.getColor()) {
            case "GREEN": {
              pawn.setFill(Color.GREEN);
              break;
            }
            case "RED": {
              pawn.setFill(Color.RED);
              break;
            }
            case "BLUE": {
              pawn.setFill(Color.BLUE);
              break;
            }
            case "WHITE": {
              pawn.setFill(Color.WHITE);
              break;
            }
            case "BLACK": {
              pawn.setFill(Color.BLACK);
              break;
            }
            case "YELLOW": {
              pawn.setFill(Color.YELLOW);
              break;
            }
          }

          pawn.setOnMouseClicked(event -> {
            if (currentPlayer.equals(pawn.getColor())) {
              pawn.setChosen(!pawn.isChosen());
              if (pawn.isChosen()) {
                int oldX = xOfChosenPawn;
                int oldY = yOfChosenPawn;
                Pawn oldPawn = board.getPawn(oldX, oldY);

                if (oldPawn != null) {
                  oldPawn.setChosen(false);
                  oldPawn.setStrokeWidth(0);
                }

                xOfChosenPawn = pawn.getX();
                yOfChosenPawn = pawn.getY();
                pawn.setStrokeWidth(5);
                pawn.setStroke(Color.PINK);
                System.out.println("a pawn has been chosen.");
                System.out.println("x: " + pawn.getX() + ", y: " + pawn.getY());
                System.out.println(xOfChosenPawn + " --- " + yOfChosenPawn);
              } else {
                System.out.println("the pawn is no longer the chosen one.");
                pawn.setStrokeWidth(0);
                xOfChosenPawn = 0;
                yOfChosenPawn = 0;
                System.out.println("x: " + pawn.getX() + ", y: " + pawn.getY());
                System.out.println(xOfChosenPawn + " --- " + yOfChosenPawn);
              }
            }
          });
          board.getChildren().addAll(pawn);
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
        client = new Client(serverAddress);
        client.play();
      } catch (Exception ex) {
        System.out.println("Connection Error: " + ex);
      }
    }).start();
  }

  private void nextPlayer() {
    moveCount++;
    currentPlayer = colors[moveCount%6];
    label.setText(currentPlayer);
  }


}
