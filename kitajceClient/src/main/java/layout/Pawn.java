package layout;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pawn extends Circle {

  // (X,Y) on board
  private int x;
  private int y;
  private final String color;
  private boolean isChosen = false;

  Pawn(int x, int y, String color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }

  /**
   * Repaints a pawn.
   * @param field New location of the pawn.
   */
  public void repaint(Field field) {
    this.setCenterX(field.getCenterX());
    this.setCenterY(field.getCenterY());
  }

  /**
   * color getter.
   * @return this.color
   */
  public String getColor() {
    return color;
  }

  /**
   * isChosen setter.
   * Disables chosen stroke.
   * @param chosen new isChosen value.
   */
  public void setChosen(boolean chosen) {
    isChosen = chosen;
    if (!chosen) {
      this.setStrokeWidth(1);
      this.setStroke(Color.BLACK);
    }
  }

  /**
   * isChosen getter.
   * @return this.isChosen
   */
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
