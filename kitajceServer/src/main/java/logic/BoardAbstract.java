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
}
