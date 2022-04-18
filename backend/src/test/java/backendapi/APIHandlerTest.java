package backendapi;

import client.ApiClient;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import client.Method;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class APIHandlerTest {
  // need to run ./run --gui and load database while running these tests

//  @Test
//  public void aTestAPIHandleTableName() {
//    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
//    ApiClient client = new ApiClient();
//
//    String endpoint = "http://localhost:4567/tableNames";
//    HttpRequest request = requestGenerator.parseRequest(endpoint, Method.GET, "");
//    String resBody = client.makeRequest(request).body();
//    System.out.println(resBody);
//    String[] names = new Gson().fromJson(resBody, String[].class);
//    Assert.assertEquals(4, names.length);
//  }
//
//  @Test
//  public void bTestAPIHandleTables() throws IOException {
//    // way of making post request from https://www.baeldung.com/httpurlconnection-post
//    URL url = new URL("http://localhost:4567/table");
//    URLConnection con = url.openConnection();
//    HttpURLConnection http = (HttpURLConnection) con;
//    http.setRequestMethod("POST");
//    http.setDoOutput(true);
//    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//    http.setRequestProperty("Access-Control-Allow-Origin","*");
//    String jsonInputString = "{'name': 'tas'}";
//
//    try(OutputStream os = con.getOutputStream()) {
//      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//      os.write(input, 0, input.length);
//    }
//
//    try(BufferedReader br = new BufferedReader(
//        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//      StringBuilder response = new StringBuilder();
//      String responseLine;
//      while ((responseLine = br.readLine()) != null) {
//        response.append(responseLine.trim());
//      }
//      System.out.println(response);
//      JSONObject res = new JSONObject(response.toString());
//      String head = res.getString("headers");
//      String[] headers = new Gson().fromJson(head, String[].class);
//      System.out.println(Arrays.toString(headers));
//      Assert.assertEquals(3, headers.length);
//
//      JSONArray rows = res.getJSONArray("rows");
//      List<JSONObject> tas = new ArrayList<>();
//      for (int i = 0; i < rows.length(); i++) {
//        JSONObject jsonobject = rows.getJSONObject(i);
//        tas.add(jsonobject);
//      }
//      System.out.println(Arrays.toString(tas.toArray()));
//      Assert.assertEquals(7, tas.size());
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void cTestAPIHandleAdd() throws IOException {
//    // way of making post request from https://www.baeldung.com/httpurlconnection-post
//    URL url = new URL("http://localhost:4567/add");
//    URLConnection con = url.openConnection();
//    HttpURLConnection http = (HttpURLConnection) con;
//    http.setRequestMethod("POST");
//    http.setDoOutput(true);
//    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//    http.setRequestProperty("Access-Control-Allow-Origin","*");
//    String jsonInputString = "{'name': 'horoscopes', " +
//        "'columns':' horoscope_id   , horoscope', 'values':'13  , Horse'}";
//
//    try(OutputStream os = con.getOutputStream()) {
//      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//      os.write(input, 0, input.length);
//    }
//
//    try(BufferedReader br = new BufferedReader(
//        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//      StringBuilder response = new StringBuilder();
//      String responseLine;
//      while ((responseLine = br.readLine()) != null) {
//        response.append(responseLine.trim());
//      }
//      JSONObject res = new JSONObject(response.toString());
//      String head = res.getString("headers");
//      String[] headers = new Gson().fromJson(head, String[].class);
//      Assert.assertEquals(2, headers.length);
//
//      JSONArray rows = res.getJSONArray("rows");
//      List<JSONObject> horoscopes = new ArrayList<>();
//      for (int i = 0; i < rows.length(); i++) {
//        JSONObject jsonobject = rows.getJSONObject(i);
//        horoscopes.add(jsonobject);
//      }
//      Assert.assertEquals(13, horoscopes.size());
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void dTestAPIHandleUpdate() throws IOException {
//    // way of making post request from https://www.baeldung.com/httpurlconnection-post
//    URL url = new URL("http://localhost:4567/update");
//    URLConnection con = url.openConnection();
//    HttpURLConnection http = (HttpURLConnection) con;
//    http.setRequestMethod("POST");
//    http.setDoOutput(true);
//    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//    http.setRequestProperty("Access-Control-Allow-Origin","*");
//    String jsonInputString = "{'name': 'horoscopes', "
//        + "'row': {'horoscope_id':'13','horoscope':'Horse'}, "
//        + "'columns':' horoscope_id   , horoscope', 'values':'13  , Dragon'}";
//
//    try(OutputStream os = con.getOutputStream()) {
//      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//      os.write(input, 0, input.length);
//    }
//
//    try(BufferedReader br = new BufferedReader(
//        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//      StringBuilder response = new StringBuilder();
//      String responseLine;
//      while ((responseLine = br.readLine()) != null) {
//        response.append(responseLine.trim());
//      }
//      JSONObject res = new JSONObject(response.toString());
//      String head = res.getString("headers");
//      String[] headers = new Gson().fromJson(head, String[].class);
//      Assert.assertEquals(2, headers.length);
//
//      JSONArray rows = res.getJSONArray("rows");
//      List<JSONObject> horoscopes = new ArrayList<>();
//      for (int i = 0; i < rows.length(); i++) {
//        JSONObject jsonobject = rows.getJSONObject(i);
//        horoscopes.add(jsonobject);
//      }
//      Assert.assertEquals(13, horoscopes.size());
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void eTestAPIHandleDelete() throws IOException {
//    // way of making post request from https://www.baeldung.com/httpurlconnection-post
//    URL url = new URL("http://localhost:4567/delete");
//    URLConnection con = url.openConnection();
//    HttpURLConnection http = (HttpURLConnection) con;
//    http.setRequestMethod("POST");
//    http.setDoOutput(true);
//    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//    http.setRequestProperty("Access-Control-Allow-Origin","*");
//    String jsonInputString = "{'name': 'horoscopes', " +
//        "'row': {'horoscope_id':'13','horoscope':'Dragon'}}";
//    try(OutputStream os = con.getOutputStream()) {
//      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
//      os.write(input, 0, input.length);
//    } catch (Exception e) {
//      Assert.fail();
//    }
//
//    try(BufferedReader br = new BufferedReader(
//        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
//      StringBuilder response = new StringBuilder();
//      String responseLine;
//      while ((responseLine = br.readLine()) != null) {
//        response.append(responseLine.trim());
//      }
//      JSONObject res = new JSONObject(response.toString());
//      String head = res.getString("headers");
//      String[] headers = new Gson().fromJson(head, String[].class);
//      Assert.assertEquals(2, headers.length);
//
//      JSONArray rows = res.getJSONArray("rows");
//      List<JSONObject> horoscopes = new ArrayList<>();
//      for (int i = 0; i < rows.length(); i++) {
//        JSONObject jsonobject = rows.getJSONObject(i);
//        horoscopes.add(jsonobject);
//      }
//      Assert.assertEquals(12, horoscopes.size());
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//  }
}

