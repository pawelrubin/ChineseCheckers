class Point {
  private int x;
  private int y;

  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  private int getX() {
    return x;
  }

  private int getY() {
    return y;
  }

  @Override
  public boolean equals(Object object) {
    Point point;
    if (object instanceof Point) {
      point = (Point) object;
    } else {
      return false;
    }

    return this.x == point.getX() && this.y == point.getY();
  }
}
