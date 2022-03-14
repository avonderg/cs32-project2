package client;

import apiaggregator.Type;
import cvsprocessing.Student;
import json.APIResponseFormatter;
import json.JSONParseable;
import org.junit.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClientRequestGeneratorTest {

  @Test
  public void getActiveInfoTest() {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    ApiClient client = new ApiClient();
    JSONParseable<Student> formatter = new APIResponseFormatter();

    HttpRequest req = requestGenerator.getActiveInfo();
    List<String> urls = formatter.getEndpoints(client.makeRequest(req).body());
    assertEquals(3, urls.size());
    System.out.println(urls.get(0));
    assertEquals(true, urls.get(0).contains("/info"));
    assertEquals(true, urls.get(1).contains("/info"));
    assertEquals(true, urls.get(2).contains("/info"));
  }

  @Test
  public void getActiveMatchTest() {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    ApiClient client = new ApiClient();
    JSONParseable<Student> formatter = new APIResponseFormatter();

    HttpRequest req = requestGenerator.getActiveMatch();
    List<String> urls = formatter.getEndpoints(client.makeRequest(req).body());
    assertEquals(3, urls.size());
    assertEquals(true, urls.get(0).contains("/match"));
    assertEquals(true, urls.get(1).contains("/match"));
    assertEquals(true, urls.get(2).contains("/match"));
  }

  @Test
  public void getApiTest() {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    ApiClient client = new ApiClient();
    JSONParseable<Student> formatter = new APIResponseFormatter();

    HttpRequest req = requestGenerator.getActiveInfo();
    List<String> urls = formatter.getEndpoints(client.makeRequest(req).body());
    assertEquals(3, urls.size());

    ClientAuth clientAuth = new ClientAuth();
    String apiKey = clientAuth.getApiKey();
    String csLogin = clientAuth.getCSLogin();

    String parametersInfo =  "url:auth=" + csLogin + ";url:key=" + apiKey;
    String endpoint = "https://studentinfoapi.herokuapp.com" + urls.get(0);
//    System.out.println(endpoint);
//    System.out.println(parametersInfo);
    HttpRequest request = requestGenerator.parseRequest(endpoint, Method.GET, parametersInfo);

    List<Student> students = formatter.parseJsonList(client.makeRequest(request).body());
    assertEquals(15, students.size());
    assertEquals(200, client.makeRequest(request).statusCode());

  }

}