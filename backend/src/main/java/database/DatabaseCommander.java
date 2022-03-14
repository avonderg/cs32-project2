package database;

import edu.brown.cs.student.main.CommandAcceptor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class to run database commands.
 */
public class DatabaseCommander implements CommandAcceptor {
  private DatabaseProxy db = null;

  /**
   * function that handles commands associated with the database.
   * @param input from cmd line.
   */
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "load_db_data":
        try {
          // Replace second arg
          Map<String, Permission> dataPermissions = new HashMap<>();
          dataPermissions.put("INTERESTS", Permission.RW);
          dataPermissions.put("NAMES", Permission.W);
          dataPermissions.put("SKILLS", Permission.R);
          dataPermissions.put("TRAITS", Permission.R);
          db = new DatabaseProxy("data/dbFiles/test_data.sqlite3", dataPermissions);
        } catch (SQLException e) {
          System.out.println("ERROR: Could not connect to database");
        } catch (ClassNotFoundException e) {
          System.out.println("ERROR: Class not found??");
        }
        break;
      case "load_db_horoscopes":
        try {
          Map<String, Permission> horoscopePermissions = new HashMap<>();
          horoscopePermissions.put("HOROSCOPES", Permission.R);
          horoscopePermissions.put("SQLITE_SEQUENCE", Permission.RW);
          horoscopePermissions.put("TA_HOROSCOPE", Permission.RW);
          horoscopePermissions.put("TAS", Permission.RW);
          db = new DatabaseProxy("data/dbFiles/horoscopes.sqlite3", horoscopePermissions);
        } catch (SQLException e) {
          System.out.println("ERROR: Could not connect to database");
        } catch (ClassNotFoundException e) {
          System.out.println("ERROR: Class not found??");
        }
        break;
      case "load_db_zoo":
        try {
          // Replace second arg
          Map<String, Permission> zooPermissions = new HashMap<>();
          zooPermissions.put("ZOO", Permission.R);
          db = new DatabaseProxy("data/dbFiles/zoo.sqlite3", zooPermissions);
        } catch (SQLException e) {
          System.out.println("ERROR: Could not connect to database");
        } catch (ClassNotFoundException e) {
          System.out.println("ERROR: Class not found??");
        }
        break;
      case "query":
        ResultSet results = null;
        if (db == null) {
          System.out.println("ERROR: You need to instantiate a database");
          break;
        }
        try {
          String query = "";
          switch (input[1]) {
            case "0":
              query = "SELECT * FROM NAMES";
              break;
            case "1":
              query = "INSERT INTO NAMES (ID, NAME, EMAIL) VALUES (99, 'JOSH', 'JOSH@LOL.com')";
              break;
            case "2":
              query = "UPDATE NAMES SET ID= '12' WHERE NAME = 'JOSH'";
              break;
            case "3":
              query = "SELECT * FROM INTERESTS JOIN SKILLS ON SKILLS.ID=INTERESTS.ID";
              break;
            case "4":
              query = "SELECT * FROM INTERESTS LIMIT 0, 30";
              break;
            case "5":
              query = "UPDATE INTERESTS SET INTEREST='LOL' WHERE ID=40";
              break;
            case "6":
              query = "UPDATE INTERESTS SET INTEREST='Not FUNNY' WHERE ID=40";
              break;
            case "7":
              query = "UPDATE 'TAS' SET ROLE='MEME' WHERE NAME='Tim'";
              break;
            case "8":
              query = "SELECT * FROM ZOO";
              break;
            default:
              break;
          }
          results = db.runCommand(query);
        } catch (SQLException e) {
          System.out.println("ERROR: Invalid Query");
          break;
        } catch (ExecutionException e) {
          System.out.println("error with cache");
        }
        try {
          if (results != null) {
            printResults(results);
          }
        } catch (SQLException e) {
          System.out.println("ERROR: Trouble printing results");
        }
        break;
      default:
        System.out.println("ERROR: Did not match expected input");
        break;
    }
  }

  /**
   * Method to print the result set.
   * @param results a ResultSet from a query.
   * @throws SQLException if the results do not have metadata.
   */
  public void printResults(ResultSet results) throws SQLException {
    ResultSetMetaData rsmd = results.getMetaData();
    // logic from stackOverflow
    int columnsNumber = rsmd.getColumnCount();
    while (results.next()) {
      for (int i = 1; i <= columnsNumber; i++) {
        if (i > 1) {
          System.out.print(", ");
        }
        String columnValue = results.getString(i);
        System.out.print(columnValue + " ");
      }
      System.out.println();
    }
  }
}
