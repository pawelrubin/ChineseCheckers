package layout;

class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  private int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  private int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean equals(Point point) {
    return this.x == point.getX() && this.y == point.getY();
  }
}
