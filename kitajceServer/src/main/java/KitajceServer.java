import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class KitajceServer {

  private static final int port = 2137;
  private int numOfPlayers = 2;

  public static void main(String[] args) throws Exception {
    KitajceServer server = new KitajceServer();
    server.start();
  }

  private void settings() {
    Scanner scanner = new Scanner(System.in);
    do {
      System.out.println("Number of players: ");
      numOfPlayers = scanner.nextInt();
      System.out.println(numOfPlayers);
    } while ((numOfPlayers < 2 || numOfPlayers > 6 || numOfPlayers == 5));
  }

  private void start() throws IOException {
    try (ServerSocket listener = new ServerSocket(port)) {
      System.out.println("Kitajce Server is running.");
      settings();
      new Game(numOfPlayers, listener);
    }
  }
}