package database;

import junit.framework.TestCase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class to test DatabaseProxy.
 */
public class DatabaseProxyTest extends TestCase {

  /**
   * Test the runCommand which invokes all other functions.
   */
  public void testRunCommand() {
//    DatabaseProxy data = null;
//    try {
//      // Replace second arg
//      Map<String, Permission> dataPermissions = new HashMap<>();
//      dataPermissions.put("INTERESTS", Permission.RW);
//      dataPermissions.put("NAMES", Permission.W);
//      dataPermissions.put("SKILLS", Permission.R);
//      dataPermissions.put("TRAITS", Permission.R);
//      data = new DatabaseProxy("data/dbFiles/test_data.sqlite3", dataPermissions);
//    } catch (SQLException e) {
//      System.out.println("ERROR: Could not connect to database");
//    } catch (ClassNotFoundException e) {
//      System.out.println("ERROR: Class not found??");
//    }
//    try {
//      assertEquals(null, data.runCommand("SELECT * FROM NAMES"));
//      assertEquals(0, data.getCache().size());
//      assertTrue(data.runCommand("SELECT * FROM INTERESTS").getString(1).equals("40"));
//      assertTrue(data.runCommand("SELECT * FROM SKILLS").getString(1).equals("17"));
//      assertEquals(2, data.getCache().size());
//      assertEquals(data.runCommand("SELECT * FROM SKILLS").getString(2).equals("algorithms"), true);
//      assertEquals(2, data.getCache().size());
//    } catch (ExecutionException e) {
//      e.printStackTrace();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    DatabaseProxy horoscopes = null;
//    try {
//      // Replace second arg
//      Map<String, Permission> horoscopePermissions = new HashMap<>();
//      horoscopePermissions.put("HOROSCOPES", Permission.R);
//      horoscopePermissions.put("SQLITE_SEQUENCE", Permission.RW);
//      horoscopePermissions.put("TA_HOROSCOPE", Permission.RW);
//      horoscopePermissions.put("TAS", Permission.RW);
//      horoscopes = new DatabaseProxy("data/dbFiles/horoscopes.sqlite3", horoscopePermissions);
//    } catch (SQLException e) {
//      System.out.println("ERROR: Could not connect to database");
//    } catch (ClassNotFoundException e) {
//      System.out.println("ERROR: Class not found??");
//    }
//
//    try {
//      assertEquals(null, horoscopes.runCommand("DELETE * FROM HOROSCOPES"));
//      assertEquals(0, horoscopes.getCache().size());
//      assertTrue(horoscopes.runCommand("SELECT * FROM HOROSCOPES").getString(2).equals("Aries"));
//      assertEquals(1, horoscopes.getCache().size());
//      assertTrue(horoscopes.runCommand("SELECT * FROM TA_HOROSCOPE").getString(2).equals("9"));
//      assertEquals(2, horoscopes.getCache().size());
//      assertEquals(horoscopes.runCommand("UPDATE TAS SET ROLE='MEME' WHERE NAME='Tim'"), null);
//      assertEquals(0, horoscopes.getCache().size());
//      assertTrue(horoscopes.runCommand("SELECT * FROM TAS").getString(3).equals("MEME"));
//      assertEquals(1, horoscopes.getCache().size());
//      assertEquals(horoscopes.runCommand("UPDATE TAS SET ROLE='Professor' WHERE NAME='Tim'"), null);
//      assertEquals(0, horoscopes.getCache().size());
//    } catch (ExecutionException e) {
//      e.printStackTrace();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
  }
}