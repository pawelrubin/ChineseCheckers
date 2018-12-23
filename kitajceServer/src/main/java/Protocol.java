import java.io.BufferedReader;
import java.io.PrintWriter;

class Protocol {
  private BufferedReader input;
  private PrintWriter output;

  Protocol(Game.Player player) {
    input = player.input;
    output = player.output;
    System.out.println("New protocol created.");
  }

  /**
   * Sends "VALID_MOVE" message to a client.
   */
  void validMoveMessage(Pawn pawn, Field field) {
    System.out.println("VALID_MOVE " + pawn.getX() + " " + pawn.getY() + " "
            + field.getX() + " " + field.getY());
    output.println("VALID_MOVE " + pawn.getX() + " " + pawn.getY() + " "
      + field.getX() + " " + field.getY());
  }


  void playerMoved(Pawn pawn, Field field) {
    output.println("PLAYER_MOVED " + pawn.getX() + " " + pawn.getY() + " " + field.getX() + " " + field.getY());
  }

  void invalidMoveMessage() {
    output.println("INVALID_MOVE");
  }

  void next(String color) {
    output.println("NEXT " + color);
  }

  void winnerMessage(String winner) {
    output.println("WINNER " + winner);
  }
}
