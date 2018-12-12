package layout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.event.MouseEvent;

public class Field extends Circle {

  private int x;
  private int y;

  public Field(int x, int y) {
    this.x = x;
    this.y = y;

    setOnMousePressed(event -> {
      this.setStroke(Color.BLACK);
      this.setStrokeWidth(5);
    });
    setOnMouseReleased(event -> this.setStrokeWidth(0));
  }

  public Field(double centerX, double centerY, double radius, Paint fill) {
    super(centerX, centerY, radius, fill);
    
    setOnMousePressed(event -> {
      this.setStroke(Color.BLACK);
      this.setStrokeWidth(5);
    });

    setOnMouseReleased(event -> this.setStrokeWidth(0));
  }
}
