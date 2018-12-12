package layout;


import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Pone extends Circle {

  private static final int radius = 15;
  // (X,Y) on board
  private int X;
  private int Y;

  // TODO: no pionki musza byc tworzone na konktretnych wspolrzednych na planszy
  public Pone(double centerX, double centerY) {
    super(centerX, centerY, radius);
  }

  public Pone(double centerX, double centerY, Paint fill) {
    super(centerX, centerY, radius, fill);
  }
}
