import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Thread {
  private String color;
  private Socket socket;
  private BufferedReader input;
  private PrintWriter output;
  private Player opponent;

  Player(String color, Socket socket) {
    this.color = color;
    this.socket = socket;

    try {
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // PrintWriter with automatic line flushing
      output = new PrintWriter(socket.getOutputStream(), true);

      output.println("NO KURWA SIEMA");
      output.println("Welcome my friend whose color is " + this.color);
      output.println("Waiting for your opponents to connect...");
    } catch (IOException ex) {
      System.out.println("Player error: " + ex);
    }


  }

  void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  @Override
  public void run() {
    try {
      output.println("MESSAGE All players connected.");

      while (true) {
        String command = input.readLine();
        if (command.startsWith("MOVE")) {

        }
        else if (command.startsWith("QUIT")) {
          return;
        }
      }
    } catch (IOException ex) {
      System.out.println("Player error: " + ex);
    } finally {
      try {
        socket.close();
      } catch (IOException ignored) {
      }
    }
  }
}
