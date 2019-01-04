package server;

import org.junit.Test;

import java.io.IOException;

public class KitajceServerTest {
  private KitajceServer server;

  @Test
  public void serverRuns() throws IOException {
    server = new KitajceServer(0, 2);
    server.start();
  }

  @Test (expected = IllegalArgumentException.class)
  public void shouldThrowException() {
    server = new KitajceServer(0, 1);
  }
}