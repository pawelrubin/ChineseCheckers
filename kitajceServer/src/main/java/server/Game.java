package server;

import logic.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class Game {
  private final Board board;
  private int numOfHumans;
  private int numOfBots;
  private int numOfPlayers;
  private Player currentPlayer;
  private String currentColor;
  private MovementController controller;
  private int moveCount;
  private List<Player> players;
  private ServerSocket listener;
  private List<String> colors;
  // --Commented out by Inspection (06/01/2019 00:41):private boolean tie = false;

  Game(int numOfHumans, int numOfBots, ServerSocket listener) throws IOException {
    List<Integer> legalNumOfPLayers = Arrays.asList(2, 3, 4, 6);
    if (!legalNumOfPLayers.contains(numOfHumans + numOfBots)) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    this.numOfHumans = numOfHumans;
    this.numOfBots = numOfBots;
    this.numOfPlayers = numOfBots + numOfHumans;
    this.listener = listener;
    setColors(numOfPlayers);
    board = new Board(numOfPlayers);
    controller = new MovementController(board);
    int randomIndex = new Random().nextInt(numOfPlayers);
    moveCount = randomIndex;
    currentColor = colors.get(randomIndex);
    addPlayers();
    currentPlayer = players.get(randomIndex);
    runPlayers();
  }

// --Commented out by Inspection START (06/01/2019 00:16):
//  public void setTie(boolean tie) {
//    this.tie = tie;
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

  private void addPlayers() throws IOException {
    players = new ArrayList<>();
    addHumans();
    addBots();
  }

  private void setColors(int numOfPlayers) {
    colors = new ArrayList<>();
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

  private void endTurn() {
    moveCount++;
    for (Player player : players) {
      if (player instanceof Human) {
        ((Human) player).protocol.next(colors.get(moveCount % numOfPlayers));
      }
    }
    currentPlayer = players.get(moveCount % numOfPlayers);
  }

  private void checkForWinners() {
    if (controller.someoneFinished()) {
      for (Player player : players) {
        if (player instanceof Human) {
          ((Human) player).protocol.winnerMessage(controller.getWinner());
        }
      }
      colors.remove(controller.getWinner());
      numOfPlayers--;
    }
  }

// --Commented out by Inspection START (06/01/2019 00:16):
//  public Human getHuman(int i) {
////    while (!(players.get(i) instanceof Human)) {
////      i++;
////    }
//    return (Human) players.get(i);
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

// --Commented out by Inspection START (06/01/2019 00:16):
//  public void endGame() {
//    for (Player player: players) {
//      player.kill();
//    }
//  }
// --Commented out by Inspection STOP (06/01/2019 00:16)

  class Human extends Player  {
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
            checkForWinners();
          } else {
            this.protocol.invalidMoveMessage();
          }
        } else {
          output.println("Mordziu nie ten pionek co ty");
        }
      } else if (command.startsWith("END_TURN")) {
        String words[] = command.split(" ");
        if (words[1].equals(currentPlayer.color)) {
          endTurn();
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
            try {
              move();
            } catch (NullPointerException ex) {
              ex.printStackTrace();
              System.out.println("BLĄD W: " + color);
            }
          }
        }
      }
    }

    private void move() {
      Pawn pawn;
      Field bestChoice = null;
      double minDistance = Double.MAX_VALUE;
      double distance, current;
      List<Pawn> usedPawns = new ArrayList<>();
      do {
        pawn = chooseRandomPawn();
        System.out.println("Wybrany pionek " + pawn.getX() + ", " + pawn.getY());
        distance = Double.MAX_VALUE;
        if (usedPawns.contains(pawn)) {
          continue;
        } else {
          usedPawns.add(pawn);
          if (usedPawns.size() == 10) {
            System.out.println("10 pawns have been used, im sending endTurn()");
            endTurn();
            return;
          }
        }
        current = board.distance(board.getField(pawn.getX(), pawn.getY()), destination);
        for (Field[] row : board.getFields()) {
          for (Field field : row) {
            if (field != null) {
              if (board.getPawn(field.getX(), field.getY()) == null) {
                if (controller.isValid(pawn.getX(), pawn.getY(), field, color)) {
                  if (board.getTargetCorner(color).contains(new Point(pawn.getX(), pawn.getY()))) {
                    if (board.distance(field, destination) < current) {
                      distance = board.distance(field, destination);
                      if (distance < minDistance) {
                        bestChoice = field;
                        minDistance = distance;
                      }
                    }
                  } else {
                    Field target = board.getTarget(color);
                    if (target == null) System.out.println("ELO NULLA MAMY");
                    distance = board.distance(field, target);
                    if (distance < minDistance) {
                      bestChoice = field;
                      minDistance = distance;
                    }
                  }
                }
              }
            }
          }
        }
      } while (distance == Double.MAX_VALUE);
      System.out.println("minDistance: " + minDistance);
      moveCount++;
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
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
      board.movePawn(pawn.getX(), pawn.getY(), Objects.requireNonNull(bestChoice).getX(), bestChoice.getY());
      pawn.setX(bestChoice.getX());
      pawn.setY(bestChoice.getY());
      checkForWinners();
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
