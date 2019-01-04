package server;

import logic.Field;
import logic.Pawn;

import java.io.PrintWriter;

class Protocol {
  private PrintWriter output;

  Protocol(PrintWriter output) {
    this.output = output;
    System.out.println("New protocol created.");
  }

  /**
   * Sends "MESSAGE All players connected to a client."
   */
  void allConnected() {
    output.println("MESSAGE All players connected.");
    System.out.println("allConnected has been send.");
  }

  /**
   * Sends "START_GAME" message to a client.
   */
  void startGame() {
    output.println("START_GAME");
    System.out.println("startGame has been send.");
  }


  /**
   * Sends "VALID_MOVE" message to a client.
   */
  void validMove(Pawn pawn, Field field) {
    output.println("VALID_MOVE " + pawn.getX() + " " + pawn.getY() + " "
      + field.getX() + " " + field.getY());
    System.out.println("validMove has been send.");
  }

  /**
   * Sends "PLAYER_MOVED pX pY fX fY" message to a client.
   * where pX, pY are pawn's coordinates
   * and fX, fY are field's coordinates.
   *
   * @param pawn A pawn that made a move.
   * @param field A destination field.
   */
  void playerMoved(Pawn pawn, Field field) {
    output.println("PLAYER_MOVED " + pawn.getX() + " " + pawn.getY() + " " + field.getX() + " " + field.getY());
    System.out.println("playerMoved has been send.");
  }

  /**
   * Sends "INVALID_MOVE" message to a client.
   */
  void invalidMoveMessage() {
    output.println("INVALID_MOVE");
    System.out.println("invalidMoveMessage has been send.");
  }

  /**
   * Sends "NEXT color" message to a client,
   * where color is passed String variable.
   *
   * @param color of the next player.
   */
  void next(String color) {
    output.println("NEXT " + color);
    System.out.println("next has been send.");
  }


  /**
   * Sends "TIE color" message to a client,
   * where color is passed String variable.
   *
   * @param color of "tied" player
   */
  void tie(String color) {
    output.println("TIE " + color);
  }

  /**
   * Sends "WINNER color",
   * where color is a color of the winner.
   *
   * @param winner is a color String of the winner.
   */
  void winnerMessage(String winner) {
    output.println("WINNER " + winner);
    System.out.println("winnerMessage has been send.");
  }
}
