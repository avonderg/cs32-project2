package json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cvsprocessing.Student;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Class that uses Google Gson to parse
 * String body of Http Response into objects.
 * @author Suraj Anand
 */
public class APIResponseFormatter implements JSONParseable<Student> {

  /**
   * Method to get the active endpoints from a json list of strings.
   * @param jsonEndPoints json list of strings
   * @return list of strings that has the endpoints
   */
  public List<String> getEndpoints(String jsonEndPoints) {
    Gson parser = new Gson();

    Type endPointList = new TypeToken<List<String>>() {
    }.getType();
    //logic from https://stackoverflow.com/questions/16518252/different-json-array-response

    List<String> endpoints = (List<String>) parser.fromJson(jsonEndPoints, endPointList);

    return endpoints;
  }

  /**
   * Gets the list of students from the String input.
   * @param jsonObjects String representing json
   * @return List of Students contained in jsonObjects
   */
  public List<Student> parseJsonList(String jsonObjects) {
    Gson parser = new Gson();

    Type studentList = new TypeToken<List<Student>>() {
    }.getType();
    //logic from https://stackoverflow.com/questions/16518252/different-json-array-response

    List<Student> students = parser.fromJson(jsonObjects, studentList);

    return students;
  }

  /**
   * Gets the message object from String.
   * @param jsonMessage String representing json
   * @return message contained in jsonMessage
   */
  public Message getMessage(String jsonMessage) {
    Gson parser = new Gson();
    return parser.fromJson(jsonMessage, Message.class);
  }
}
