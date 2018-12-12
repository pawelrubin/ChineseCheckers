package layout;

import javafx.scene.Group;

public class Board extends Group {
  private final int height = 17;
  private static String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  //public int offset[] = {6,5,5,4,0,0,1,1,2,1,1,0,0,4,5,5,6};
  public int offsetDraw[] = {7, 6, 6, 5, 0, 0, 2, 2, 3, 2, 2, 0, 0, 5, 6, 6, 7};
  //offset for storing the board
  public int offset[] = {4, 4, 4, 4, 0, 1, 2, 3, 4, 4, 4, 4, 4, 9, 10, 11, 12};
  public int widths[] = {1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1};
  private int numOfPlayers;
  private Field fields[][];
  private Pone pones[][];

  public Board(int numOfPlayers) {
    super();
    this.numOfPlayers = numOfPlayers;
    fields = new Field[height][height];
    pones = new Pone[height][height];

    //nulling fields[] array
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < height; j++) {
        fields[i][j] = null;
      }
    }

    // creating fields
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        fields[i][j + offset[i]] = new Field(i, j + offset[i]);
      }
    }

    //creating pones
    switch (numOfPlayers) {
      case 2: {
        addGreenPones();
        addYellowPones();
        break;
      }
      case 3: {
        addGreenPones();
        addBlackPones();
        addRedPones();
        break;
      }
      case 4: {
        addBluePones();
        addWhitePones();
        addBluePones();
        addRedPones();
        break;
      }
      case 6: {
        addBluePones();
        addWhitePones();
        addBluePones();
        addRedPones();
        addGreenPones();
        addYellowPones();
        break;
      }
    }
  }

  private void addGreenPones() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[0]);
      }
    }
  }

  private void addYellowPones() {
    for (int i = 13; i < 17; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[3]);
      }
    }
  }

  private void addBluePones() {
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[5]);
      }
    }
  }

  private void addWhitePones() {
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[1]);
      }
    }
  }

  private void addBlackPones() {
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < widths[i - 9]; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[4]);
      }
    }
  }

  private void addRedPones() {
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < widths[i]; j++) {
        pones[i][j + offset[i]] = new Pone(i, j + offset[i], colors[2]);
      }
    }
  }

  public int getHeight() {
    return height;
  }

  public int getOffset(int i) {
    return offset[i];
  }

  public Field getField(int x, int y) {
    return fields[x][y];
  }
}
