package kitajce;

import layout.Board;
import layout.Field;
import layout.Pawn;
import layout.Point;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client  {
  private static final int port = 2137;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

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
      response = in.readLine();
      if (response.startsWith("WELCOME")) {
        System.out.println("ELO");
      }
      while (true) {
        response = in.readLine();
        if (response != null) {
          System.out.println("response from server: " + response);
          if (response.startsWith("VALID_MOVE")) {
            System.out.println("dobry ruch kolego.");
          } else if (response.startsWith("PLAYER_MOVED")) {
            String words[] = response.split(" ");
            MainController.movePawn(Integer.parseInt(words[1]),
                    Integer.parseInt(words[2]), Integer.parseInt(words[3]),
                    Integer.parseInt(words[4]));
          } else if (response.startsWith("QUIT")) {
            break;
          }
        }
      }
      out.println("QUIT");
    } finally {
      socket.close();
    }
  }

  public void movePawn(Pawn pawn, Field field) {
    pawn.setX(field.getX());
    pawn.setY(field.getY());
    pawn.repaint(field);
  }

  public void sendMessage(String message) {
    out.println(message);
  }
}
