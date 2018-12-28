package server;

import server.Board;
import server.Field;
import server.Pawn;

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
  //private final int numOfHumans;
  private int numOfHumans;
  private int numOfBots;
  private int numOfPlayers;
  private final Board board;
  private Player currentPlayer;
  private String currentColor;
  private MovementController controller;
  private int moveCount;
  private List<Player> players;
  private ServerSocket listener;
  //private String[] colors;
  private List<String> colors;
  private boolean tie = false;

  Game(int numOfHumans, int numOfBots, ServerSocket listener) throws IOException {
    List<Integer> legalNumOfPLayers = Arrays.asList(2, 3, 4, 6);
    if (!legalNumOfPLayers.contains(numOfHumans + numOfBots)) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    colors = new ArrayList<>();
    this.numOfHumans = numOfHumans;
    this.numOfBots = numOfBots;
    this.numOfPlayers = numOfBots + numOfHumans;
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
    currentPlayer = players.get(randomIndex);
  }

  public void setTie(boolean tie) {
    this.tie = tie;
  }

  private void addPlayers() throws IOException {
    players = new ArrayList<>();
    addHumans();
    addBots();
  }

  // Adds humans to the game.
  private void addHumans() throws IOException {
    int i = 0;
    while (i < numOfHumans) {
      System.out.println("adding player number " + String.valueOf(i + 1));
      players.add(new Human(colors.get(i), listener.accept()));
      i++;
    }
  }

  private void addBots() {
    int i = 0;
    while (i < numOfBots) {
      System.out.println("adding player (bot) number " + String.valueOf(i + numOfHumans + 1));
      players.add(new Bot(colors.get(i + numOfHumans)));
      i++;
    }
  }
  // Runs players threads.
  private void runPlayers() {
    for (Player player : players) {
      player.start();
    }
  }

  public class Human extends Player {
    BufferedReader input;
    PrintWriter output;
    Protocol protocol;
//    private String color;
    private Socket socket;

    Human(String color, Socket socket) {
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
        System.out.println("server.Game.Player error: " + ex);
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
              if (player instanceof Human) {
                ((Human) player).protocol.next(colors.get(moveCount % numOfPlayers));
              }
            }
            currentPlayer = players.get(moveCount % numOfPlayers);
            for (Player player : players) {
              if (player != this) {
                if (player instanceof Human) {
                  ((Human) player).protocol.playerMoved(pawn, field);
                }
              }
            }
            board.movePawn(pawnX, pawnY, fieldX, fieldY);
            pawn.setX(fieldX);
            pawn.setY(fieldY);
            if (controller.gameOver()) {
              for (Player player : players) {
                if (player instanceof Human) {
                  ((Human) player).protocol.winnerMessage(controller.winner);
                }
              }
              colors.remove(controller.winner);
              numOfPlayers--;
            }
            else if (tie) {

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
          currentPlayer = players.get(moveCount % numOfPlayers);
          for (Player player : players) {
            if (player instanceof Human) {
              ((Human) player).protocol.next(colors.get(moveCount % numOfPlayers));
            }
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
        System.out.println("server.Game.Player error: " + ex);
      } finally {
        try {
          socket.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

  public class Bot extends Player {
    private List<Pawn> pawns;
    private Field destination;

    Bot(String color) {
      this.color = color;
      setPawns();
//      setDestination();
      destination = board.getField(0, 4);
    }

    private void setPawns() {
      pawns = board.getPawnsByColor(color);
    }

    @Override
    public void run() {
      while (true) {
        synchronized (this) {
          if (currentPlayer == this) {
            move();
//            endTurn();
          }
        }
      }
    }

    private void endTurn() {
      moveCount++;
      for (Player player : players) {
        if (player instanceof Human) {
          ((Human) player).protocol.next(colors.get(moveCount % numOfPlayers));
        }
      }
      currentPlayer = players.get(moveCount % numOfPlayers);
    }

    private void move() {
      Pawn pawn;
      Field bestChoice = null;
      double minDistance = Double.MAX_VALUE;
      double distance;
      do {
        pawn = chooseRandomPawn();
        System.out.println("Wybrany pionek " + pawn.getX() + ", " + pawn.getY());
        distance = 0;
        for (Field[] row : board.getFields()) {
          for (Field field : row) {
            if (field != null) {
              if (board.getPawn(field.getX(), field.getY()) == null) {
                if (controller.isValid(pawn.getX(), pawn.getY(), field, color)) {
                  distance = board.distance(field, destination);
                  System.out.println("distance: " + distance);
                  if (distance < minDistance) {
                    bestChoice = field;
                    minDistance = distance;
                  }
                }
              }
            }
          }
        }
      } while (distance == 0);
      System.out.println("minDistance: " + minDistance);
      moveCount++;
      for (Player player : players) {
        if (player instanceof Human) {
          ((Human) player).protocol.next(colors.get(moveCount % numOfPlayers));
        }
      }
      currentPlayer = players.get(moveCount % numOfPlayers);
      for (Player player : players) {
        if (player != this) {
          if (player instanceof Human) {
            ((Human) player).protocol.playerMoved(pawn, bestChoice);
          }
        }
      }
      board.movePawn(pawn.getX(), pawn.getY(), bestChoice.getX(), bestChoice.getY());
      pawn.setX(bestChoice.getX());
      pawn.setY(bestChoice.getY());
      if (controller.gameOver()) {
        for (Player player : players) {
          if (player instanceof Human) {
            ((Human) player).protocol.winnerMessage(controller.winner);
          }
        }
        colors.remove(controller.winner);
        numOfPlayers--;
      }
    }

    private void makeMove() {
//      Pawn pawn = chooseRandomPawn();
    }

    private void setDestination() {
      destination = board.getDestination(color);
    }

    private Pawn chooseRandomPawn() {
      Pawn pawn;
      do {
        pawn = pawns.get(new Random().nextInt(Board.numOfPawns));
      } while (!pawn.getColor().equals(this.color));
      return pawn;
    }

  }
}
