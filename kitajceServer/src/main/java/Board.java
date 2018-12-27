import java.util.ArrayList;
import java.util.List;

class Board {
  private int numOfPlayers;
  private Field fields[][];
  private Pawn pawns[][];
  private final int height = 17;
  private static String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  public int offsetDraw[] = {7, 6, 6, 5, 0, 0, 2, 2, 3, 2, 2, 0, 0, 5, 6, 6, 7};
  private int offset[] = {4, 4, 4, 4, 0, 1, 2, 3, 4, 4, 4, 4, 4, 9, 10, 11, 12};
  private int widths[] = {1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1};
  private List<Point> topCorner;
  private List<Point> topRightCorner;
  private List<Point> topLeftCorner;
  private List<Point> bottomRightCorner;
  private List<Point> bottomLeftCorner;
  private List<Point> bottomCorner;
  public static final int numOfPawns = 10;

  Board(int numOfPlayers) {
    super();
    this.numOfPlayers = numOfPlayers;
    fields = new Field[height][height];
    pawns = new Pawn[height][height];
    topCorner = new ArrayList<>();
    topRightCorner = new ArrayList<>();
    topLeftCorner = new ArrayList<>();
    bottomRightCorner = new ArrayList<>();
    bottomLeftCorner = new ArrayList<>();
    bottomCorner = new ArrayList<>();

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
        topCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addYellowPawns() {
    for (int i = 13; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[3]);
        bottomCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addBluePawns() {
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[5]);
        topLeftCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addWhitePawns() {
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[1]);
        topRightCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addBlackPawns() {
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < widths[i - 9]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[4]);
        bottomLeftCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addRedPawns() {
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[2]);
        bottomRightCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  Field getField(int x, int y) {
    return fields[x][y];
  }

  Pawn getPawn(int x, int y) {
    return pawns[x][y];
  }

  int getOffset(int i) {
    return offset[i];
  }

  List<Point> getTopCorner() {
    return topCorner;
  }

  List<Point> getTopRightCorner() {
    return topRightCorner;
  }

  List<Point> getTopLeftCorner() {
    return topLeftCorner;
  }

  List<Point> getBottomRightCorner() {
    return bottomRightCorner;
  }

  List<Point> getBottomLeftCorner() {
    return bottomLeftCorner;
  }

  List<Point> getBottomCorner() {
    return bottomCorner;
  }

  void movePawn(int oldX, int oldY, int newX, int newY) {
    pawns[newX][newY] = pawns[oldX][oldY];
    pawns[oldX][oldY] = null;
//    this.getPawn(newX, newY).setX(newX);
//    this.getPawn(newX, newY).setY(newY);
  }

  int getHeight() {
    return height;
  }

  int getWidth(int i) {
    return widths[i];
  }
  
  Field getDestination(String color) throws IllegalArgumentException {
    switch (color) {
      case "GREEN": {
        return getField(16, 12);
      }
      case "WHITE": {
        return getField(12, 4);
      }
      case "RED": {
        return getField(4, 0);
      }
      case "YELLOW": {
        return getField(0, 4);
      }
      case "BLACK": {
        return getField(4, 12);
      }
      case "BLUE": {
        return getField(12, 16);
      }
      default: {
        throw new IllegalArgumentException("Wrong color");
      }
    }
  }

  List<Pawn> getPawnsByColor(String color) {
    List<Pawn> pawnsByColor = new ArrayList<>();

    for(Pawn[] row: pawns) {
      for (Pawn pawn: row) {
        if (pawn.getColor().equals(color)) {
          pawnsByColor.add(pawn);
        }
      }
    }

    return pawnsByColor;
  }
}
