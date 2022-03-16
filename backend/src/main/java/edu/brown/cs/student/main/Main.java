package edu.brown.cs.student.main;

// look into using these imports for your REPL!

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import database_loader.TableCommander;
import database_loader.TableLoader;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;


/**
 * The Main class of our project. This is where execution begins.
 */

public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;


  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   * @throws IOException for any running issues in the repl
   */
  public static void main(String[] args) throws IOException {
    new Main(args).run();
  }

  private String[] args;
  private Repl repl;

  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Runs the function.
   */
  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    runREPL();

  }

  /**
   * Runs read eval print loop.
   */
  private void runREPL() {
    repl = new Repl();
  }

  /**
   * Runs the spark server.
   * @param port port to run the server on
   */
  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");


    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    // Allows requests from any domain (i.e., any URL). This makes development
    // easier, but it’s not a good idea for deployment.
    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

    Spark.get("/table", new TableHandler());

    Spark.init();
  }

  /**
   * Handles requests for horoscope matching on an input
   *
   * @return GSON which contains the result of MatchMaker.makeMatches
   */
  private class TableHandler implements Route {
    private final Gson GSON = new Gson();

    @Override
    public String handle(Request req, Response res) {
      String tableName = req.queryParams("name");

      try {
        return GSON.toJson(TableCommander.db.getTable(tableName));
      } catch (IllegalStateException | IllegalArgumentException e) {
        return GSON.toJson(e.getMessage());
      } catch (SQLException e) {
        return GSON.toJson("ERROR: Some error while exectuing sql.");
      }
    }
  }
}
