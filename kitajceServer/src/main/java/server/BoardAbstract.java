package server;

import server.Field;

public abstract class BoardAbstract {
  //height of the board
  private int height;
  //offset for drawing the board
  private int offsetDraw[];
  //offset for storing the board
  private int offset[];
  //widths of particular rows
  private int widths[];
  //array that holds fields
  private Field fields[][];

  public int getHeight() {
    return height;
  }

  public int getOffsetDraw(int i) {
    return offsetDraw[i];
  }

  public int getOffset(int i) {
    return offset[i];
  }

  public int getWidths(int i) {
    return widths[i];
  }

  public Field getField(int x, int y) {
    return fields[x][y];
  }
}
