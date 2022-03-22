package databaseloader;

import client.ApiClient;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TableTest {

  @Test
  public void getTables() throws SQLException {

    String[] loadDb = new String[]{"load_db", "../data/horoscopes.sqlite3"};
    TableCommander tableCommander = new TableCommander();
    tableCommander.handleCommand(loadDb);
    Table horoscopes = TableCommander.getDb().getTable("horoscopes");
    assertEquals(2, horoscopes.getHeaders().size());

    Table tas = TableCommander.getDb().getTable("tas");
    assertEquals(3, tas.getHeaders().size());
  }
}
