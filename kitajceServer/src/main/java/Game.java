import com.sun.security.ntlm.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Game {
  //private final int numOfPlayers;
  private int numOfPlayers;
  private final Board board;
  private Player currentPlayer;
  private String currentColor;
  private MovementController controller;
  private int moveCount;
  private Player[] players;
  private ServerSocket listener;
  //private String[] colors;
  private List<String> colors;

  Game(int numOfPlayers, ServerSocket listener) throws IOException {
    List<Integer> legalNumOfPLayers = Arrays.asList(2, 3, 4, 6);
    if (!legalNumOfPLayers.contains(numOfPlayers)) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    colors = new ArrayList<>();
    this.numOfPlayers = numOfPlayers;
    this.listener = listener;
    switch (numOfPlayers) {
      case 2: {
        //colors = new String[]{"GREEN", "YELLOW"};
        colors.add("GREEN");
        colors.add("YELLOW");
        break;
      }
      case 3: {
        //colors = new String[]{"GREEN", "RED", "BLACK"};
        colors.add("GREEN");
        colors.add("RED");
        colors.add("BLACK");
        break;
      }
      case 4: {
        //colors = new String[]{"BLUE", "WHITE", "RED", "BLACK"};
        colors.add("BLUE");
        colors.add("WHITE");
        colors.add("RED");
        colors.add("BLACK");
        break;
      }
      case 6: {
        //colors = new String[]{"GREEN", "WHITE", "RED", "YELLOW", "BLACK", "BLUE"};
        colors.add("GREEN");
        colors.add("WHITE");
        colors.add("RED");
        colors.add("YELLOW");
        colors.add("BLACK");
        colors.add("BLUE");
        break;
      }
    }
    board = new Board(numOfPlayers);
    controller = new MovementController(board);
    int randomIndex = new Random().nextInt(numOfPlayers);
    moveCount = randomIndex;
    currentColor = colors.get(randomIndex);
    addPlayers();
    runPlayers();
    currentPlayer = players[randomIndex];
  }

  // Adds players to the game.
  private void addPlayers() throws IOException {
    players = new Player[numOfPlayers];

    for (int i = 0; i < numOfPlayers; i++) {
      System.out.println("adding player number " + String.valueOf(i + 1));
      players[i] = new Player(colors.get(i), listener.accept());
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
          if (controller.isValid(pawnX, pawnY, field, currentPlayer.color)) {
            moveCount++;
            this.protocol.validMove(pawn, field);
            for (Player player : players) {
              player.protocol.next(colors.get(moveCount % numOfPlayers));
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
              colors.remove(controller.winner);
              numOfPlayers--;
            }
          } else {
            this.protocol.invalidMoveMessage();
          }
        } else {
          output.println("Mordziu nie ten pionek co ty");
        }
      } else if (command.startsWith("END_TURN")) {
        String words[] = command.split(" ");
        if (words[1].equals(currentPlayer.color)) {
          moveCount++;
          currentPlayer = players[moveCount % numOfPlayers];
          for (Player player : players) {
            player.protocol.next(colors.get(moveCount % numOfPlayers));
          }
        }
      }
    }

    @Override
    public void run() {
      try {
        System.out.println("All players connected");
        protocol.allConnected();
        protocol.startGame();
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

  public class Bot extends Thread {
    String color;
    Socket socket;
    BufferedReader input;

    public Bot(String color, Socket socket) {
      this.color = color;
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        while (true) {
          String command = input.readLine();
          if (command != null) {

          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          socket.close();
        } catch (IOException ignored) {}
      }
    }
  }
}
