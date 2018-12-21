import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class KitajceServer {

  private static final int port = 2137;
  private static final String[] colors = {
          "Green", "Red", "Blue",
          "Yellow", "Orange", "Purple"
  };
  private ServerSocket listener;
  private int numOfPlayers = 2;
  private int numOfPlayersConnected = 0;
  private Game.Player players[];
  private Game game;

  public static void main(String[] args) throws Exception {
    KitajceServer server = new KitajceServer();
    server.start();
  }

  private void settings() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Number of players: ");
      numOfPlayers = scanner.nextInt();
      System.out.println(numOfPlayers);
      if ((numOfPlayers >= 2 && numOfPlayers <= 6 && numOfPlayers != 5)) {
        break;
      }
    }
  }

  private void setPlayers() throws IOException {
    players = new Game.Player[numOfPlayers];
    for (int i = 0; i < numOfPlayers; i++) {
      System.out.println("waiting for " + i + "player");
      players[i] = game.new Player(colors[i], listener.accept());
      numOfPlayersConnected++;
    }
    for (int i = 0; i < numOfPlayers; i++) {
      players[i].start();
    }
    System.out.println("All players connected.");
  }

  private void start() throws IOException {
    listener = new ServerSocket(port);
    System.out.println("Kitajce Server is running.");
//    settings();
    try {
//      while (true) {
        game = new Game(numOfPlayers, listener);
//      }
    } finally {
      listener.close();
    }
  }
}