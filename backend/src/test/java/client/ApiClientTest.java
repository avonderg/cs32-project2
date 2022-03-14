package client;

import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

public class ApiClientTest {

  @Test
  public void apiClientTest() {
    ApiClient client = new ApiClient();
    ClientRequestGenerator clientRequestGenerator = new ClientRequestGenerator();

    HttpRequest req = clientRequestGenerator.getActiveInfo();
    HttpResponse<String> response = client.makeRequest(req);

    assertEquals(200, response.statusCode()) ;
    assertEquals(URI.create("https://studentinfoapi.herokuapp.com/"
        + "get-active?auth=sanand14&key=ADYCJAQQSHF"), response.uri()) ;
  }

}