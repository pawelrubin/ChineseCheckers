package server;

import java.io.IOException;
import java.net.ServerSocket;

public class KitajceServer {

  private static final int port = 2137;
  private final int numOfHumans;
  private final int numOfBots;

  public KitajceServer(int numOfHumans, int numOfBots) {
    this.numOfHumans = numOfHumans;
    this.numOfBots = numOfBots;
    int numOfPlayers = numOfBots + numOfHumans;
    if (numOfPlayers < 2 || numOfPlayers > 6 || numOfPlayers == 5) {
      throw new IllegalArgumentException("Illegal number of players.");
    }
  }

  public void start() throws IOException {
    try (ServerSocket listener = new ServerSocket(port)) {
      System.out.println("Kitajce Server is running.");
      new Game(numOfHumans, numOfBots, listener);
    }
  }
}