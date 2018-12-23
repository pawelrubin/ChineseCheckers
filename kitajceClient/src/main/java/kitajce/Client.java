package kitajce;

import layout.Board;
import layout.Field;
import layout.Pawn;
import layout.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client  {
  private static final int port = 2137;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean isValid;

  Client(String serverAddress) throws Exception {
    // Setup networking
    socket = new Socket(serverAddress, port);
    in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  public void play() throws Exception {
    String response;
    System.out.println("elo");
    try {
      while (true) {
        response = in.readLine();
        isValid = false;
        if (response != null) {
          System.out.println("response from server: " + response);
          if (response.startsWith("WELCOME")) {
            MainController.setColor(response.split(" ")[1]);
            MainController.setNumOfPlayers(Integer.parseInt(response.split(" ")[2]));
            MainController.setCurrentPlayer(response.split(" ")[3]);
            System.out.println("ELO");
          } else if (response.startsWith("VALID_MOVE")) {
            isValid = true;
            System.out.println("dobry ruch kolego.");
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

  public void sendMessage(String message) {
    out.println(message);
  }

  public String getResponse() throws IOException {
    return in.readLine();
  }

  public  boolean isValid() {
    return isValid;
  }
}
