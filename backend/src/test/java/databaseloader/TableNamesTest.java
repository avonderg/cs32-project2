package databaseloader;

import client.ApiClient;
import client.ClientAuth;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import client.Method;
import cvsprocessing.Student;
import edu.brown.cs.student.main.Main;
import json.APIResponseFormatter;
import json.JSONParseable;
import junit.framework.TestCase;
import org.junit.Test;

import java.net.http.HttpRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TableNamesTest {

  @Test
  public void getTableNames() throws SQLException {
//    String[] loadDb = new String[]{"load_db", "../data/horoscopes.sqlite3"};
//    TableCommander tableCommander = new TableCommander();
//    tableCommander.handleCommand(loadDb);
//    Set<String> tableNames = TableCommander.getDb().getTableNames();
//    assertEquals(4, tableNames.size());
//
//    String[] loadDbOther = new String[]{"load_db", "../data/tas.sqlite3"};
//    tableCommander.handleCommand(loadDbOther);
//    Set<String> tableNamesOther = TableCommander.getDb().getTableNames();
//    assertEquals(4, tableNamesOther.size());
  }


}