package server;

class Pawn {
  private int x;
  private int y;
  private String color;

  Pawn (int x, int y, String color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }

  int getX() {
    return x;
  }

  void setX(int x) {
    this.x = x;
  }

  int getY() {
    return y;
  }

  void setY(int y) {
    this.y = y;
  }

  String getColor() {
    return color;
  }
}
