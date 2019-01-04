package kitajce;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;

import static org.junit.Assert.*;

public class ClientTest {

  private final String address = "localhost";
  private Client client;

  @BeforeClass
  public void setUp() throws Exception {
    client = new Client();
//    client.setConnection(address);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void setController() {
    client.setController(new MainController());
  }

  @Test (expected = ConnectException.class)
  public void setConnection() throws IOException {
    client.setConnection(address);
  }

  @Test (expected = NullPointerException.class)
  public void sendMessage() {
    client.sendMessage("Message");
  }

  @Test
  public void play() throws IOException {
//    client.play();
  }
}