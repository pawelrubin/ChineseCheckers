package layout;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Pone extends Circle {

  public Pone(double centerX, double centerY, double radius, Paint fill) {
    super(centerX, centerY, radius, fill);

    setOnMousePressed(event -> {
      this.setStroke(Color.PURPLE);
      this.setStrokeWidth(5);
    });

    setOnMouseReleased(event -> this.setStrokeWidth(0));
  }
}
