package kitajce;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;

public class ClientTest {

  private Client client;

  @BeforeClass
  public void setUp() {
    client = new Client();
//    client.setConnection(address);
  }

  @Test
  public void setController() {
    client.setController(new MainController());
  }

  @Test(expected = ConnectException.class)
  public void setConnection() throws IOException {
    String address = "localhost";
    client.setConnection(address);
  }

  @Test(expected = NullPointerException.class)
  public void sendMessage() {
    client.sendMessage("Message");
  }

}