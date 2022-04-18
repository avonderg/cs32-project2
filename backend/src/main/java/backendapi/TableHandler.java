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
 * Class that handles any POST request to the /table endpoint.
 * @author Suraj Anand
 */
public class TableHandler implements Route {
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
    String sortCol = "";

    try {
      values = new JSONObject(req.body());
      tableName = values.getString("name");
      sortCol = values.getString("sortCol");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    try {
      return GSON.toJson(TableCommander.getDb().getTable(tableName, sortCol));
      // returns table
    } catch (IllegalArgumentException | SQLException e) {
      return GSON.toJson(e.getMessage());
      // returns error message
    }
  }
}
