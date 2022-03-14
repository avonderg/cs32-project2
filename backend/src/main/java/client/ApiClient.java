package client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * ApiClient generic class used to make http requests.
 * Credit: from the api lab cs 32
 */
public class ApiClient {
  static final int TIMEOUT_SECONDS = 60;
  // number of seconds until client times out

  private HttpClient client;

  /**
   * Constructor for the api client.
   */
  public ApiClient() {
    this.client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
        .build();
  }

  /**
   * Makes the inputted http request.
   *
   * @param req a specific http request
   * @return the http response from the api
   */
  public HttpResponse<String> makeRequest(HttpRequest req) {
    HttpResponse<String> apiResponse = null;
    try {
      apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());

      while (apiResponse.body()
          .equals("{\"message\": \"Your API call failed due to a server fault\"}")) {
        apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
      }
      // in case 500 server fault make sure retries

      //System.out.print("Status " + apiResponse.statusCode());
      //System.out.println(apiResponse.body());
      //JSONParser.printMessage(apiResponse.body());

    } catch (IOException ioe) {
      System.out.print("ERROR: An I/O error occurred when sending or receiving data.");
      System.out.println(ioe.getMessage());

    } catch (InterruptedException ie) {
      System.out.print("ERROR: The operation was interrupted.");
      System.out.println(ie.getMessage());

    } catch (IllegalArgumentException iae) {
      System.out.print(
          "ERROR: The request argument was invalid. It must be "
              + "built as specified by HttpRequest.Builder.");
      System.out.println(iae.getMessage());

    } catch (SecurityException se) {
      System.out.print("ERROR: There was a security configuration error.");
      System.out.println(se.getMessage());
    }
    return apiResponse;
  }
}
