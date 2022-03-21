package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

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
    JSONObject values = null;

    try {
      values = new JSONObject(req.body());
      tableName = values.getString("name");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    // TODO: Handle errors on the frontend
    try {
      TableCommander
      return GSON.toJson(TableCommander.db.getTable(tableName));
      // returns table
    } catch (IllegalArgumentException e) {
      return GSON.toJson(e.getMessage());
      // returns error message
    } catch (SQLException e) {
      return GSON.toJson(e.getMessage());
      // returns error message
    }
  }
}
