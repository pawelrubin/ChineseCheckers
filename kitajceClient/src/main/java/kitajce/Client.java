package kitajce;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client  {
  private static final int port = 8902;
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
        System.out.println("response from server: " + response);
        if (response.startsWith("QUIT")) {
          break;
        }
      }
      out.println("QUIT");
    } finally {
      socket.close();
    }
  }
}
