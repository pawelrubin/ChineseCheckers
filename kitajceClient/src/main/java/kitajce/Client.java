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
  private MainController mainController;

  void setController(MainController mainController) {
    this.mainController = mainController;
  }

  /**
   * Sets up networking.
   *
   * @param serverAddress address of the server.
   * @throws IOException is thrown by Socket constructor.
   */
  void setConnection(String serverAddress) throws IOException {
    // Setup networking
    socket = new Socket(serverAddress, port);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  /**
   * Sends a message to the server.
   *
   * @param message Message to be send.
   */
  void sendMessage(String message) {
    out.println(message);
  }

  /**
   * Main loop of the client.
   *
   * @throws IOException is thrown by socket.close().
   */
  void play() throws IOException {
    try {
      while (true) {
        final String response = in.readLine();
        if (response != null) {
          System.out.println("Response from server: " + response);
          handleResponse(response);
        }
      }
    } finally {
      socket.close();
    }
  }

  /**
   * Handles response from the server.
   *
   * @param response Response read by the BufferedReader.
   */
  private void handleResponse(String response) {
    String[] words = response.split(" ");
    switch (words[0]) {
      case "WELCOME":
        Platform.runLater(() -> {
          mainController.setColor(words[1]);
          mainController.setNumOfPlayers(Integer.parseInt(words[2]));
          mainController.setCurrentPlayer(words[3]);
        });
        break;
      case "START_GAME":
        Platform.runLater(() -> mainController.drawBoard());
        break;
      case "VALID_MOVE":
        Platform.runLater(() -> mainController.movePawn(Integer.parseInt(words[1]),
                Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                Integer.parseInt(words[4])));
        break;
      case "PLAYER_MOVED":
        Platform.runLater(() -> mainController.movePawn(Integer.parseInt(words[1]),
                Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                Integer.parseInt(words[4])));
        break;
      case "NEXT":
        Platform.runLater(() -> {
          mainController.updateTurnLabel(words[1]);
          mainController.setCurrentPlayer(words[1]);
        });
        break;
      case "WINNER":
        Platform.runLater(() -> mainController.addWinner(words[1]));
        break;
    }
  }
}