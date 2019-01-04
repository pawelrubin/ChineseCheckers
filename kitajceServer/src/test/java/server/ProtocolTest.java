package server;

import logic.Field;
import logic.Pawn;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ProtocolTest {
  private Protocol protocol;

  @Before
  public void setProtocol() throws IOException {
    Socket socket = new Socket("localhost", 2137);
    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
    protocol = new Protocol(output);
  }

  @Test
  public void shouldSendMessages() {
    protocol.allConnected();
    protocol.playerMoved(new Pawn(21, 37, "GREEN"), new Field(21, 38));
    protocol.next("GREEN");
    protocol.winnerMessage("GREEN");
    protocol.startGame();
    protocol.validMove(new Pawn(21, 37, "GREEN"), new Field(21, 38));
    protocol.invalidMoveMessage();
    protocol.tie("YELLOW");
  }
}