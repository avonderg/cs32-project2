package backendapi;

import client.ApiClient;
import databaseloader.Table;
import databaseloader.TableCommander;
import org.junit.Assert;
import org.junit.Test;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class testTableLoader {

  public void testLoad() {
    ApiClient client = new ApiClient();
    TableCommander commander = new TableCommander();
    commander.handleCommand(new String[]{"load_db", "../data/horoscopes.sqlite3"});
    try {
      Table horoscopeTable = TableCommander.getDb().getTable("horoscopes");
      assertEquals(2, horoscopeTable.getHeaders().size());
      Table taTable = TableCommander.getDb().getTable("tas");
      assertEquals(3, taTable.getHeaders().size());
//
//      String
    } catch (SQLException e) {
      System.out.println("ERROR: TEST FAILED");
      Assert.fail();
    }
//    HttpRequest req = ;
//    HttpResponse<String> res =;
  }


}
