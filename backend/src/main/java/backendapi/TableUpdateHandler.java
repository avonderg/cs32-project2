package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * class that implements the Route interface to handle updating a table.
 * @author neilxu
 */
public class TableUpdateHandler implements Route {
  private static final Gson GSON = new Gson();

  /**
   * Handles a request to spark backend server and this route.
   * @param req spark request (handled in typescript)
   * @param res spark response (handled in typescript)
   * @return String representing JSON object
   */
  @Override
  public String handle(Request req, Response res) {
    String tableName = "";
    String columns;
    String dataValues;
    Map<String, String> dataMap = new HashMap<>();
    JSONObject values;
    JSONObject data = null;

    try {
      values = new JSONObject(req.body());
      data = values.getJSONObject("row");
      tableName = values.getString("name");
      JSONArray colsToAddJSON = values.getJSONArray("columns");
      JSONArray valsToAddJSON = values.getJSONArray("values");
      String[] colsToAdd = new String[colsToAddJSON.length()];
      for (int i = 0; i < colsToAdd.length; i++) {
        colsToAdd[i] = colsToAddJSON.optString(i);
      }

      String[] valsToAdd = new String[valsToAddJSON.length()];
      for (int i = 0; i < valsToAdd.length; i++) {
        valsToAdd[i] = valsToAddJSON.optString(i);
      }

//      columns = values.getString("columns");
      // splitting and trimming from https://stackoverflow.com/questions/41953388/java-split-and-trim-in-one-shot
//      String[] colsToAdd = Arrays.stream(columns.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
//          .map(String::trim).toArray(String[]::new);
//      dataValues = values.getString("values");
//      String[] valsToAdd = Arrays.stream(dataValues.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)",-1))
//          .map(String::trim).toArray(String[]::new);
      if (colsToAdd.length != valsToAdd.length) {
        return "ERROR: Column and value lengths do not match";
      }
      // creating a map of columns to values to update
      for (int i = 0; i < colsToAdd.length; i++) {
        dataMap.put(colsToAdd[i], valsToAdd[i]);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    // create response type, wrap it. have the status code and the table
    try {
      // updates the table
      TableCommander.getDb().updateRow(tableName, data, dataMap);
      return GSON.toJson(TableCommander.getDb().getTable(tableName, "1"));
    } catch (IllegalArgumentException | SQLException e) {
      try {
        return GSON.toJson(TableCommander.getDb().getTable(tableName, "1"));
      } catch (IllegalArgumentException | SQLException err) {
        return GSON.toJson(err.getMessage());
      }
    }
  }
}
