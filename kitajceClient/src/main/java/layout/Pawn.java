package layout;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import kitajce.MainController;

public class Pawn extends Circle {

  private static final int radius = 14;
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
  }

  public void repaint(Field field) {
    this.setCenterX(field.getCenterX());
    this.setCenterY(field.getCenterY());
  }

  public String getColor() {
    return color;
  }

  public void setChosen(boolean chosen) {
    isChosen = chosen;
    if (!chosen) {
      this.setStrokeWidth(0);
//      MainController.xOfChosenPawn = 0;
//      MainController.yOfChosenPawn = 0;
    }
  }

  public boolean isChosen() {
    return isChosen;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
