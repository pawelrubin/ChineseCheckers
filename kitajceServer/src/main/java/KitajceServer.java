import java.io.IOException;
import java.net.ServerSocket;

public class KitajceServer {

    private static final int port = 8902;

    private void start() throws IOException {
        ServerSocket listener = new ServerSocket(port);
        System.out.println("Kitajce Server is running.");
        try {
            while (true) {
                Player player1 = new Player("green", listener.accept());
                Player player2 = new Player("red", listener.accept());
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                player1.start();
                player2.start();
            }
        } finally {
            listener.close();
        }
    }

    public static void main(String[] args) throws Exception {
        KitajceServer server = new KitajceServer();
        server.start();
    }
}
