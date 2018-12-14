package layout;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import kitajce.MainController;

public class Pawn extends Circle {

  private static final int radius = 9;
  // (X,Y) on board
  private int x;
  private int y;
  private String color;
  private boolean isChosen = false;

  // TODO: no pionki musza byc tworzone na konktretnych wspolrzednych na planszy

  Pawn(int x, int y, String color) {
    this.x = x;
    this.y = y;
    this.color = color;

    // choosing the pawn
    setOnMouseClicked(event -> {
      isChosen ^= true;
      if (isChosen) {
        int oldX = MainController.xOfChosenPawn;
        int oldY = MainController.yOfChosenPawn;
        Pawn oldPawn = MainController.board.getPawn(oldX, oldY);

        if (oldPawn != null) {
          oldPawn.setChosen(false);
          oldPawn.setStrokeWidth(0);
        }

        MainController.xOfChosenPawn = this.x;
        MainController.yOfChosenPawn = this.y;
        this.setStrokeWidth(5);
        this.setStroke(Color.PINK);
        System.out.println("a pawn has been chosen.");
        System.out.println("x: " + this.x + ", y: " + this.y);
        System.out.println(MainController.xOfChosenPawn + " --- " + MainController.yOfChosenPawn);
      } else {
        System.out.println("the pawn is no longer the chosen one.");
        this.setStrokeWidth(0);
        MainController.xOfChosenPawn = 0;
        MainController.yOfChosenPawn = 0;
        System.out.println("x: " + this.x + ", y: " + this.y);
        System.out.println(MainController.xOfChosenPawn + " --- " + MainController.yOfChosenPawn);

      }
    });
  }

  void repaint(Field field) {
    this.setCenterX(field.getCenterX());
    this.setCenterY(field.getCenterY());
  }

  public String getColor() {
    return color;
  }

  void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  void setChosen(boolean chosen) {
    isChosen = chosen;
    if (!chosen) {
      this.setStrokeWidth(0);
      MainController.xOfChosenPawn = 0;
      MainController.yOfChosenPawn = 0;
    }
  }
}
