package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class GameTest {

  private Game game;

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentException() throws IOException {
    game = new Game(1, 0, new ServerSocket(2137));
  }

}