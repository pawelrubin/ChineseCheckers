import java.io.BufferedReader;
import java.io.PrintWriter;

public class Protocol {
  BufferedReader input;
  PrintWriter output;
  MovementController controller;
  Board board;

  /**
   * Sends "VALID_MOVE" message to a client.
   */
  public void validMoveMessage() {
    output.println("VALID_MOVE");
  }


  public void playerMoved(Pawn pawn, Field field) {
    output.println("PLAYER_MOVED " + pawn.getX() + " " + pawn.getY() + " " + field.getX() + " " + field.getY());
  }


}
