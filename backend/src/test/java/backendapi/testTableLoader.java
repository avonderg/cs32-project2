package backendapi;

import client.ApiClient;
import com.google.gson.Gson;
import databaseloader.Table;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class testTableLoader {
  private static final Gson GSON = new Gson();
  @Test
  public void testLoad() {
    ApiClient client = new ApiClient();
    TableCommander commander = new TableCommander();
    commander.handleCommand(new String[]{"load_db", "../data/horoscopes.sqlite3"});
    try {
      Table horoscopeTable = TableCommander.getDb().getTable("horoscopes");
      assertEquals(2, horoscopeTable.getHeaders().size());

//      Map<String, String> dataValues = new HashMap<>();
//      dataValues.put("name", "Tim");
//      dataValues.put("id", "1");
//      dataValues.put("role", "Professor");
//      TableCommander.getDb().addData("tas", dataValues);

      Table taTable = TableCommander.getDb().getTable("tas");
      taTable.printTable();
      assertEquals(3, taTable.getHeaders().size());
      int tableLength = taTable.getNumRows();
      System.out.println(tableLength);

      String str = "{\"name\":\"Tim\",\"id\":\"1\",\"role\":\"Professor\"}";
      JSONObject json = new JSONObject(str);

      TableCommander.getDb().deleteRow("tas", json);
      Table deleted = TableCommander.getDb().getTable("tas");
      deleted.printTable();
      System.out.println(deleted.getNumRows());
      assertEquals(tableLength-1, deleted.getNumRows());

      int deletedLength = deleted.getNumRows();

      Map<String, String> dataValues = new HashMap<>();
      dataValues.put("name", "Tim");
      dataValues.put("id", "1");
      dataValues.put("role", "UTA");
      TableCommander.getDb().addData("tas", dataValues);
      Table added = TableCommander.getDb().getTable("tas");
      assertEquals(deletedLength+1, added.getNumRows());

      String timTa = "{\"name\":\"Tim\",\"id\":\"1\",\"role\":\"UTA\"}";
      JSONObject timJson = new JSONObject(timTa);
      Map<String, String> updateVals = new HashMap<>();
      updateVals.put("role", "Professor");
      TableCommander.getDb().updateRow("tas", timJson, updateVals);
      Table updated = TableCommander.getDb().getTable("tas");
      assertEquals(added.getNumRows(), updated.getNumRows());

//      String tableAsStr = GSON.toJson(TableCommander.getDb().getTable("tas"));
//      JSONObject tableJson = new JSONObject(tableAsStr);
//      String newId = tableJson.getString("id");
//      assertEquals("20", newId);
    } catch (SQLException | JSONException e) {
      System.out.println("ERROR: TEST FAILED");
      Assert.fail();
    }
  }
}
