package json;

import java.util.List;

/**
 * Interface that is used to indicate that a class contains methods
 * that help with parsing JSONs into a certain type of object.
 * @param <T> type that the parseJsonList outputs
 */
public interface JSONParseable<T> {

  /**
   * Parses string into list of objects T.
   * @param jsonList string representation of json that is a list of objects
   * @return list of objects type T
   */
  List<T> parseJsonList(String jsonList);

  /**
   * Gets the endpoints of the api into strings.
   * @param body string rep of json that is a list of endpoints
   * @return list of strings representing the endpoints
   */
  List<String> getEndpoints(String body);
}
