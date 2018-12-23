import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class KitajceServer {

  private static final int port = 2137;
  private int numOfPlayers;

  public static void main(String[] args) throws Exception {
    KitajceServer server = new KitajceServer();
    server.numOfPlayers = Integer.parseInt(args[0]);
    if (server.numOfPlayers < 2 || server.numOfPlayers > 6 || server.numOfPlayers == 5 ) {
      System.exit(1);
    }
    server.start();
  }

  private void start() throws IOException {
    try (ServerSocket listener = new ServerSocket(port)) {
      System.out.println("Kitajce Server is running.");
      new Game(numOfPlayers, listener);
    }
  }
}