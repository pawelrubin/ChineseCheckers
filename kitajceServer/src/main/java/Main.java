import server.KitajceServer;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
//    KitajceServer server = new KitajceServer();
    if (args.length <= 1) {
      System.out.println("Too few arguments.");
      System.exit(1);
    }
    try {
      KitajceServer server = new KitajceServer(
              Integer.parseInt(args[0]),
              Integer.parseInt(args[1])
      );
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException ex) {
      System.out.println(ex.getMessage());
      System.exit(1);
    }
  }
}
