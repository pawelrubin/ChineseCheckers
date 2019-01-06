package logic;

abstract class BoardAbstract {
  //height of the board
  int height;
  //offset for storing the board
  int offset[];
  //widths of particular rows
  int widths[];
  //array that holds fields
  Field fields[][];
  Pawn pawns[][];

  int getHeight() {
    return height;
  }

  int getOffset(int i) {
    return offset[i];
  }

  int getWidth(int i) {
    return widths[i];
  }

  public Field getField(int x, int y) {
    return fields[x][y];
  }

  public Pawn getPawn(int x, int y) {
    return pawns[x][y];
  }

  /**
   * Calculates distance between two fields
   *
    * @param a First field
   * @param b Second field
   * @return Distance in double
   */
  public double distance(Field a, Field b) {
    return Math.sqrt(Math.pow(Math.abs(a.getX() - b.getX()), 2) +
            Math.pow(Math.abs(a.getY() - b.getY()), 2));
  }
}
