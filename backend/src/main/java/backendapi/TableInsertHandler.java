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

public class TableInsertHandler implements Route {
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
    Map<String, String> dataMap = null;
    JSONObject values;

    try {
      values = new JSONObject(req.body());
      tableName = values.getString("name");
      JSONObject data = values.getJSONObject("data");
      // Converting jsonObject to a map from: https://tinyurl.com/5n6e9pn5
      dataMap = GSON.fromJson(data.toString(),
          new TypeToken<HashMap<String, String>>() { }.getType());
    } catch (JSONException e) {
      e.printStackTrace();
    }

//     TODO: Handle errors on the frontend
    try {
      TableCommander.getDb().addData(tableName, dataMap);
      return GSON.toJson("Successfully inserted data into the database");
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
