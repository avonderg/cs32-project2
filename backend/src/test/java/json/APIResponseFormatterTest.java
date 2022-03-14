package json;

import cvsprocessing.Student;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class APIResponseFormatterTest {

  APIResponseFormatter formatter;

  @Test
  public void getEndpointsTest() {
    formatter = new APIResponseFormatter();
    List<String> endPoints =
        formatter.getEndpoints("[\"/info-four\", \"/info-five\", \"/info-six\"]");
    assertEquals("/info-four", endPoints.get(0));
    assertEquals("/info-five", endPoints.get(1));
    assertEquals("/info-six", endPoints.get(2));
  }

  @Test
  public void getStudentsTest() {
    formatter = new APIResponseFormatter();
    List<Student> students = formatter.parseJsonList(
        "[{\"id\": 1, \"name\": \"Petr Dillingstone\", \"class_year\": \"sophomore\","
            + " \"years_experience\": 8, \"communication_style\": \"email\", "
            + "\"weekly_avail_hours\": 13, \"meeting_style\": \"in person\", "
            + "\"meeting_time\": \"afternoon\"}]");

    assertEquals(1, students.size());
    assertEquals(1, students.get(0).getID());
    assertEquals("email", students.get(0).getCommunicationStyle());
  }

  @Test
  public void getMessageTest() {
    formatter = new APIResponseFormatter();
    Message errorMessage = formatter.getMessage("{\"message\": "
        + "\"This endpoint is inactive due to maintenance.\"}");

    assertEquals("This endpoint is inactive due to maintenance.",
        errorMessage.getMessage());
  }
}