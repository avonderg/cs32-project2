package apiaggregator;

import client.ApiClient;
import client.ClientAuth;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import client.Method;
import json.JSONParseable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generic class that helps aggregate apis and the data collected from these apis.
 * @param <T> data type in these apis that is aggregated.
 */
public class ApiAggregator<T> {
  private Map<HttpRequest, HttpResponse<String>> apiResponses = new HashMap<>();

  private EndpointRequestGenerator requestGenerator;
  private ApiClient client = new ApiClient();
  private List<T> objects = new ArrayList<>();
  private JSONParseable<T> formatter;

  private Type type;

  /**
   * ApiAggregator constructor.
   * @param type match or info
   * @param formatter jsonparseable to parse the body of the responses from the api
   * @param requestGenerator endpoint request generator that generates requests for endpoints
   *                         and for general requests.
   */
  public ApiAggregator(Type type, JSONParseable<T> formatter,
                       EndpointRequestGenerator requestGenerator) {
    this.type = type;
    this.formatter = formatter;
    this.requestGenerator = requestGenerator;
  }

  /**
   * Aggregates all of the data expected from the active endpoints of request generator.
   * Formats this data into a List of type T.
   *
   * @return a list of type t representing aggregated objects or null if errors
   */
  public List<T> aggregate() {
    List<String> endpoints = getActiveEndpoints();
    if (endpoints == null) {
      return null;
    }
    String parameters;
    Method method;

    ClientAuth clientAuth = new ClientAuth();
    String apiKey = clientAuth.getApiKey();
    String csLogin = clientAuth.getCSLogin();

    parameters = (type == Type.Match) ? "header:x-api-key=" + apiKey + ";body:auth=" + csLogin
        : "url:auth=" + csLogin + ";url:key=" + apiKey;
    method = (type == Type.Match) ? Method.POST : Method.GET;

    for (String endpoint : endpoints) {
      HttpRequest request = requestGenerator.parseRequest(endpoint, method, parameters);
      apiResponses.put(request, client.makeRequest(request));
    }

    for (HttpRequest request : apiResponses.keySet()) {
      HttpResponse<String> response = apiResponses.get(request);
      objects.addAll(formatter.parseJsonList(response.body()));
    }
    return objects;
  }

  /**
   * Gets the active endpoints necessary to extract and aggregate the data.
   *
   * @return list of strings representing the uris of the active endpoints or null if does not work.
   */
  private List<String> getActiveEndpoints() {
    HttpRequest requestActiveEndpoints = null;
    HttpResponse<String> responseActiveEndpoints;
    String uriMain;

    switch (this.type) {
      case Info:
        requestActiveEndpoints = requestGenerator.getActiveInfo();
        uriMain = ClientRequestGenerator.BASE_URI_INFO;
        break;
      case Match:
        requestActiveEndpoints = requestGenerator.getActiveMatch();
        uriMain = ClientRequestGenerator.BASE_URI_MATCH;
        break;
      default:
        System.out.println("ERROR: Not valid type");
        return null;
    }
    responseActiveEndpoints = client.makeRequest(requestActiveEndpoints);

    List<String> endPointsParts = formatter.getEndpoints(responseActiveEndpoints.body());
    List<String> activeEndpoints =
        endPointsParts.stream().map(s -> uriMain + s).collect(Collectors.toList());
    return activeEndpoints;
  }

}
