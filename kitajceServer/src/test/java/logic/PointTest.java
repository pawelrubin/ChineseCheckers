package logic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {

  private Point a, b;

  @Before
  public void preparePoints() {
    a = new Point(0, 1);
    b = new Point(0, 1);
  }

  @Test
  public void equals() {
    assertEquals(a, b);
  }

}