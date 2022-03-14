package database;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class for database proxy, allows queries and to load in a database, implements caching.
 */
public class DatabaseProxy {
  private static final Map<String, Permission> PERMISSIONS_HASH_MAP;
  private LoadingCache<String, ResultSet> cache = CacheBuilder.newBuilder().maximumSize(5).build(
      new CacheLoader<>() {
        public ResultSet load(String s) throws SQLException, ExecutionException {
          return runCommand(s);
        }
      }
      );
  private Map<String, Permission> tablePermissions;
  private static Connection conn = null;
  // Variable from abao5
  static {
    PERMISSIONS_HASH_MAP = new HashMap<>();
    PERMISSIONS_HASH_MAP.put("SELECT", Permission.R);
    PERMISSIONS_HASH_MAP.put("INSERT", Permission.W);
    PERMISSIONS_HASH_MAP.put("DROP", Permission.R);
    PERMISSIONS_HASH_MAP.put("UPDATE", Permission.RW);
    PERMISSIONS_HASH_MAP.put("DELETE", Permission.RW);
    PERMISSIONS_HASH_MAP.put("ALTER", Permission.RW);
    PERMISSIONS_HASH_MAP.put("JOIN", Permission.R);
    PERMISSIONS_HASH_MAP.put("TRUNCATE", Permission.R);
  }

  /**
   * Simple getter for the cache, for testing purposes.
   * @return the cache associated with the class.
   */
  public LoadingCache<String, ResultSet> getCache() {
    return cache;
  }

  /**
   * Constructor for the database proxy.
   * @param filename input String, the name of the file
   * @param tablePermissions input Map, the tables and the permissions associated with each.
   * @throws SQLException exception thrown if the connection cannot be made
   * @throws ClassNotFoundException exception thrown if the class to connect to databases can't be
   *                                found.
   */
  public DatabaseProxy(String filename, Map<String, Permission> tablePermissions)
      throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
    conn = DriverManager.getConnection(urlToDB);
    this.tablePermissions = tablePermissions;
  }

  /**
   * Method to check the permissions of the query and the table and see if they match.
   * @param query String representing the SQL query.
   * @return a boolean, representing whether the sql query can be executed or not.
   */
  private Boolean checkPermissions(String query) {
    String[] queryBrokenUp = query.split(" ");
    Permission currPerm = null;
    for (String word : queryBrokenUp) {
      if (PERMISSIONS_HASH_MAP.containsKey(word)) {
        currPerm = PERMISSIONS_HASH_MAP.get(word);
      } else if (tablePermissions.containsKey(word)) {
        if (currPerm != tablePermissions.get(word) && tablePermissions.get(word) != Permission.RW) {
          return false;
        } else {
          currPerm = null;
        }
      }
    }
    return true;
  }

  /**
   * Method to run an SQL command.
   * @param command String representing the SQL command
   * @return a ResultSet if the command is a query, null if the command updates or deletes.
   * @throws SQLException if the command cannot be executed.
   * @throws ExecutionException if the cache lookup fails.
   */
  public ResultSet runCommand(String command) throws SQLException, ExecutionException {
    String commandUpper = command.toUpperCase();
    Boolean executable = checkPermissions(commandUpper);
    if (!executable) {
      System.out.println("ERROR: Do not have the necessary permissions");
      return null;
    }
    ResultSet returnValue = cache.getIfPresent(command);
    if (returnValue != null) {
      return returnValue;
    }
    PreparedStatement ps = conn.prepareStatement(command);
    if (ps.execute()) {
      cache.put(command, ps.getResultSet());
      return cache.get(command);
    } else {
      cache.invalidateAll();
    }
    return null;
  }
}
