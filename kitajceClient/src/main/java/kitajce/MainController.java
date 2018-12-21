package kitajce;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import layout.Board;
import layout.Field;
import layout.Pawn;
import layout.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
  private String winner;
  private final String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  private List<Point> nodes = new ArrayList<>();
  private Random random = new Random();

  @FXML
  private void drawBoard() {
    board = new Board(6);
    currentPlayer = colors[0];
    moveCount = 0;
    borderPane.setCenter(board);
    label.setText(currentPlayer + "'s turn");
    for (int i = 0; i < board.getHeight(); i++) {
      double posY = ((i * 40) * sqrt(3) / 2 + 50);
      int offset = 0;
      for (int j = 0; j < board.getWidth(i); j++) {
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
          System.out.println("A field has been clicked.");
          System.out.println("X: " + field.getX() + ", Y: " + field.getY());
          System.out.println(xOfChosenPawn + " --- " + yOfChosenPawn);
          if (!(xOfChosenPawn == 0 && yOfChosenPawn == 0)) {
            int tempX = xOfChosenPawn;
            int tempY = yOfChosenPawn;
            System.out.println("Temp: x y : " + tempX + " | " + tempY);
            if (isValid(tempX, tempY, field)) {
              Pawn pawn = board.getPawn(tempX, tempY);

              client.sendMessage("MOVE " + pawn.getColor() + " " + pawn.getX() + " " + pawn.getY() +
                      " " + field.getX() + " " + field.getY());

              pawn.setX(field.getX());
              pawn.setY(field.getY());
              pawn.repaint(field);
              pawn.setChosen(false);

              board.movePawn(tempX, tempY, field.getX(), field.getY());
              nextPlayer();
              xOfChosenPawn = 0;
              yOfChosenPawn = 0;
              if (gameOver()) {
                System.out.println(winner + "player has won!");
              }
            }
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
          pawn.setStroke(Color.BLACK);
          pawn.setStrokeWidth(1);

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
                  oldPawn.setStroke(Color.BLACK);
                  oldPawn.setStrokeWidth(1);
                }

                xOfChosenPawn = pawn.getX();
                yOfChosenPawn = pawn.getY();
                pawn.setStrokeWidth(3);
                pawn.setStroke(Color.PINK);
                System.out.println("A pawn has been chosen.");
                System.out.println("X: " + pawn.getX() + ", Y: " + pawn.getY());
                System.out.println(xOfChosenPawn + " --- " + yOfChosenPawn);
              } else {
                System.out.println("The pawn is no longer the chosen one");
                pawn.setStroke(Color.BLACK);
                pawn.setStrokeWidth(1);
                xOfChosenPawn = 0;
                yOfChosenPawn = 0;
                System.out.println("X: " + pawn.getX() + ", Y: " + pawn.getY());
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

  @FXML
  private void nextPlayer() {
    moveCount++;
    currentPlayer = colors[moveCount % 6];
    label.setText(currentPlayer + "'s turn");
  }

  private int randomPlayer(int numberOfPlayers) {
    return random.nextInt(numberOfPlayers);
  }

  private boolean isValid(int oldX, int oldY, Field field) {

    nodes.clear();

    if (moveValidation(oldX, oldY, field)) {
      return true;
    }

    if (jumpRecursiveValidation(oldX, oldY, 0, 0, oldX, oldY, field)) {
      return true;
    }

    return false;
  }

  private boolean moveValidation(int oldX, int oldY, Field field) {
    int newX = field.getX();
    int newY = field.getY();

    //moving right [1, 0] and left[-1, 0]
    if (newY - oldY == 0) {
      if (newX - oldX == 1) {
        return true;
      }
      if (newX - oldX == -1) {
        return true;
      }
    }

    //moving top right [0, -1] and top left [-1, -1]
    if (newY - oldY == -1) {
      if (newX - oldX == 0) {
        return true;
      }
      if (newX - oldX == -1) {
        return true;
      }
    }

    //moving bottom right [1, 1] or bottom left [0, 1]
    if (newY - oldY == 1) {
      if (newX - oldX == 1) {
        return true;
      }
      if (newX - oldX == 0) {
        return true;
      }
    }

    return false;
  }

  private boolean jumpRecursiveValidation(int oldX, int oldY, int offsetX, int offsetY, int originalX, int originalY, Field field) {
    int newX = field.getX();
    int newY = field.getY();

    if (oldX == newX && oldY == newY) {
      return true;
    }

    for (Point i : nodes) {
      for (Point j : nodes) {
        if (i != j) {
          if (i.equals(j)) {
            nodes.remove(nodes.size() - 1);
            return false;
          }
        }
      }
    }

    if (oldX == originalX && oldY == originalY && (offsetX != 0 || offsetY != 0)) {
      return false;
    }

    //jumping right [2, 0]
    if ((offsetX != -2 || offsetY != 0)
      && oldX <= board.getHeight() - 3
      && board.getField(oldX + 2, oldY) != null
      && board.getPawn(oldX + 2, oldY) == null
      && board.getPawn(oldX + 1, oldY) != null) {
      nodes.add(new Point(oldX + 2, oldY));
      if (jumpRecursiveValidation(oldX + 2, oldY, 2, 0, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping left [-2, 0]
    if ((offsetX != 2 || offsetY != 0)
      && oldX >= 2
      && board.getField(oldX - 2, oldY) != null
      && board.getPawn(oldX - 2, oldY) == null
      && board.getPawn(oldX - 1, oldY) != null) {
      nodes.add(new Point(oldX - 2, oldY));
      if (jumpRecursiveValidation(oldX - 2, oldY, -2, 0, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping top right [0, -2]
    if ((offsetX != 0 || offsetY != 2)
      && oldY >= 2
      && board.getField(oldX, oldY - 2) != null
      && board.getPawn(oldX, oldY - 2) == null
      && board.getPawn(oldX, oldY - 1) != null) {
      nodes.add(new Point(oldX, oldY - 2));
      if (jumpRecursiveValidation(oldX, oldY - 2, 0, -2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping top left [-2, -2]
    if ((offsetX != 2 || offsetY != 2)
      && oldX >= 2 && oldY >= 2
      && board.getField(oldX - 2, oldY - 2) != null
      && board.getPawn(oldX - 2, oldY - 2) == null
      && board.getPawn(oldX - 1, oldY - 1) != null) {
      nodes.add(new Point(oldX - 2, oldY - 2));
      if (jumpRecursiveValidation(oldX - 2, oldY - 2, -2, -2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping bottom right [2, 2]
    if ((offsetX != -2 || offsetY != -2)
      && oldX <= board.getHeight() - 3 && oldY <= board.getHeight() - 3
      && board.getField(oldX + 2, oldY + 2) != null
      && board.getPawn(oldX + 2, oldY + 2) == null
      && board.getPawn(oldX + 1, oldY + 1) != null) {
      nodes.add(new Point(oldX + 2, oldY + 2));
      if (jumpRecursiveValidation(oldX + 2, oldY + 2, 2, 2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping bottom left [0, 2]
    if ((offsetX != 0 || offsetY != -2)
      && oldY <= board.getHeight() - 3
      && board.getField(oldX, oldY + 2) != null
      && board.getPawn(oldX, oldY + 2) == null
      && board.getPawn(oldX, oldY + 1) != null) {
      nodes.add(new Point(oldX, oldY + 2));
      if (jumpRecursiveValidation(oldX, oldY + 2, 0, 2, originalX, originalY, field)) {
        return true;
      }
    }

    if (nodes.size() > 0) {
      nodes.remove(nodes.size() - 1);
    }

    return false;
  }

  private boolean greenWinningCondition() {
    for (int i = 13; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getWidth(i); j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("GREEN")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean whiteWinningCondition() {
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < board.getWidth(i - 9); j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("WHITE")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean redWinningCondition() {
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("RED")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean yellowWinningCondition() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < board.getWidth(i); j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("YELLOW")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean blackWinningCondition() {
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("BLACK")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean blueWinningCondition() {
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < board.getWidth(i); j++) {
        int counter = 0;
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals("BLUE")) {
          counter++;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean gameOver() {
    if (greenWinningCondition()) {
      winner = "GREEN";
      return true;
    }
    if (yellowWinningCondition()) {
      winner = "YELLOw";
      return true;
    }
    if (blackWinningCondition()) {
      winner = "BLACK";
      return true;
    }
    if (whiteWinningCondition()) {
      winner = "WHITE";
      return true;
    }
    if (redWinningCondition()) {
      winner = "RED";
      return true;
    }
    if (blueWinningCondition()) {
      winner = "BLUE";
      return true;
    }
    return false;
  }

  public static void movePawn(int pawnX, int pawnY, int fieldX, int fieldY) {
    Pawn pawn = board.getPawn(pawnX, pawnY);
    Field field = board.getField(fieldX, fieldY);
    board.movePawn(pawnX, pawnY, fieldX, fieldY);
    pawn.setX(field.getX());
    pawn.setY(field.getY());
    pawn.repaint(field);
  }
}
