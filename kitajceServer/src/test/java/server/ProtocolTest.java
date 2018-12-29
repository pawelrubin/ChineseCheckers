package server;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProtocolTest {
  private Protocol protocol;
  private Socket socket;
  private Game game;

  @Before
  public void setProtocol() {
    new Thread(() -> {
      try {
        game = new Game(1, 1, new ServerSocket(2137));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
    new Thread(() -> {
      try {
        socket = new Socket("localhost", 2137);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
    protocol = new Protocol(game.getHuman(0));
  }

  @Test
  public void shouldSendMessages() {

  }
}