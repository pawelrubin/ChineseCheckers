package layout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import kitajce.MainController;

public class Field extends Circle {

  private int x;
  private int y;

  Field(int x, int y) {
    this.x = x;
    this.y = y;

    setOnMousePressed(event -> {
      this.setStroke(Color.BLACK);
      this.setStrokeWidth(5);
    });
    setOnMouseReleased(event -> this.setStrokeWidth(0));

    setOnMouseClicked(event -> {
      System.out.println("a field has been clicked.");
      System.out.println("x: " + this.x + ", y: " + this.y);
      System.out.println(MainController.xOfChosenPawn + " --- " + MainController.yOfChosenPawn);
      if (!(MainController.xOfChosenPawn == 0 && MainController.yOfChosenPawn == 0)) {
        int tempX = MainController.xOfChosenPawn;
        int tempY = MainController.yOfChosenPawn;
        System.out.println("temp: x y : " + tempX + " | " + tempY);
        Pawn pawn = MainController.board.getPawn(tempX, tempY);

        pawn.setXY(this.x, this.y);
        pawn.repaint(this);
        pawn.setChosen(false);

        MainController.board.movePawn(tempX, tempY, this.x, this.y);
        MainController.currentPlayer = MainController.nextPlayer();
        MainController.xOfChosenPawn = 0;
        MainController.yOfChosenPawn = 0;
      }
    });
  }
}
