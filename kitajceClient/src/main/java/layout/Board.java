package layout;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Board extends Group {
  private final int height = 17;
  private static String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  //public int offset[] = {6,5,5,4,0,0,1,1,2,1,1,0,0,4,5,5,6};
  public int offsetDraw[] = {7, 6, 6, 5, 0, 0, 2, 2, 3, 2, 2, 0, 0, 5, 6, 6, 7};
  //offset for storing the board
  private int offset[] = {4, 4, 4, 4, 0, 1, 2, 3, 4, 4, 4, 4, 4, 9, 10, 11, 12};
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
        pones[i][j] = null;
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
        addBlackPones();
        addRedPones();
        break;
      }
      case 6: {
        addBluePones();
        addWhitePones();
        addBlackPones();
        addRedPones();
        addGreenPones();
        addYellowPones();
        break;
      }
    }
  }
//TODO make it work
  public void movePone(String color) {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < height; j++) {
        if (pones[i][j] != null) {
          final int a = i;
          final int b = j;
          pones[i][j].setOnMouseClicked(e -> {
            pones[a][b].setStroke(Color.PURPLE);
            pones[a][b].setStrokeWidth(5);
            setOnMousePressed(ee -> {
              for (int k = 0; k < height; k++) {
                for (int m = 0; m < height; m++) {
                  if (fields[k][m] != null && pones[k][m] == null && color.equals(pones[a][b].getColor())) {
                    double x = fields[k][m].getCenterX();
                    double y = fields[k][m].getCenterY();
                    //moving pone physically
                    pones[a][b].setCenterX(x);
                    pones[a][b].setCenterY(y);
                    //moving pone algorithmically
                    pones[k][m] = pones[a][b];
                    pones[a][b] = null;
                    pones[a][b].setStrokeWidth(0);
                  }
                }
              }
            });
          });
        }
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

  public Pone getPone(int x, int y) {
    return pones[x][y];
  }
}
