package server;

import logic.Board;
import logic.Field;
import logic.MovementController;
import logic.Pawn;

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

  public Human getHuman(int i) {
//    while (!(players.get(i) instanceof Human)) {
//      i++;
//    }
    return (Human) players.get(i);
  }

  public void endGame() {
    for (Player player: players) {
      player.kill();
    }
  }

  public class Human extends Player  {
    BufferedReader input;
    PrintWriter output;
    Protocol protocol;
    private final Socket socket;

    Human(String color, Socket socket) {
      alive = true;
      this.color = color;
      this.socket = socket;

      try {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // PrintWriter with automatic line flushing
        output = new PrintWriter(socket.getOutputStream(), true);

        protocol = new Protocol(this.output);

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
                  ((Human) player).protocol.winnerMessage(controller.getWinner());
                }
              }
              colors.remove(controller.getWinner());
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
        while (alive) {
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

  class Bot extends Player {
    private List<Pawn> pawns;
    private Field destination;

    Bot(String color) {
      alive = true;
      this.color = color;
      setPawns();
//      destination = board.getField(0, 4);
      setDestination();
    }

    private void setPawns() {
      pawns = board.getPawnsByColor(color);
    }

    @Override
    public void run() {
      while (alive) {
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
      double distance, current;
      do {
        pawn = chooseRandomPawn();
        System.out.println("Wybrany pionek " + pawn.getX() + ", " + pawn.getY());
        distance = Double.MAX_VALUE;
        current = board.distance(board.getField(pawn.getX(), pawn.getY()), destination);
        for (Field[] row : board.getFields()) {
          for (Field field : row) {
            if (field != null) {
              if (board.getPawn(field.getX(), field.getY()) == null) {
                if (controller.isValid(pawn.getX(), pawn.getY(), field, color)) {
//                  if (board.distance(field, destination) < current) {
                    distance = board.distance(field, destination);
//                  }
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
      } while (distance == Double.MAX_VALUE);
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
            ((Human) player).protocol.winnerMessage(controller.getWinner());
          }
        }
        colors.remove(controller.getWinner());
        numOfPlayers--;
      }
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
