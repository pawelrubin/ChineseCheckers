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

  boolean equals(Point point) {
    return this.x == point.getX() && this.y == point.getY();
  }
}