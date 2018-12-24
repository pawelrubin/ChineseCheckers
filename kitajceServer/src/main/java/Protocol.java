import java.io.PrintWriter;

class Protocol {
  private PrintWriter output;

  Protocol(Game.Player player) {
    output = player.output;
    System.out.println("New protocol created.");
  }

  /**
   * Sends "MESSAGE All players connected to a client."
   */
  void allConnected() {
    output.println("MESSAGE All players connected.");
  }

  /**
   * Sends "START_GAME" message to a client.
   */
  void startGame() {
    output.println("START_GAME");
  }

  /**
   * Sends "VALID_MOVE" message to a client.
   */
  void validMove(Pawn pawn, Field field) {
    output.println("VALID_MOVE " + pawn.getX() + " " + pawn.getY() + " "
      + field.getX() + " " + field.getY());
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
  }

  /**
   * Sends "INVALID_MOVE" message to a client.
   */
  void invalidMoveMessage() {
    output.println("INVALID_MOVE");
  }

  /**
   * Sends "NEXT color" message to a client,
   * where color is passed String variable.
   *
   * @param color of the next player.
   */
  void next(String color) {
    output.println("NEXT " + color);
  }

  /**
   * Sends "WINNER color",
   * where color is a color of the winner.
   *
   * @param winner is a color String of the winner.
   */
  void winnerMessage(String winner) {
    output.println("WINNER " + winner);
  }
}
