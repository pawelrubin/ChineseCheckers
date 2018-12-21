import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {
  private final int numOfPlayers;
  private final Board board;
  private Player currentPlayer;
  private MovementController controller;
  private String colors[] = {"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
  private int moveCount = 0;
  private Player players[];
  private ServerSocket listener;

  public Game(int numOfPlayers, ServerSocket listener) throws IOException {
    this.numOfPlayers = numOfPlayers;
    this.listener = listener;
    board = new Board(numOfPlayers);
    controller = new MovementController(board);
    addPlayers();
    runPlayers();
  }

  // Adds players to the game.
  public void addPlayers() throws IOException {
    players = new Player[numOfPlayers];
    for (int i = 0; i < numOfPlayers; i++) {
      System.out.println("adding player number " + String.valueOf(i+1));
      players[i] = new Player(colors[i], listener.accept());
    }
  }

  // Runs players threads.
  public void runPlayers() {
    for (Player player: players) {
      player.start();
    }
  }

  public synchronized boolean legalMove(Pawn pawn, Field field) {
    if (controller.isValid(pawn.getX(), pawn.getY(), field)) {
//      currentPlayer = nextPlayer();
    }
    return true;
  }

//  private Player nextPlayer() {
//    moveCount++;
//    return colors[moveCount % 6];
//  }

  public class Player extends Thread {
    private String color;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Player opponent;
    private Pawn pawns[] = new Pawn[10];

    Player(String color, Socket socket) {
      this.color = color;
      this.socket = socket;

      setPawns(color);

      try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // PrintWriter with automatic line flushing
        output = new PrintWriter(socket.getOutputStream(), true);

        output.println("NO KURWA SIEMA");
        output.println("Welcome my friend whose color is " + this.color);
        output.println("Waiting for your opponents to connect...");
      } catch (IOException ex) {
        System.out.println("Game.Player error: " + ex);
      }


    }

    void setOpponent(Player opponent) {
      this.opponent = opponent;
    }

    void setPawns(String color) {
      for (int i = 0; i < pawns.length; i++) {
        // setting pawns depending on color
      }
    }

    void handleCommands(String command) {
      String word = command.split(" ")[0];
      switch (word) {
        case "MOVE": {
          int poneX = command.charAt(6);
          int poneY = command.charAt(8);
          int targetX = command.charAt(10);
          int targetY = command.charAt(11);
  //        if (MovementController.isValid(poneX, poneY, targetX, targetY)) {
  //          output.println("VALID_MOVE");
  //        }
          break;
        }
      }
    }

    @Override
    public void run() {
      try {
        output.println("MESSAGE All players connected.");
        Protocol protocol = new Protocol();
        while (true) {
          String command = input.readLine();
          if (command.startsWith("MOVE")) { // MOVE GREEN 0 4 5 4
            String words[] = command.split(" ");
            System.out.println("command: " + command);
            Pawn pawn = new Pawn(Integer.parseInt(words[2]), Integer.parseInt(words[3]), words[1]);
            Field field = new Field(Integer.parseInt(words[4]), Integer.parseInt(words[5]));
            System.out.println(pawn.toString());
            System.out.println(field.toString());
            protocol.playerMoved(pawn, field);
          }
          else if (command.startsWith("QUIT")) {
            return;
          }
        }
      } catch (IOException ex) {
        System.out.println("Game.Player error: " + ex);
      } finally {
        try {
          socket.close();
        } catch (IOException ignored) {
        }
      }
    }
  }
}
