package database_loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that connects to a SQL Database file and handles all related SQL queries.
 * @author Suraj Anand
 */
public class TableLoader {
  private static Connection conn = null;

  /**
   * Instantiates a connection to the specified SQL Database File.
   * @param filename name of the .sqlite3 file
   * @throws SQLException happens if DriverManager cannot connect to specified urlToDB
   * @throws ClassNotFoundException happens if org.sqlite.JDBC is not a class (not sure when)
   */
  public TableLoader(String filename)
      throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Runs a specified SQL query on the database connected to in the constructor.
   * @param command SQL query (should be proper syntax of SQL used, think mysql)
   * @return either the result set of the command or null if command does not return anything
   * @throws SQLException if error when preparing statment or executing command
   * @throws IllegalStateException if connection is null
   */
  public static ResultSet runCommand(String command) throws SQLException, IllegalStateException {
    if (conn == null) {
      throw new IllegalStateException("ERROR: Cannot run command on a null connection.");
    }
    PreparedStatement ps = conn.prepareStatement(command);
    if (ps.execute()) {
      return ps.getResultSet();
    }
    return null;
  }

  /**
   * @return
   * @throws SQLException
   * @throws IllegalStateException
   */
  public static Set<String> getTableNames()
      throws SQLException, IllegalStateException {

    ResultSet dbRes = runCommand("SELECT tbl_name FROM sqlite_master;");

    Set<String> tableNames = new HashSet<>();
    while (dbRes.next()) {
      tableNames.add(dbRes.getString(1));
    }

    return tableNames;
  }

  /**
   * Command that gets all data for specified table in the connected database.
   * Credit: logic from stew2003
   * @param tableName specified table name
   * @return Table object representing data in this table
   * @throws SQLException if errors during SQL
   * @throws IllegalArgumentException if table name is null or table not exist
   */
  public static Table getTable(String tableName)
      throws SQLException, IllegalArgumentException {
    if (tableName == null) {
      throw new IllegalArgumentException("ERROR: Cannot get null table.");
    }

    // Check the requested table exists.
    if (!getTableNames().contains(tableName)) {
      throw new IllegalArgumentException("ERROR: Table \"" + tableName + "\" does not exist.");
    }

    // Prepare a statement to get everything from the table.
    ResultSet dbRes = runCommand("SELECT * FROM " + tableName + ";");
    ResultSetMetaData dbResMeta = dbRes.getMetaData();

    // Get the column headers
    int numCols = dbResMeta.getColumnCount();
    List<String> columnNames = new ArrayList<>();
    for (int i = 1; i <= numCols; i++) {
      columnNames.add(dbResMeta.getColumnName(i));
    }

    // Add each row of the db to a list
    List<Map<String, String>> rows = new ArrayList<>();
    while (dbRes.next()) {
      Map<String, String> curRow = new HashMap<>();
      for (int i = 1; i <= numCols; i++) {
        curRow.put(
            dbResMeta.getColumnName(i),
            dbRes.getString(i)
        );
      }
      rows.add(curRow);
    }
    return new Table(tableName, columnNames, rows);
  }
}
