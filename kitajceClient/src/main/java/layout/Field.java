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
