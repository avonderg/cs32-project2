package client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientAuthTest {
  ClientAuth clientAuth = new ClientAuth();

  @Test
  public void getApiKeyTest() {
    String apiKey = clientAuth.getApiKey();
    assertEquals("ADYCJAQQSHF", apiKey);
  }

  @Test
  public void getApiCSLoginTest() {
    String csLogin = clientAuth.getCSLogin();
    assertEquals("sanand14", csLogin);
  }
}