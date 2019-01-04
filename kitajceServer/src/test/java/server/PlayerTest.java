package server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

  private Player player;

  @Before
  public void setUp() throws Exception {
    player = new Player() {

      @Override
      public void run() {
        while(alive);
      }
    };
    player.start();
  }

  @Test
  public void kill() throws InterruptedException {
    player.kill();
    player.join();
    assertFalse(player.isAlive());
  }
}