package kitajce;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import layout.Board;
import layout.Field;
import layout.Pawn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

@SuppressWarnings("WeakerAccess")
public class MainController {
  @FXML
  private TextField textField;
  @FXML
  private Board board;
  @FXML
  private Label colorLabel;
  @FXML
  private BorderPane borderPane;
  @FXML
  private Label turnLabel;
  @FXML
  private Label winningLabel;
  @FXML
  private Label tiesLabel;

  private Client client;
  private String winnersMsg = "Winners:\n";
  private String tiesMsg = "Ties:\n";
  private String color;
  private int numOfPlayers;
  private List<String> winnersList;
  private List<String> tiesList;
  private int xOfChosenPawn = 0;
  private int yOfChosenPawn = 0;
  private String currentPlayer;

  /**
   * Initializes client object and sets controller.
   */
  @FXML
  public void initialize() {
    client = new Client();
    client.setController(this);
    winnersList = new ArrayList<>();
    tiesList = new ArrayList<>();
  }

  /**
   * Method activated by the connect button.
   */
  @FXML
  public void startClient() {
    // create new thread to handle network communication
    new Thread(() -> {
      System.out.println("Kitajce client started.");
      try {
        client.setConnection(textField.getText());
        client.play();
      } catch (IOException ex) {
        System.out.println("Connection Error: " + ex);
      }
    }).start();
  }

  /**
   * Method activated by the end turn button.
   */
  @FXML
  public void endTurn() {
    client.sendMessage("END_TURN " + color);
  }

  /**
   * Draws the board in the BorderPane.
   */
  void drawBoard() {
    board = new Board(numOfPlayers);
    borderPane.setCenter(board);
    turnLabel.setText(currentPlayer + "'s turn");
    colorLabel.setText("You are " + color + " player");
    drawFields();
    drawPawns();
  }

  /**
   * Adds a winner to the winners list, changes the label.
   * @param color A color of the winner.
   */
  void addWinner(String color) {
    winnersList.add(color);
    winnersMsg += winnersList.size() + ". " + winnersList.get(winnersList.size() - 1) + "\n";
    winningLabel.setText(winnersMsg);
    disablePawns(color);
  }

  void addTie(String color) {
    tiesList.add(color);
    tiesMsg += "- " + tiesList.get((tiesList.size() - 1)) + "\n";
    tiesLabel.setText(tiesMsg);
    disablePawns(color);
  }

  /**
   * Moves pawn.
   * @param pawnX Pawn's x coordinate.
   * @param pawnY Pawn's y coordinate.
   * @param fieldX Target field x coordinate.
   * @param fieldY Target field y coordinate.
   */
  void movePawn(int pawnX, int pawnY, int fieldX, int fieldY) {
    Pawn pawn = board.getPawn(pawnX, pawnY);
    Field field = board.getField(fieldX, fieldY);
    board.movePawn(pawnX, pawnY, fieldX, fieldY);
    if (pawn != null) {
      pawn.setX(field.getX());
      pawn.setY(field.getY());
      pawn.repaint(field);
      pawn.setChosen(false);
    }
    xOfChosenPawn = 0;
    yOfChosenPawn = 0;
  }

  /**
   * Color setter.
   * @param color A color to be set.
   */
  void setColor(String color) {
    this.color = color;
  }

  /**
   * NumOfPlayers setter.
   * @param numOfPlayers A number of players to be set.
   */
  void setNumOfPlayers(int numOfPlayers) {
    this.numOfPlayers = numOfPlayers;
  }

  /**
   * currentPlayer setter.
   * @param currentPlayer A color of a player to be set to the currentPlayer.
   */
  void setCurrentPlayer(String currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  /**
   * Updates turnLabel
   * @param color A color of one's turn.
   */
  void updateTurnLabel(String color) {
    turnLabel.setText(color + "'s turn");
  }

  private void drawFields() {
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
            Pawn pawn = board.getPawn(tempX, tempY);

            client.sendMessage("MOVE " + pawn.getColor() + " " + pawn.getX() + " " + pawn.getY() +
                    " " + field.getX() + " " + field.getY());
            turnLabel.setText(currentPlayer);
          }
        });

        board.getChildren().addAll(field);
      }
    }
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

          //choosing currentPlayer
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
            if (color.equals(pawn.getColor()) && currentPlayer.equals(pawn.getColor())) {
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

  private void disablePawns(String color) {
    for (int i = 0; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getHeight(); j++) {
        if (board.getPawn(i, j) != null && board.getPawn(i, j).getColor().equals(color)) {
          board.getPawn(i, j).setOnMouseClicked(null);
        }
      }
    }
  }
}
