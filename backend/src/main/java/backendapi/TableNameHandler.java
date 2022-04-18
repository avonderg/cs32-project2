package backendapi;

import com.google.gson.Gson;
import databaseloader.TableCommander;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;

/**
 * Class that handles any GET request to the /tableNames endpoint.
 * @author Suraj Anand
 */
public class TableNameHandler implements Route {
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
      return GSON.toJson(TableCommander.getDb().getTableNames());
      // returns table names
    } catch (IllegalStateException | SQLException e) {
      return GSON.toJson(e.getMessage());
      // returns error
    }
  }
}
