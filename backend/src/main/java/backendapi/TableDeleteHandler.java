package backendapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
      return GSON.toJson("Successfully deleted from the database");
      // returns table
    } catch (IllegalArgumentException | SQLException e) {
      return GSON.toJson(e.getMessage());
      // returns error message
    }
  }
}
