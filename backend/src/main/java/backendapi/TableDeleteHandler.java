package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

/**
 * class that implements the Route interface to handle deletion from a table.
 * @author neilxu
 */
public class TableDeleteHandler implements Route {
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
    JSONObject data = null;
    JSONObject values;

    try {
      // getting the inputs to the deleteRow method
      values = new JSONObject(req.body());
      tableName = values.getString("name");
      data = values.getJSONObject("row");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    try {
      // deleting the row from the database
      TableCommander.getDb().deleteRow(tableName, data);
      return GSON.toJson(TableCommander.getDb().getTable(tableName, "1"));
      // returns table
    } catch (IllegalArgumentException | SQLException e) {
      try {
        System.out.println("ERROR: unable to delete from table");
        return GSON.toJson(TableCommander.getDb().getTable(tableName, "1"));
      } catch (IllegalArgumentException | SQLException err) {
        System.out.println("ERROR: unable to delete from table invalid name or db");
        return GSON.toJson(err.getMessage());
      }
    }
  }
}
