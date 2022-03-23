package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class APIHandlerErrorsTest {
  @Before
  public void aaLoadDatabase() {
    TableCommander commander = new TableCommander();
    commander.handleCommand(new String[]{"load_db", "../data/horoscopes.sqlite3"});
  }

  @Test
  public void aTestAPIHandleAddError() throws IOException, SQLException {
    // way of making post request from https://www.baeldung.com/httpurlconnection-post
    URL url = new URL("http://localhost:4567/add");
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    http.setRequestProperty("Access-Control-Allow-Origin","*");
    String jsonInputString = "{'name': 'horoscopes', " +
        "'columns':' haroscope_id   , horoscope', 'values':'13  , Horse'}";

    try(OutputStream os = con.getOutputStream()) {
      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      JSONObject res = new JSONObject(response.toString());
      String head = res.getString("headers");
      String[] headers = new Gson().fromJson(head, String[].class);
      Assert.assertEquals(2, headers.length);

      JSONArray rows = res.getJSONArray("rows");
      List<JSONObject> horoscopes = new ArrayList<>();
      for (int i = 0; i < rows.length(); i++) {
        JSONObject jsonobject = rows.getJSONObject(i);
        horoscopes.add(jsonobject);
      }
      Assert.assertEquals(12, horoscopes.size());
    } catch (JSONException e) {
      Assert.assertEquals(12, TableCommander.getDb().getTable("horoscopes").getNumRows());
    }
  }

  @Test
  public void bTestAPIHandleUpdateError() throws IOException, SQLException {
    // way of making post request from https://www.baeldung.com/httpurlconnection-post
    URL url = new URL("http://localhost:4567/update");
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    http.setRequestProperty("Access-Control-Allow-Origin","*");
    String jsonInputString = "{'name': 'horoscopes', "
        + "'row': {'horoscope_id':'13','horoscope':'Horse'}, "
        + "'columns':' horoscope_id   , horoscope', 'values':'13  , Dragon, asdf'}";

    try(OutputStream os = con.getOutputStream()) {
      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }

    try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      System.out.println(response);
      JSONObject res = new JSONObject(response.toString());
      String head = res.getString("headers");
      String[] headers = new Gson().fromJson(head, String[].class);
      Assert.assertEquals(2, headers.length);

      JSONArray rows = res.getJSONArray("rows");
      List<JSONObject> horoscopes = new ArrayList<>();
      for (int i = 0; i < rows.length(); i++) {
        JSONObject jsonobject = rows.getJSONObject(i);
        horoscopes.add(jsonobject);
      }
      Assert.assertEquals(12, horoscopes.size());
    } catch (JSONException e) {
      Assert.assertEquals(12, TableCommander.getDb().getTable("horoscopes").getNumRows());
    }
  }

  @Test
  public void cTestAPIHandleDeleteError() throws IOException, SQLException {
    // way of making post request from https://www.baeldung.com/httpurlconnection-post
    URL url = new URL("http://localhost:4567/delete");
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("POST");
    http.setDoOutput(true);
    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    http.setRequestProperty("Access-Control-Allow-Origin","*");
    String jsonInputString = "{'name': 'horoscopes', " +
        "'row': {'horoscope_id':'14','horoscope':'Dragon'}}";
    try(OutputStream os = con.getOutputStream()) {
      byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    } catch (Exception e) {
      Assert.fail();
    }

    try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = br.readLine()) != null) {
        response.append(responseLine.trim());
      }
      JSONObject res = new JSONObject(response.toString());
      String head = res.getString("headers");
      String[] headers = new Gson().fromJson(head, String[].class);
      Assert.assertEquals(2, headers.length);

      JSONArray rows = res.getJSONArray("rows");
      List<JSONObject> horoscopes = new ArrayList<>();
      for (int i = 0; i < rows.length(); i++) {
        JSONObject jsonobject = rows.getJSONObject(i);
        horoscopes.add(jsonobject);
      }
      Assert.assertEquals(12, horoscopes.size());
    } catch (JSONException e) {
      Assert.assertEquals(12, TableCommander.getDb().getTable("horoscopes").getNumRows());
    }
  }
}
