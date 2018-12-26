import java.io.IOException;
import java.net.ServerSocket;

public class KitajceServer {

  private static final int port = 2137;
  private int numOfPlayers;

  public static void main(String[] args) {
    KitajceServer server = new KitajceServer();
    if (args.length == 0) {
      System.out.println("Too few arguments.");
      System.exit(1);
    }
    try {
      server.numOfPlayers = Integer.parseInt(args[0]);
      if (server.numOfPlayers < 2 || server.numOfPlayers > 6 || server.numOfPlayers == 5) {
        throw new IllegalArgumentException("Illegal number of players.");
      }
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException ex) {
      System.out.println(ex.getMessage());
      System.exit(1);
    }
  }

  private void start() throws IOException {
    try (ServerSocket listener = new ServerSocket(port)) {
      System.out.println("Kitajce Server is running.");
      new Game(numOfPlayers, listener);
    }
  }
}