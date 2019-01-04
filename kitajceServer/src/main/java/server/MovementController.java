package server;

import java.util.ArrayList;
import java.util.List;

class MovementController {
  private Board board;
  private List<Point> nodes = new ArrayList<>();
  String winner;

  MovementController(Board board) {
    this.board = board;
  }

  boolean isValid(int oldX, int oldY, Field field, String color) {

    nodes.clear();

    switch (color) {
      case "GREEN": {
        if (board.getBottomCorner().contains(new Point(oldX, oldY))) {
          if (!board.getBottomCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
      case "YELLOW": {
        if (board.getTopCorner().contains(new Point(oldX, oldY))) {
          if (!board.getTopCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
      case "RED": {
        if (board.getTopLeftCorner().contains(new Point(oldX, oldY))) {
          if (!board.getTopLeftCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
      case "WHITE": {
        if (board.getBottomLeftCorner().contains(new Point(oldX, oldY))) {
          if (!board.getBottomLeftCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
      case "BLUE": {
        if (board.getBottomRightCorner().contains(new Point(oldX, oldY))) {
          if (!board.getBottomRightCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
      case "BLACK": {
        if (board.getTopRightCorner().contains(new Point(oldX, oldY))) {
          if (!board.getTopRightCorner().contains(new Point(field.getX(), field.getY()))) {
            return false;
          }
        }
        break;
      }
    }

    if (moveValidation(oldX, oldY, field)) {
      return true;
    }

    return jumpRecursiveValidation(oldX, oldY, 0, 0, oldX, oldY, field);

  }

  boolean gameOver() {
    if (greenWinningCondition()) {
      winner = "GREEN";
      return true;
    }
    if (yellowWinningCondition()) {
      winner = "YELLOW";
      return true;
    }
    if (blackWinningCondition()) {
      winner = "BLACK";
      return true;
    }
    if (whiteWinningCondition()) {
      winner = "WHITE";
      return true;
    }
    if (redWinningCondition()) {
      winner = "RED";
      return true;
    }
    if (blueWinningCondition()) {
      winner = "BLUE";
      return true;
    }
    return false;
  }

  private boolean moveValidation(int oldX, int oldY, Field field) {
    int newX = field.getX();
    int newY = field.getY();

    //moving right [1, 0] and left[-1, 0]
    if (newY - oldY == 0) {
      if (newX - oldX == 1) {
        return true;
      }
      if (newX - oldX == -1) {
        return true;
      }
    }

    //moving top right [0, -1] and top left [-1, -1]
    if (newY - oldY == -1) {
      if (newX - oldX == 0) {
        return true;
      }
      if (newX - oldX == -1) {
        return true;
      }
    }

    //moving bottom right [1, 1] or bottom left [0, 1]
    if (newY - oldY == 1) {
      if (newX - oldX == 1) {
        return true;
      }
      if (newX - oldX == 0) {
        return true;
      }
    }

    return false;
  }

  private boolean jumpRecursiveValidation(int oldX, int oldY, int offsetX, int offsetY, int originalX, int originalY, Field field) {
    int newX = field.getX();
    int newY = field.getY();

    if (oldX == newX && oldY == newY) {
      return true;
    }

    for (Point i : nodes) {
      for (Point j : nodes) {
        if (i != j) {
          if (i.equals(j)) {
            nodes.remove(nodes.size() - 1);
            return false;
          }
        }
      }
    }

    if (oldX == originalX && oldY == originalY && (offsetX != 0 || offsetY != 0)) {
      return false;
    }

    //jumping right [2, 0]
    if ((offsetX != -2 || offsetY != 0)
            && oldX <= board.getHeight() - 3
            && board.getField(oldX + 2, oldY) != null
            && board.getPawn(oldX + 2, oldY) == null
            && board.getPawn(oldX + 1, oldY) != null) {
      nodes.add(new Point(oldX + 2, oldY));
      if (jumpRecursiveValidation(oldX + 2, oldY, 2, 0, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping left [-2, 0]
    if ((offsetX != 2 || offsetY != 0)
            && oldX >= 2
            && board.getField(oldX - 2, oldY) != null
            && board.getPawn(oldX - 2, oldY) == null
            && board.getPawn(oldX - 1, oldY) != null) {
      nodes.add(new Point(oldX - 2, oldY));
      if (jumpRecursiveValidation(oldX - 2, oldY, -2, 0, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping top right [0, -2]
    if ((offsetX != 0 || offsetY != 2)
            && oldY >= 2
            && board.getField(oldX, oldY - 2) != null
            && board.getPawn(oldX, oldY - 2) == null
            && board.getPawn(oldX, oldY - 1) != null) {
      nodes.add(new Point(oldX, oldY - 2));
      if (jumpRecursiveValidation(oldX, oldY - 2, 0, -2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping top left [-2, -2]
    if ((offsetX != 2 || offsetY != 2)
            && oldX >= 2 && oldY >= 2
            && board.getField(oldX - 2, oldY - 2) != null
            && board.getPawn(oldX - 2, oldY - 2) == null
            && board.getPawn(oldX - 1, oldY - 1) != null) {
      nodes.add(new Point(oldX - 2, oldY - 2));
      if (jumpRecursiveValidation(oldX - 2, oldY - 2, -2, -2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping bottom right [2, 2]
    if ((offsetX != -2 || offsetY != -2)
            && oldX <= board.getHeight() - 3 && oldY <= board.getHeight() - 3
            && board.getField(oldX + 2, oldY + 2) != null
            && board.getPawn(oldX + 2, oldY + 2) == null
            && board.getPawn(oldX + 1, oldY + 1) != null) {
      nodes.add(new Point(oldX + 2, oldY + 2));
      if (jumpRecursiveValidation(oldX + 2, oldY + 2, 2, 2, originalX, originalY, field)) {
        return true;
      }
    }

    //jumping bottom left [0, 2]
    if ((offsetX != 0 || offsetY != -2)
            && oldY <= board.getHeight() - 3
            && board.getField(oldX, oldY + 2) != null
            && board.getPawn(oldX, oldY + 2) == null
            && board.getPawn(oldX, oldY + 1) != null) {
      nodes.add(new Point(oldX, oldY + 2));
      if (jumpRecursiveValidation(oldX, oldY + 2, 0, 2, originalX, originalY, field)) {
        return true;
      }
    }

    if (nodes.size() > 0) {
      nodes.remove(nodes.size() - 1);
    }

    return false;
  }

  public void tie(String color) {
    System.out.println("It's a tie for " + color + " player!");
  }

  private boolean greenWinningCondition() {
    int counter = 0;
    for (int i = 13; i < board.getHeight(); i++) {
      for (int j = 0; j < board.getWidth(i); j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("GREEN")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(16, 12) != null && !board.getPawn(16, 12).getColor().equals("GREEN")) {
            innerCounter++;
          }
          if (board.getPawn(15, 12) != null && !board.getPawn(15, 12).getColor().equals("GREEN")) {
            innerCounter++;
          }
          if (board.getPawn(15, 11) != null && !board.getPawn(15, 11).getColor().equals("GREEN")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("GREEN");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(16, 12) != null && !board.getPawn(16, 12).getColor().equals("GREEN")) {
          tie("GREEN");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean whiteWinningCondition() {
    int counter = 0;
    for (int i = 9; i < 13; i++) {
      for (int j = 0; j < board.getWidth(i - 9); j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("WHITE")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(12, 4) != null && !board.getPawn(12, 4).getColor().equals("WHITE")) {
            innerCounter++;
          }
          if (board.getPawn(11, 4) != null && !board.getPawn(11, 4).getColor().equals("WHITE")) {
            innerCounter++;
          }
          if (board.getPawn(12, 5) != null && !board.getPawn(12, 5).getColor().equals("WHITE")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("WHITE");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(12, 4) != null && !board.getPawn(12, 4).getColor().equals("WHITE")) {
          tie("WHITE");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean redWinningCondition() {
    int counter = 0;
    for (int i = 4; i < 8; i++) {
      for (int j = 0; j < 8 - i; j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("RED")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(4, 0) != null && !board.getPawn(4, 0).getColor().equals("RED")) {
            innerCounter++;
          }
          if (board.getPawn(4, 1) != null && !board.getPawn(4, 1).getColor().equals("RED")) {
            innerCounter++;
          }
          if (board.getPawn(5, 1) != null && !board.getPawn(5, 1).getColor().equals("RED")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("RED");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(4, 0) != null && !board.getPawn(4, 0).getColor().equals("RED")) {
          tie("RED");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean yellowWinningCondition() {
    int counter = 0;
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < board.getWidth(i); j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("YELLOW")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(0, 4) != null && !board.getPawn(0, 4).getColor().equals("YELLOW")) {
            innerCounter++;
          }
          if (board.getPawn(1, 4) != null && !board.getPawn(1, 4).getColor().equals("YELLOW")) {
            innerCounter++;
          }
          if (board.getPawn(1, 5) != null && !board.getPawn(1, 5).getColor().equals("YELLOW")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("YELLOW");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(0, 4) != null && !board.getPawn(0, 4).getColor().equals("YELLOW")) {
          tie("YELLOW");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean blackWinningCondition() {
    int counter = 0;
    for (int i = 4; i < 8; i++) {
      for (int j = 9; j <= 16 - i; j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("BLACK")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(4, 12) != null && !board.getPawn(4, 12).getColor().equals("BLACK")) {
            innerCounter++;
          }
          if (board.getPawn(4, 11) != null && !board.getPawn(4, 11).getColor().equals("BLACK")) {
            innerCounter++;
          }
          if (board.getPawn(5, 12) != null && !board.getPawn(5, 12).getColor().equals("BLACK")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("BLACK");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(4, 12) != null && !board.getPawn(4, 12).getColor().equals("BLACK")) {
          tie("BLACK");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean blueWinningCondition() {
    int counter = 0;
    for (int i = 9; i < 13; i++) {
      for (int j = 9; j < board.getWidth(i); j++) {
        if (board.getPawn(i, j + board.getOffset(i)) != null && board.getPawn(i, j + board.getOffset(i)).getColor().equals("BLUE")) {
          counter++;
        }
        if (counter == 7 || counter == 8) {
          int innerCounter = 0;
          if (board.getPawn(12, 16) != null && !board.getPawn(12, 16).getColor().equals("BLUE")) {
            innerCounter++;
          }
          if (board.getPawn(12, 15) != null && !board.getPawn(12, 15).getColor().equals("BLUE")) {
            innerCounter++;
          }
          if (board.getPawn(11, 15) != null && !board.getPawn(11, 15).getColor().equals("BLUE")) {
            innerCounter++;
          }
          if ((counter == 7 && innerCounter == 3) || (counter == 8 && innerCounter == 2)) {
            tie("BLUE");
            return false;
          }
        }
        if (counter == 9 && board.getPawn(12, 16) != null && !board.getPawn(12, 16).getColor().equals("BLUE")) {
          tie("BLUE");
          return false;
        }
        if (counter == 10) {
          return true;
        }
      }
    }
    return false;
  }
}
