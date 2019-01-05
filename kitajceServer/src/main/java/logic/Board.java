package logic;

import java.util.ArrayList;
import java.util.List;

public class Board extends BoardAbstract {
  public static final int numOfPawns = 10;
  private static final String[] colors = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  private List<Point> topCorner;
  private List<Point> topRightCorner;
  private List<Point> topLeftCorner;
  private List<Point> bottomRightCorner;
  private List<Point> bottomLeftCorner;
  private List<Point> bottomCorner;

  public Board(int numOfPlayers) {
    if (numOfPlayers < 2 || numOfPlayers > 6 || numOfPlayers == 5) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    setOffsets();
    setHeight();
    setWidths();
    addFields();
    addPawns(numOfPlayers);
  }

  private void setOffsets() {
    offset = new int[]{4, 4, 4, 4, 0, 1, 2, 3, 4, 4, 4, 4, 4, 9, 10, 11, 12};
  }

  private void setWidths() {
    widths = new int[]{1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1};
  }

  private void setHeight() {
    height = 17;
  }

  private void addFields() {
    fields = new Field[height][height];
    // creating fields
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        fields[i][j + offset[i]] = new Field(i, j + offset[i]);
      }
    }
  }

  private void addPawns(int numOfPlayers) {
    pawns = new Pawn[height][height];
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
        setBottomCorner();
        setTopLeftCorner();
        setTopRightCorner();
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
    topCorner = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[0]);
        topCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addYellowPawns() {
    bottomCorner = new ArrayList<>();
    for (int i = 13; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[3]);
        bottomCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addBluePawns() {
    topLeftCorner = new ArrayList<>();
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[5]);
        topLeftCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addWhitePawns() {
    topRightCorner = new ArrayList<>();
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[1]);
        topRightCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addBlackPawns() {
    bottomLeftCorner = new ArrayList<>();
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < widths[i - 9]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[4]);
        bottomLeftCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void addRedPawns() {
    bottomRightCorner = new ArrayList<>();
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < widths[i]; j++) {
        pawns[i][j + offset[i]] = new Pawn(i, j + offset[i], colors[2]);
        bottomRightCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

// --Commented out by Inspection START (06/01/2019 00:16):
//  private void setTopCorner() {
//    topCorner = new ArrayList<>();
//    for (int i = 0; i < 4; i++) {
//      for (int j = 0; j < widths[i]; j++) {
//        topCorner.add(new Point(i, j + offset[i]));
//      }
//    }
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

// --Commented out by Inspection START (06/01/2019 00:16):
//  private void setBottomLeftCorner() {
//    bottomLeftCorner = new ArrayList<>();
//    for (int i = 9; i < 13; i++) {
//      for (int j = 0; j < widths[i - 9]; j++) {
//        bottomLeftCorner.add(new Point(i, j + offset[i]));
//      }
//    }
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

// --Commented out by Inspection START (06/01/2019 00:16):
//  private void setBottomRightCorner() {
//    bottomRightCorner = new ArrayList<>();
//    for (int i = 9; i < 13; i++) {
//      for (int j = 9; j < widths[i]; j++) {
//        bottomRightCorner.add(new Point(i, j + offset[i]));
//      }
//    }
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

  private void setTopRightCorner() {
    topRightCorner = new ArrayList<>();
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        topRightCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void setTopLeftCorner() {
    topLeftCorner = new ArrayList<>();
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        topLeftCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  private void setBottomCorner() {
    bottomCorner = new ArrayList<>();
    for (int i = 13; i < height; i++) {
      for (int j = 0; j < widths[i]; j++) {
        bottomCorner.add(new Point(i, j + offset[i]));
      }
    }
  }

  public List<Point> getTopCorner() {
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

  public void movePawn(int oldX, int oldY, int newX, int newY) {
    pawns[newX][newY] = pawns[oldX][oldY];
    pawns[oldX][oldY] = null;
  }

  public Field getDestination(String color) throws IllegalArgumentException {
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

  public Field getTarget(String color) throws IllegalArgumentException {
    double bestDistance = Double.MAX_VALUE;
    Field target = null;
    for (Point point: this.getTargetCorner(color)) {
      if (this.getPawn(point.getX(), point.getY()) == null) {
        double tempDistance = distance(new Field(point.getX(), point.getY()), getDestination(color));
        if (tempDistance < bestDistance) {
          bestDistance = tempDistance;
          target = this.getField(point.getX(), point.getY());
        }
      }
    }
    if (target == null) {
      return getDestination(color); // improvised fix, better than null which would be returned for some reason
    } else {
      return target;
    }
  }

  public List<Pawn> getPawnsByColor(String color) {
    List<Pawn> pawnsByColor = new ArrayList<>();

    for(Pawn[] row: pawns) {
      for (Pawn pawn: row) {
        if (pawn != null) {
          if (pawn.getColor().equals(color)) {
            pawnsByColor.add(pawn);
          }
        }
      }
    }

    return pawnsByColor;
  }

  public Field[][] getFields() {
    return fields;
  }

  public List<Point> getTargetCorner(String color) {
    switch (color) {
      case "GREEN": {
        return getBottomCorner();
      }
      case "WHITE": {
        return getBottomLeftCorner();
      }
      case "BLUE": {
        return getTopLeftCorner();
      }
      case "YELLOW": {
        return getTopCorner();
      }
      case "BLACK": {
        return getTopRightCorner();
      }
      case "RED": {
        return getTopLeftCorner();
      }
    }
    return null;
  }
}
