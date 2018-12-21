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
    public BufferedReader input;
    public PrintWriter output;
    private Player opponent;
    private Pawn pawns[] = new Pawn[10];
    public Protocol protocol;

    Player(String color, Socket socket) {
      this.color = color;
      this.socket = socket;

      setPawns(color);

      try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // PrintWriter with automatic line flushing
        output = new PrintWriter(socket.getOutputStream(), true);

        protocol = new Protocol(this);

        output.println("");
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

    public Player getPlayer(int i) {
      return players[i];
    }

    @Override
    public void run() {
      try {
        System.out.println("All players connected");
        output.println("MESSAGE All players connected.");
        while (true) {
          String command = input.readLine();
          if (command != null) {
            System.out.println("Command from a client: " + command);
            if (command.startsWith("MOVE")) { // MOVE GREEN 0 4 5 4
              String words[] = command.split(" ");
              int pawnX = Integer.parseInt(words[2]);
              int pawnY = Integer.parseInt(words[3]);
              int fieldX = Integer.parseInt(words[4]);
              int fieldY = Integer.parseInt(words[5]);
              Pawn pawn = board.getPawn(pawnX, pawnY);
              Field field = board.getField(fieldX, fieldY);
              if (controller.isValid(pawnX, pawnY, field)) {
                board.movePawn(pawnX, pawnY, fieldX, fieldY);
                this.protocol.validMoveMessage();
                for (Player player : players) {
                  if (player != this) {
                    player.protocol.playerMoved(pawn, field);
                  }
                }
              } else {
                this.protocol.invalidMoveMessage();
              }
            } else if (command.startsWith("QUIT")) {
              return;
            }
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
