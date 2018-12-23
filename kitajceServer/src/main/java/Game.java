import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Game {
  private final int numOfPlayers;
  private final Board board;
  private Player currentPlayer;
  private String currentColor;
  private MovementController controller;
  private int moveCount;
  private Player[] players;
  private ServerSocket listener;
  private String[] colors;

  Game(int numOfPlayers, ServerSocket listener) throws IOException {
    List<Integer> legalNumOfPLayers = Arrays.asList(2, 3, 4, 6);
    if (!legalNumOfPLayers.contains(numOfPlayers)) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    this.numOfPlayers = numOfPlayers;
    this.listener = listener;
    switch (numOfPlayers) {
      case 2: {
        colors = new String[]{"GREEN", "YELLOW"};
        break;
      }
      case 3: {
        colors = new String[]{"GREEN", "RED", "BLACK"};
        break;
      }
      case 4: {
        colors = new String[]{"BLUE", "WHITE", "RED", "BLACK"};
        break;
      }
      case 6: {
        colors = new String[]{"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
        break;
      }
    }
    board = new Board(numOfPlayers);
    controller = new MovementController(board);
    int randomIndex = new Random().nextInt(numOfPlayers);
    moveCount = randomIndex;
    currentColor = colors[randomIndex];
    addPlayers();
    runPlayers();
    currentPlayer = players[randomIndex];
  }

  // Adds players to the game.
  private void addPlayers() throws IOException {
    players = new Player[numOfPlayers];

    for (int i = 0; i < numOfPlayers; i++) {
      System.out.println("adding player number " + String.valueOf(i + 1));
      players[i] = new Player(colors[i], listener.accept());
    }
  }

  // Runs players threads.
  private void runPlayers() {
    for (Player player : players) {
      player.start();
    }
  }

  public class Player extends Thread {
    BufferedReader input;
    PrintWriter output;
    Protocol protocol;
    private String color;
    private Socket socket;

    Player(String color, Socket socket) {
      this.color = color;
      this.socket = socket;

      try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // PrintWriter with automatic line flushing
        output = new PrintWriter(socket.getOutputStream(), true);

        protocol = new Protocol(this);

        output.println("");
        output.println("WELCOME " + this.color + " " + numOfPlayers + " " + currentColor);
        output.println("Waiting for your opponents to connect...");
      } catch (IOException ex) {
        System.out.println("Game.Player error: " + ex);
      }
    }

    void handleCommand(String command) {
      System.out.println("Command from a client: " + command);
      if (command.startsWith("MOVE")) { // MOVE GREEN 0 4 5 4
        String words[] = command.split(" ");
        if (words[1].equals(currentPlayer.color)) {
          int pawnX = Integer.parseInt(words[2]);
          int pawnY = Integer.parseInt(words[3]);
          int fieldX = Integer.parseInt(words[4]);
          int fieldY = Integer.parseInt(words[5]);
          Pawn pawn = board.getPawn(pawnX, pawnY);
          Field field = board.getField(fieldX, fieldY);
          if (controller.isValid(pawnX, pawnY, field)) {
            moveCount++;
            this.protocol.validMoveMessage(pawn, field);
            for (Player player : players) {
              player.protocol.next(colors[moveCount % numOfPlayers]);
              currentPlayer = players[moveCount % numOfPlayers];
            }
            for (Player player : players) {
              if (player != this) {
                player.protocol.playerMoved(pawn, field);
              }
            }
            board.movePawn(pawnX, pawnY, fieldX, fieldY);
            pawn.setX(fieldX);
            pawn.setY(fieldY);
            if (controller.gameOver()) {
              for (Player player : players) {
                player.protocol.winnerMessage(controller.winner);
              }
            }
          } else {
            this.protocol.invalidMoveMessage();
          }
        } else {
          output.println("Mordziu nie ten pionek co ty");
        }
      } else if (command.startsWith("QUIT")) {
        return;
      }
    }

    @Override
    public void run() {
      try {
        System.out.println("All players connected");
        output.println("MESSAGE All players connected.");
        while (true) {
          String command = input.readLine();
          if (command != null) {
            handleCommand(command);
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
