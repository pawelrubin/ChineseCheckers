package layout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
