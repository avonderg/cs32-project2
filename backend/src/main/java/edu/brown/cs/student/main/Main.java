package edu.brown.cs.student.main;

// look into using these imports for your REPL!

import java.io.IOException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
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
  }
}
