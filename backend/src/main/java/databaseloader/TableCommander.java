package databaseloader;

import edu.brown.cs.student.main.CommandAcceptor;
import java.sql.SQLException;

/**
 * Class that handles the REPL commands for any table visualization related
 * db loading as well as testing.
 */
public class TableCommander implements CommandAcceptor {
  private static TableLoader db = null;

  /**
   * retrieves the TableLoader associated with this class.
   * @return the TableLoader field
   */
  public static TableLoader getDb() {
    return db;
  }
  /**
   * sets the TableLoader associated with this class.
   * @param newDb the TableLoader object we want to set
   */
  public static void setDb(TableLoader newDb) {
    db = newDb;
  }

  /**
   * function that handles commands associated with the database.
   * @param input from cmd line.
   */
  public void handleCommand(String[] input) {
    if ("load_db".equals(input[0])) {
      if (input.length != 2) {
        System.out.println("ERROR: Input must be of length two");
        return;
      }
      try {
        db = new TableLoader(input[1]);
      } catch (SQLException e) {
        System.out.println(e);
        System.out.println("ERROR: Could not connect to database");
      } catch (ClassNotFoundException e) {
        System.out.println("ERROR: Class not found??");
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }

      if (db != null) {
        System.out.println("Loaded database from file " + input[1]);
      }
    }
  }
}
