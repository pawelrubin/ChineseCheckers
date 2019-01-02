package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameTest {

  private ServerSocket listener;
  private Socket socket1, socket2;
  private Game game;
  private final int port = 2137;

//  @Before
//  public void setUp() {
//    new Thread(() -> {
//      try {
//        listener = new ServerSocket(port);
//        game = new Game(2, 2, listener);
//        socket1 = new Socket("localhost", port);
//        socket2 = new Socket("localhost", port);
//      } catch (Exception ignored) {}
//    }).start();
//  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentException() throws IOException {
    listener = new ServerSocket(port);
    game = new Game(1, 0, listener);
  }

  @After
  public void cleanUp() throws IOException {
    if (listener != null)
      listener.close();
    if (socket1 != null)
      socket1.close();
    if (socket2 != null)
      socket2.close();
  }

}