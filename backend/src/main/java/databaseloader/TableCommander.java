package databaseloader;


import database.DatabaseCommander;
import edu.brown.cs.student.main.CommandAcceptor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableCommander implements CommandAcceptor {
  private static Connection conn = null;
  public static TableLoader db = null;

  /**
   * function that handles commands associated with the database.
   * @param input from cmd line.
   */
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "load_db":
        if (input.length != 2) {
          System.out.println("ERROR: Input must be of length two");
          return;
        }
        try {
          db = new TableLoader(input[1]);
          System.out.println(db.getTableNames());
        } catch (SQLException e) {
          System.out.println("ERROR: Could not connect to database");
        } catch (ClassNotFoundException e) {
          System.out.println("ERROR: Class not found??");
        }
        if (db != null) {
          System.out.println("Loaded database from file " + input[1]);
        }
        break;
      case "modify":
        if (db == null) {
          System.out.println("ERROR: You need to instantiate a database");
          break;
        }
        // TODO: SAVED THIS FOR YOU NEIL <3
        break;
    }
  }

  /**
   * Method to print the result set.
   * @param results a ResultSet from a query.
   * @throws SQLException if the results do not have metadata.
   */
  public void printResults(ResultSet results) throws SQLException {
    DatabaseCommander.printResults(results);
  }
}
