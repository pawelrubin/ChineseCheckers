package layout;

import javafx.scene.Group;

abstract class BoardAbstract extends Group {
  //height of the board
  int height;
  //offset for storing the board
  int offset[];
  //widths of particular rows
  int widths[];
  //array that holds fields
  Field fields[][];
  Pawn pawns[][];

  public int getHeight() {
    return height;
  }

  public int getOffset(int i) {
    return offset[i];
  }

  public int getWidth(int i) {
    return widths[i];
  }

  public Field getField(int x, int y) {
    return fields[x][y];
  }

  public Pawn getPawn(int x, int y) {
    return pawns[x][y];
  }
}
