package server;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameTest {

  private Socket socket1, socket2;
  private Game game;

  @Before
  public void setUp() throws IOException {
    new Thread(() -> {
      try {
        game = new Game(2, 2, new ServerSocket(2137));
        socket1 = new Socket("localhost", 2137);
        socket2 = new Socket("localhost", 2137);
      } catch (Exception ignored) {}
    }).start();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentException() throws IOException {
    Game game = new Game(1, 0, new ServerSocket(2137));
  }

}