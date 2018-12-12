package layout;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Pone extends Circle {

  private static final int radius = 15;
  // (X,Y) on board
  private int x;
  private int y;

  // TODO: no pionki musza byc tworzone na konktretnych wspolrzednych na planszy

  public Pone(double centerX, double centerY, Paint fill) {
    super(centerX, centerY, radius, fill);

    setOnMousePressed(event -> {
      this.setStroke(Color.PURPLE);
      this.setStrokeWidth(5);
    });

    setOnMouseReleased(event -> this.setStrokeWidth(0));
  }
}
