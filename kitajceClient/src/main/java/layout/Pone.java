package layout;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Pone extends Circle {

  private static final int radius = 9;
  // (X,Y) on board
  private int x;
  private int y;
  private String color;

  // TODO: no pionki musza byc tworzone na konktretnych wspolrzednych na planszy

  public Pone(int x, int y, String color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }

  public String getColor() {
    return color;
  }


  public Pone(double centerX, double centerY, Paint fill) {
    super(centerX, centerY, radius, fill);

    setOnMousePressed(event -> {
      this.setStroke(Color.PURPLE);
      this.setStrokeWidth(5);
    });

    setOnMouseReleased(event -> this.setStrokeWidth(0));
  }
}
