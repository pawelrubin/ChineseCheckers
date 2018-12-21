import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Protocol {
  private BufferedReader input;
  private PrintWriter output;
  MovementController controller;
  Board board;

  Protocol(Game.Player player) throws IOException {
    input = player.input;
    output = player.output;
    System.out.println("New protocol created.");
  }
  /**
   * Sends "VALID_MOVE" message to a client.
   */
  public void validMoveMessage() {
    output.println("VALID_MOVE");
  }


  public void playerMoved(Pawn pawn, Field field) {
    output.println("PLAYER_MOVED " + pawn.getX() + " " + pawn.getY() + " " + field.getX() + " " + field.getY());
  }

  public void newTurn(String color) {
    output.println("NEW_TURN " + color);
  }


  public void invalidMoveMessage() {
    output.println("INVALID_MOVE");
  }
}
