package databaseloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that connects to a SQL Database file and handles all related SQL queries.
 * @author Suraj Anand
 */
public class TableLoader {
  private Connection conn = null;

  /**
   * Instantiates a connection to the specified SQL Database File.
   * @param filename name of the .sqlite3 file
   * @throws SQLException happens if DriverManager cannot connect to specified urlToDB
   * @throws ClassNotFoundException happens if org.sqlite.JDBC is not a class (not sure when)
   */
  public TableLoader(String filename)
      throws SQLException, ClassNotFoundException {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.err.println("ERROR: System error, sqlite class doesn't exist");
      System.exit(1);
    }
    // got this error message from will guo

    if (filename == null || !filename.endsWith(".sqlite3")) {
      throw new IllegalArgumentException("ERROR: Cannot connect to database.");
    }
    if (!(new File(filename)).exists()) {
      throw new IllegalArgumentException("ERROR: File does not exist.");
    }

    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);

    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Runs a specified SQL query on the database connected to in the constructor.
   * @param command SQL query (should be proper syntax of SQL used, think mysql)
   * @return either the result set of the command or null if command does not return anything
   * @throws SQLException if error when preparing statement or executing command
   * @throws IllegalStateException if connection is null
   */
  public ResultSet runCommand(String command) throws SQLException, IllegalStateException {
    if (conn == null) {
      throw new IllegalStateException("ERROR: Cannot run command on a null connection.");
    }
    PreparedStatement ps = conn.prepareStatement(command);
    if (ps.execute()) {
      return ps.getResultSet();
    }
    return null;
  }

  private boolean checkValidTable(String tableName)
      throws SQLException, IllegalArgumentException {
    if (tableName == null) {
      throw new IllegalArgumentException("ERROR: tableName cannot be empty");
    }

    // Check the requested table exists.
    if (!getTableNames().contains(tableName)) {
      throw new IllegalArgumentException("ERROR: Table \"" + tableName + "\" does not exist.");
    }

    return true;
  }

  /**
   * @return a set of strings representing the table names.
   * @throws SQLException
   * @throws IllegalStateException
   */
  public Set<String> getTableNames()
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
  public Table getTable(String tableName)
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

  /**
   * Inserts a new row containing data into the table.
   * @param tableName
   * @param dataValues
   * @throws SQLException
   * @throws IllegalArgumentException
   */
  public void addData(String tableName, Map<String, String> dataValues)
      throws SQLException, IllegalArgumentException {
    if (checkValidTable(tableName)) {
      // creating arguments for INPUT INTO sql statement
      StringBuilder columns = new StringBuilder("(");
      StringBuilder values = new StringBuilder("(");

      for (Map.Entry<String, String> dataPair : dataValues.entrySet()) {
        String colName = dataPair.getKey();
        String value = dataPair.getValue();
        columns.append(colName).append(", ");
        values.append("'").append(value).append("'").append(", ");
      }
      // formatting end of inputs
      System.out.println(columns);
      System.out.println(values);
      columns.delete(columns.length() - 2, columns.length());
      columns.append(")");
      values.delete(values.length() - 2, values.length());
      values.append(")");

      String colsToInsert = columns.toString();
      String valsToInsert = values.toString();
      System.out.println(colsToInsert);
      System.out.println(valsToInsert);

      // Prepare a statement to add the data to the table.
      Statement stmt = conn.createStatement();
      String insertQuery = "INSERT INTO " + tableName + " "
          + colsToInsert + " VALUES " + valsToInsert;
      System.out.println(insertQuery);
      stmt.executeUpdate(insertQuery);
      System.out.println("Successfully inserted into database");
    }
  }

  public void deleteRow(String tableName, JSONObject row)
      throws SQLException, IllegalArgumentException {
    if (checkValidTable(tableName)) {
      // Prepare a statement to delete the row from the table.
      try {
        StringBuilder deleteQuery = new StringBuilder();
        Iterator keys = row.keys();
        while (keys.hasNext()) {
          String currKey = (String) keys.next();
          String val = row.getString(currKey);
          String colEq = currKey +  " == " + "'" + val + "'" + " AND ";
          deleteQuery.append(colEq);
        }
        deleteQuery.delete(deleteQuery.length() - 4, deleteQuery.length());
        String delQuery = deleteQuery.toString();
        System.out.println("DELETE FROM " + tableName + " WHERE " + delQuery);

        PreparedStatement deletion = conn.prepareStatement(
            "DELETE FROM " + tableName + " WHERE " + delQuery);
        deletion.executeUpdate();
        System.out.println("Successfully deleted from database");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("ERROR: TABLE INVALID");
    }
  }

  public void updateRow(String tableName, JSONObject row,
                               Map<String, String> dataValues)
      throws SQLException, IllegalArgumentException {
    if (checkValidTable(tableName)) {
      try {
//        String primaryKey = this.getPrimaryKey(tableName);
//        String id = row.getString(primaryKey);
        StringBuilder updateQuery = new StringBuilder();
        Iterator keys = row.keys();
        while (keys.hasNext()) {
          String currKey = (String) keys.next();
          String val = row.getString(currKey);
          String colEq = currKey +  " == " + "'" + val + "'" + " AND ";
          updateQuery.append(colEq);
        }
        updateQuery.delete(updateQuery.length() - 4, updateQuery.length());
        String modQuery = updateQuery.toString();

        StringBuilder setQuery = new StringBuilder();
        for (Map.Entry<String, String> dataPair : dataValues.entrySet()) {
          String colName = dataPair.getKey();
          String val = dataPair.getValue();
          String setPair = colName + " = " + "'" + val + "',";
          setQuery.append(setPair);
        }
        setQuery.deleteCharAt(setQuery.length() - 1);
        String setAsString = setQuery.toString();
        System.out.println(setAsString);

        PreparedStatement update = conn.prepareStatement(
            "UPDATE " + tableName + " SET " + setAsString + " WHERE " + modQuery
        );
        update.executeUpdate();
        System.out.println("Successfully updated database");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Closes a SQL connection (when not using this db anymore).
   * @throws SQLException if cannot connect to db
   */
  public void closeConnection() throws SQLException {
    if (conn == null) {
      return;
    }
    if (!conn.isClosed()) {
      conn.close();
    }
    conn = null;
  }
}
