package cvsprocessing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StudentCreatorTest {

  private StudentCreator creator;

  @Test
  public void testStudentCreator() {
    creator = new StudentCreator();
    List<String> headers = new ArrayList<String>();
    String[] head = {"id", "name", "email", "gender", "class_year", "ssn", "nationality", "race",
        "years_experience", "communication_style", "weekly_avail_hours", "meeting_style",
        "meeting_time", "software_engn_confidence", "strengths", "weaknesses", "skills,interests"};
    for (String h: head) {
      headers.add(h);
    }
    headers.add("id");
    String[] chars =
        {"1", "Stanton Swalough", "sswalough0@ask.com", "Female", "junior", "375 - 75 - 3870",
            "Russia", "American Indian or Alaska Native", "18", "email", "2", "in person",
            "morning", "2",
            "quick learner, prepared, team player, early starter, friendly",
            "cutthroat, unfriendly, late", "OOP",
            "mathematics, film/photography, politics"};
    Student student = creator.createObject(headers, chars);

    assertEquals(student.getID(), 1);
    System.out.println(student.getSkills()[0]);
  }

}
