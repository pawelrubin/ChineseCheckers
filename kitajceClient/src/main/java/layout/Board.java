package layout;

import javafx.scene.Group;

public class Board extends BoardAbstract {
  //vertical size of board and size of arrays
  public int height = 17;
  //public int offset[] = {6,5,5,4,0,0,1,1,2,1,1,0,0,4,5,5,6};
  //offset for drawing the board
  public int offsetDraw[] = {7,6,6,5,0,0,2,2,3,2,2,0,0,5,6,6,7};
  //offset for storing the board
  public int offset[] = {4,4,4,4,0,1,2,3,4,4,4,4,4,9,10,11,12};
  //widths of particular rows
  public int widths[] = {1,2,3,4,13,12,11,10,9,10,11,12,13,4,3,2,1};
}
