package layout;

import javafx.scene.Group;

public abstract class BoardAbstract extends Group {
  //height of the board
  private int height;
  //offset for drawing the board
  private int offsetDraw[];
  //offset for storing the board
  private int offset[];
  //widths of particular rows
  private int widths[];
  //array that holds fields
  private int fields[][];

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

  public int getFields(int x, int y) {
    return fields[x][y];
  }
}
