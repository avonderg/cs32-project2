package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import databaseloader.TableLoader;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

public class DBLoadHandler implements Route {
  /**
   * Creates a new GSON to create Json String from Object.
   */
  private static final Gson GSON = new Gson();

  /**
   * Handles a request to spark backend server and this route.
   * @param req spark request (handled in typescript)
   * @param res spark response (handled in typescript)
   * @return String representing JSON object
   */
  @Override
  public String handle(Request req, Response res) {
    JSONObject values;
    String dbName;
    try {
      values = new JSONObject(req.body());
      dbName = values.getString("name");
      TableCommander.setDb(new TableLoader("../data/" + dbName + ".sqlite3"));
      return GSON.toJson("Successfully loaded database: " + dbName);
      // returns table names
    } catch (IllegalStateException | JSONException | SQLException | ClassNotFoundException e) {
      return GSON.toJson(e.getMessage());
      // returns error
    }
  }
}
