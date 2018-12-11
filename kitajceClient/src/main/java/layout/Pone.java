package layout;


import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Pone extends Circle {

  public Pone(double centerX, double centerY, double radius) {
    super(centerX, centerY, radius);
  }

  public Pone(double centerX, double centerY, double radius, Paint fill) {
    super(centerX, centerY, radius, fill);
  }
}
