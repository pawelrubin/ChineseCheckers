package layout;

import javafx.scene.Group;

public class Board extends Group {
  private final int height = 17;
  private final static String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  //public int offset[] = {6,5,5,4,0,0,1,1,2,1,1,0,0,4,5,5,6};
  public int offsetDraw[] = {7, 6, 6, 5, 0, 0, 2, 2, 3, 2, 2, 0, 0, 5, 6, 6, 7};
  //offset for storing the board
  private int offset[] = {4, 4, 4, 4, 0, 1, 2, 3, 4, 4, 4, 4, 4, 9, 10, 11, 12};
  public int widths[] = {1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1};
  private final int numOfPlayers;
  private final Field fields[][];
  private Pawn pawns[][];

  public Board(int numOfPlayers) {
    super();
    this.numOfPlayers = numOfPlayers;
    fields = new Field[height][height];
    pawns = new Pawn[height][height];

    //nulling fields[] array
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < height; j++) {
        fields[i][j] = null;
        pawns[i][j] = null;
      }
    }

    // creating fields
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        fields[i][j + offset[i]] = new Field(i, j + offset[i]);
      }
    }

    //creating pawns
    switch (numOfPlayers) {
      case 2: {
        addGreenPawns();
        addYellowPawns();
        break;
      }
      case 3: {
        addGreenPawns();
        addBlackPawns();
        addRedPawns();
        break;
      }
      case 4: {
        addBluePawns();
        addWhitePawns();
        addBlackPawns();
        addRedPawns();
        break;
      }
      case 6: {
        addBluePawns();
        addWhitePawns();
        addBlackPawns();
        addRedPawns();
        addGreenPawns();
        addYellowPawns();
        break;
      }
    }
  }

  private void addGreenPawns() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[0]);
      }
    }
  }

  private void addYellowPawns() {
    for (int i = 13; i < 17; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[3]);
      }
    }
  }

  private void addBluePawns() {
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[5]);
      }
    }
  }

  private void addWhitePawns() {
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[1]);
      }
    }
  }

  private void addBlackPawns() {
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < widths[i - 9]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[4]);
      }
    }
  }

  private void addRedPawns() {
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[2]);
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

  public Pawn getPawn(int x, int y) {
    return pawns[x][y];
  }

  public void movePawn(int oldX, int oldY, int newX, int newY) {
    pawns[newX][newY] = pawns[oldX][oldY];
    pawns[oldX][oldY] = null;
  }
}
