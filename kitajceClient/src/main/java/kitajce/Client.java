package kitajce;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {
  private static final int port = 2137;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean isValid;

  Client(String serverAddress) throws Exception {
    // Setup networking
    socket = new Socket(serverAddress, port);
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  void play() throws Exception {
    String response;
    try {
      while (true) {
        response = in.readLine();
        isValid = false;
        if (response != null) {
          System.out.println("Response from server: " + response);
          if (response.startsWith("WELCOME")) {
            MainController.setColor(response.split(" ")[1]);
            MainController.setNumOfPlayers(Integer.parseInt(response.split(" ")[2]));
            MainController.setCurrentPlayer(response.split(" ")[3]);
            System.out.println("ELO");
          } else if (response.startsWith("VALID_MOVE")) {
            isValid = true;
            String words[] = response.split(" ");
            MainController.movePawn(Integer.parseInt(words[1]),
                    Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                    Integer.parseInt(words[4]));
          } else if (response.startsWith("PLAYER_MOVED")) {
            String words[] = response.split(" ");
            MainController.movePawn(Integer.parseInt(words[1]),
                    Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                    Integer.parseInt(words[4]));
          } else if (response.startsWith("NEXT")) {
            MainController.setCurrentPlayer(response.split(" ")[1]);
          } else if (response.startsWith("WINNER")) {
            MainController.addWinner(response.split(" ")[1]);
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