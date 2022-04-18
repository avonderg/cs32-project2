package backendapi;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

public class DBNameHandler implements Route {

  private static List<String> dbNames;
  public DBNameHandler(List<String> names) {
    dbNames = names;
  }
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
    try {
      return GSON.toJson(dbNames);
      // returns table names
    } catch (IllegalStateException e) {
      return GSON.toJson(e.getMessage());
      // returns error
    }
  }
}
