package client;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Generates Http Requests for our specific APIs. Can
 * be used to get active student match or info as well as
 * to generate custom requests from command line parameters.
 *
 * @author Suraj Anand
 */
public class ClientRequestGenerator implements EndpointRequestGenerator {
  /**
   * URLs that are useful for getting endpoints and students.
   */
  public static final String BASE_URI_INFO =
      "https://studentinfoapi.herokuapp.com";
  public static final String BASE_URI_MATCH =
      "https://studentmatchapi.herokuapp.com";

  public static final String ACTIVE_END_POINTS_INFO =
      "https://studentinfoapi.herokuapp.com/get-active";
  public static final String ACTIVE_END_POINTS_MATCH =
      "https://studentmatchapi.herokuapp.com/get-active";

  private ClientAuth clientAuth = new ClientAuth();

  private HashMap<String, List<String>> parameterMap = new HashMap<>();
  // stores the parameters

  /**
   * Creates http request to get active student info.
   *
   * @return http request
   */
  public HttpRequest getActiveInfo() {

    String authReqUri = addAuthGetReq(ACTIVE_END_POINTS_INFO);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(authReqUri)).GET().build();

    return request;
  }

  /**
   * Creates http request to get active student matches.
   *
   * @return http request
   */
  public HttpRequest getActiveMatch() {
    String authReqUri = addAuthGetReq(ACTIVE_END_POINTS_MATCH);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(authReqUri)).GET().build();

    return request;
  }

  /**
   * Parses the parameters in the String parameters (must be in format
   * {header/uri/body}:{auth/others}={cslogin/specificinfo};nextparam;nextparam).
   * Creates http request based off these params and the request URI and method.
   *
   * @param reqUri     request URI
   * @param reqMethod  request method (either GET or POST)
   * @param parameters parameters (format is
   *                   {header/uri/body}:{auth/others}={cslogin/specificinfo};nextparam;nextparam)
   * @return http request from info given
   */
  public HttpRequest parseRequest(String reqUri, Method reqMethod, String parameters) {
    List<String> specificParameter;
    List<String> parameterList = Arrays.asList(parameters.split(";"));
    parameterMap.clear();
    if (parameterList.size() > 1) {
      for (String parameter : parameterList) {
        specificParameter = Arrays.asList(parameter.split(":"));
        if (specificParameter.size() != 2) {
          System.out.println("ERROR: Could not parse parameters");
          return null;
        }
        if (!(specificParameter.get(0).equals("header")
            || specificParameter.get(0).equals("body")
            || specificParameter.get(0).equals("url"))) {
          System.out.println("ERROR: Could not parse parameters");
          return null;
        }
        if (parameterMap.containsKey(specificParameter.get(0))) {
          List<String> valuesIn = parameterMap.get(specificParameter.get(0));
          valuesIn.add(specificParameter.get(1));
          // might have to replace I dont think though bc object
        } else {
          List<String> parameterValues = new ArrayList<>(1);
          parameterValues.add(specificParameter.get(1));

          parameterMap.put(specificParameter.get(0), parameterValues);
        }
      }
    }
    return createRequest(reqUri, reqMethod);
  }

  /**
   * Creates the new http request.
   *
   * @param reqUri URI given
   * @param method POST or GET
   * @return http request
   */
  private HttpRequest createRequest(String reqUri, Method method) {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    requestBuilder = addUri(reqUri, requestBuilder);
    if (requestBuilder == null) {
      return null;
    }
    requestBuilder = addHeaders(requestBuilder);
    if (requestBuilder == null) {
      return null;
    }

    requestBuilder = addMethod(method, requestBuilder);
    if (requestBuilder == null) {
      return null;
    }
    return requestBuilder.build();
  }

  /**
   * Adds headers to the http request builder.
   *
   * @param requestBuilder http request builder
   * @return null if error or http request builder with updated headers
   */
  private HttpRequest.Builder addHeaders(HttpRequest.Builder requestBuilder) {
    List<String> individualHeaderSeperated;
    List<String> headersNotSeparated;
    List<String> headersSeparated = new ArrayList<>();

    if (!parameterMap.containsKey("header")) {
      return requestBuilder;
    }

    headersNotSeparated = parameterMap.get("header");
    for (String headerNotSeparated : headersNotSeparated) {
      individualHeaderSeperated = Arrays.asList(headerNotSeparated.split("="));
      if (individualHeaderSeperated.size() != 2) {
        System.out.println("ERROR: Could not parse parameters");
        return null;
      }
      headersSeparated.add(individualHeaderSeperated.get(0));
      headersSeparated.add(individualHeaderSeperated.get(1));
    }
    return requestBuilder.headers(headersSeparated.toArray(String[]::new));
  }

  /**
   * Adds uri to the http request builder. Uses params to add potential appendages
   * (i.e. ?auth=cslogin).
   *
   * @param reqUri         URI given
   * @param requestBuilder http request builder
   * @return null if error or http request builder with given URI
   */
  private HttpRequest.Builder addUri(String reqUri, HttpRequest.Builder requestBuilder) {
    String reqUriParams;
    HttpRequest.Builder builder = null;
    URI uri = null;
    if (!parameterMap.containsKey("url")) {
      try {
        uri = URI.create(reqUri);
        builder = requestBuilder.uri(uri);
      } catch (Exception e) {
        System.out.println("ERROR: Could not parse URI");
        return null;
      }
      return builder;
    }

    List<String> urlParams = parameterMap.get("url");
    switch (urlParams.size()) {
      case 0:
        reqUriParams = reqUri;
        break;
      case 1:
        reqUriParams = reqUri + "?" + urlParams.get(0);
        break;
      default:
        reqUriParams = reqUri + "?" + urlParams.get(0);
        for (String urlParam : urlParams.subList(1, urlParams.size())) {
          reqUriParams += "&" + urlParam;
        }
        break;
    }

    try {
      uri = URI.create(reqUriParams);
      builder = requestBuilder.uri(uri);
    } catch (Exception e) {
      System.out.println("ERROR: Could not parse URI");
      return null;
    }
    return builder;
  }

  /**
   * Adds method to the http request builder. If GET, then just calls GET(). If POST, looks
   * for body in params and formats that json as an argument to POST().
   *
   * @param method         GET or POST
   * @param requestBuilder http request builder
   * @return http request builder with method added
   */
  private HttpRequest.Builder addMethod(Method method, HttpRequest.Builder requestBuilder) {
    String body = "";
    List<String> separatedBody;

    if (method == Method.GET) {
      return requestBuilder.GET();
    }

    // otherwise assume is POST (as only post and GET are implemented
    if (parameterMap.containsKey("body")) {
      body += "{";
      for (String bodyParam : parameterMap.get("body")) {
        separatedBody = Arrays.asList(bodyParam.split("="));
        if (separatedBody.size() != 2) {
          System.out.println("ERROR: Could not parse parameters");
          return null;
        }
        body += "\"" + separatedBody.get(0) + "\":\"" + separatedBody.get(1) + "\",";
      }
      body = body.substring(0, body.length() - 1);
      // removes trailing comma
      body += "}";
    }
    return requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
  }

  /**
   * Used to authorize URI for getting active info/matches.
   *
   * @param reqUri input url
   * @return input url with authorized appendages
   */
  private String addAuthGetReq(String reqUri) {
    return reqUri + "?auth=" + clientAuth.getCSLogin() + "&key=" + clientAuth.getApiKey();
  }


//  public HttpRequest getInfoGetRequest(String reqUri) {
//    String authReqUri = addAuthGetReq(reqUri);
//    HttpRequest request = HttpRequest.newBuilder()
//        .uri(URI.create(authReqUri)).GET().build();
//    return request;
//  }
//
//  public HttpRequest getMatchPostRequest(String reqUri) {
//    String apiKey = ClientAuth.getApiKey();
//    String csLogin = ClientAuth.getCSLogin();
//
//    HttpRequest request = HttpRequest.newBuilder().header("x-api-key", apiKey)
//        .uri(URI.create(reqUri))
//        .POST(HttpRequest.BodyPublishers.ofString("{\"auth\":\"" + csLogin + "\"}")).build();
//    return request;
//  }

}
