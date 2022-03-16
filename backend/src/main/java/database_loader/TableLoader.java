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
import java.util.concurrent.ExecutionException;

public class TableLoader {
  private static Connection conn = null;

  public TableLoader(String filename)
      throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
  }

  public static ResultSet runCommand(String command) throws SQLException, IllegalStateException {
    if (conn == null) {
      throw new IllegalStateException("ERROR: Cannot prepare statement before db is loaded.");
    }
    PreparedStatement ps = conn.prepareStatement(command);
    if (ps.execute()) {
      return ps.getResultSet();
    }
    return null;
  }

  public static Set<String> getTableNames()
      throws SQLException, IllegalStateException {

    ResultSet dbRes = runCommand("SELECT tbl_name FROM sqlite_master;");

    Set<String> tableNames = new HashSet<>();
    while (dbRes.next()) {
      tableNames.add(dbRes.getString(1));
    }

    return tableNames;
  }

  public static Table getTable(String tableName)
      throws SQLException, IllegalStateException, IllegalArgumentException {
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
