package layout;

import javafx.scene.Group;

public class Board extends Group {
  public int height = 17;
  public int width = 13;
  //public int offset[] = {6,5,5,4,0,0,1,1,2,1,1,0,0,4,5,5,6};
  public int offsetDraw[] = {7,6,6,5,0,0,2,2,3,2,2,0,0,5,6,6,7};
  //offset for storing the board
  public int offset[] = {4,4,4,4,0,1,2,3,4,4,4,4,4,9,10,11,12};
  public int widths[] = {1,2,3,4,13,12,11,10,9,10,11,12,13,4,3,2,1};
}
