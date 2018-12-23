package kitajce;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {
  private static final int port = 2137;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean isValid;
  private MainController mainController;
  private String serverAddress;

  Client(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  void setConnection() throws IOException {
    // Setup networking
    socket = new Socket(serverAddress, port);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  void setController(MainController mainController) {
    this.mainController = mainController;
  }

  void play() throws IOException {
    try {
      while (true) {
        final String response = in.readLine();
        isValid = false;
        if (response != null) {
          System.out.println("Response from server: " + response);
          if (response.startsWith("WELCOME")) {
            Platform.runLater(() -> {
              mainController.setColor(response.split(" ")[1]);
              mainController.setNumOfPlayers(Integer.parseInt(response.split(" ")[2]));
              mainController.setCurrentPlayer(response.split(" ")[3]);
            });
            System.out.println("ELO");
          } else if (response.startsWith("START_GAME")) {
            Platform.runLater(() -> mainController.drawBoard());
          } else if (response.startsWith("VALID_MOVE")) {
            isValid = true;
            String words[] = response.split(" ");
            Platform.runLater(() -> mainController.movePawn(Integer.parseInt(words[1]),
                    Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                    Integer.parseInt(words[4])));
          } else if (response.startsWith("PLAYER_MOVED")) {
            String words[] = response.split(" ");
            Platform.runLater(() -> mainController.movePawn(Integer.parseInt(words[1]),
                    Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                    Integer.parseInt(words[4])));
          } else if (response.startsWith("NEXT")) {
            Platform.runLater(() -> {
              mainController.updateTurnLabel(response.split(" ")[1]);
              mainController.setCurrentPlayer(response.split(" ")[1]);
            });
          } else if (response.startsWith("WINNER")) {
            Platform.runLater(() -> mainController.addWinner(response.split(" ")[1]));
          } else if (response.startsWith("QUIT")) {
            break;
          } else {
            isValid = false;
          }
        }
      }
      out.println("QUIT");
    } finally {
      socket.close();
    }
  }

  void sendMessage(String message) {
    out.println(message);
  }
}