package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

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
      values = new JSONObject(req.body());
      tableName = values.getString("name");
      data = values.getJSONObject("row");
    } catch (JSONException e) {
      e.printStackTrace();
    }

//     TODO: Handle errors on the frontend
    try {
      TableCommander.getDb().deleteRow(tableName, data);
      return GSON.toJson(TableCommander.getDb().getTable(tableName));
      // returns table
    } catch (IllegalArgumentException | SQLException e) {
      try {
        return GSON.toJson(TableCommander.getDb().getTable(tableName));
      } catch (IllegalArgumentException | SQLException err) {
        return GSON.toJson(err.getMessage());
      }
    }
  }
}
