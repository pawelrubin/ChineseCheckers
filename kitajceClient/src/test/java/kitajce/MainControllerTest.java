package kitajce;

import org.junit.BeforeClass;
import org.junit.Test;

public class MainControllerTest {

  private static MainController instance;

  @BeforeClass
  public static void setUp() {
    instance = new MainController();
  }

  @Test
  public void initialize() {
    instance.initialize();
  }

  @Test
  public void drawBoard() {
    instance.drawBoard();
  }

}