package databaseloader;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

/**
 * Class that represents a SQL Table.
 * @author Suraj Anand
 */
public class Table {
  private String name;
  private List<String> headers;
  private List<Map<String, String>> rows;

  /**
   * Constructor for Table Class.
   * @param name SQL Table name
   * @param headers headers for columns in the rows
   * @param rows a list of rows where each row is a map of header to value
   */
  public Table(String name, List<String> headers, List<Map<String, String>> rows) {
    this.name = name;
    this.headers = headers;
    this.rows = rows;
  }

  /**
   * Gets the headers of the table
   * @return
   */
  public List<String> getHeaders() {
    return ImmutableList.copyOf(headers);
    // defensive immutable proxy -- should check that works lmao
  }
}
