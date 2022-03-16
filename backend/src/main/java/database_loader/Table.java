package database_loader;

import java.util.List;
import java.util.Map;

public class Table {
  private String name;
  private List<String> headers;
  private List<Map<String, String>> rows;

  public Table(String name, List<String> headers, List<Map<String, String>> rows) {
    this.name = name;
    this.headers = headers;
    this.rows = rows;
  }
}
