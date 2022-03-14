package edu.brown.cs.student.main;

import apiaggregator.ApiAggregator;
import apiaggregator.Type;
import client.ApiClient;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import client.Method;
import cvsprocessing.Student;
import json.APIResponseFormatter;
import json.JSONParseable;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handles commands for the API aggregator portion of
 * the CS32 project 1: Data.
 * @author Sanand14
 */
public class APICommander implements CommandAcceptor {

  /**
   * Handles commands from the command line.
   *
   * @param input from cmd line.
   */
  @Override
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "active":
        List<String> activeEndpoints = getActive(input);
        if (activeEndpoints != null) {
          for (String activeEndpoint : activeEndpoints) {
            System.out.println(activeEndpoint);
          }
        }
        break;
      case "api":
        HttpResponse<String> response = callApiMethod(input);
        if (response != null) {
          APIResponseFormatter formatter = new APIResponseFormatter();
          try {
            List<Student> students = formatter.parseJsonList(response.body());
            for (Student student : students) {
              System.out.println(student.getName() + " ID: " + student.getID());
            }
          } catch (Exception e) {
            System.out.println("ERROR: Could not get students from JSON");
          }
        }
        break;
      case "api_aggregate":
        List<Student> students = apiAggregate(input);
        if (students != null) {
          for (Student student : students) {
            System.out.println(student.getName() + " ID: " + student.getID());
          }
        }
        break;
      default:
        System.out.println("ERROR: No such command");
        break;
    }
  }

  /**
   * Aggregates all the students either matches or info.
   *
   * @param input from the command line
   * @return List of students aggregated
   */
  private List<Student> apiAggregate(String[] input) {
    JSONParseable<Student> formatter = new APIResponseFormatter();
    if (!(input.length == 2)) {
      System.out.println("ERROR: Incorrect length of parameters");
      return null;
    }
    if (!(input[1].equals("info") || input[1].equals("match"))) {
      System.out.println("ERROR: parameter must be info or match");
      return null;
    }
    Type type = (input[1].equals("info")) ? Type.Info : Type.Match;
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    ApiAggregator<Student> aggregator =
        new ApiAggregator<Student>(type, formatter, requestGenerator);
    return aggregator.aggregate();
  }

  /**
   * Creates an HttpRequest with the parameters, method, and uri given
   * in the input. Then sends that to the api specified.
   *
   * @param input from the command line
   * @return HttpResponse from the api
   */
  private HttpResponse<String> callApiMethod(String[] input) {
    HttpRequest apiRequest;

    if (!(input.length == 4 || input.length == 3)) {
      System.out.println("ERROR: Incorrect length of parameters");
      return null;
    }

    if (!(input[1].equals("POST") || input[1].equals("GET"))) {
      System.out.println("ERROR: Method must be POST or GET");
      return null;
    }

    Method method = (input[1].equals("POST")) ? Method.POST : Method.GET;
    String uri = input[2];

    String parameters = (input.length == 3) ? "" : input[3];

    ClientRequestGenerator reqGen = new ClientRequestGenerator();
    ApiClient client = new ApiClient();

    apiRequest = reqGen.parseRequest(uri, method, parameters);
    if (apiRequest == null) {
      return null;
    }

    HttpResponse<String> apiResponse = client.makeRequest(apiRequest);
    if (apiResponse.statusCode() >= 500) {
      APIResponseFormatter formatter = new APIResponseFormatter();
      System.out.println("ERROR: " + formatter.getMessage(apiResponse.body()).getMessage());
      return null;
    }

    if (apiResponse.statusCode() >= 400) {
      System.out.println("ERROR: 400 Status Code of the Server");
      return null;
    }
    return apiResponse;
  }

  /**
   * Gets the active info or matches endpoints.
   *
   * @param input from the cmd line
   * @return list of strings representing active endpoints.
   */
  public List<String> getActive(String[] input) {
    HttpRequest activeRequest;

    if (!(input.length == 2)) {
      System.out.println("ERROR: wrong number of parameters");
      return null;
    }
    if (!(input[1].equals("info") || input[1].equals("match"))) {
      System.out.println("ERROR: type must be \"info\" or \"match\"");
      return null;
    }

    ClientRequestGenerator reqGen = new ClientRequestGenerator();
    ApiClient client = new ApiClient();
    APIResponseFormatter formatter = new APIResponseFormatter();

    activeRequest =
        (input[1].equals("info") ? reqGen.getActiveInfo() : reqGen.getActiveMatch());

    HttpResponse<String> getActiveResponse = client.makeRequest(activeRequest);

    String uriMain = (input[1].equals("info") ? ClientRequestGenerator.BASE_URI_INFO
        : ClientRequestGenerator.BASE_URI_MATCH);

    List<String> activeParts = formatter.getEndpoints(getActiveResponse.body());
    List<String> activeEndpoints =
        activeParts.stream().map(s -> uriMain + s).collect(Collectors.toList());
    return activeEndpoints;
  }

}
