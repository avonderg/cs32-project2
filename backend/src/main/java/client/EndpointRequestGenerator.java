package client;

import java.net.http.HttpRequest;

/**
 * Interface that ensures that classes implement
 * getActiveInfo and getActiveMatch. This is used in
 * API aggregator and follows the framework of extracting
 * active endpoints.
 * @author Suraj Anand
 */
public interface EndpointRequestGenerator {
  /**
   * Generates the request to get the active info endpoints.
   * @return http request to get active info endpoints
   */
  HttpRequest getActiveInfo();

  /**
   * Generates the request to get the active match endpoints.
   * @return http request to get active match endpoints
   */
  HttpRequest getActiveMatch();

  /**
   * Generates generic request to a specified enpoint with specific method.
   * @param endpoint String representing the URI
   * @param method Method of GET or POST
   * @param parameters parameters to put in header, url, and body
   * @return http request
   */
  HttpRequest parseRequest(String endpoint, Method method, String parameters);
}
