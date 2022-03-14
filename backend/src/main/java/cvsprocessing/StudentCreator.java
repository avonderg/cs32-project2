package cvsprocessing;

import java.util.List;

/**
 * Class responsible for creating the student using the student's constructor. The studentCreator
 * class implements the objectCreator interface and therefore also implements the method
 * createObject where the student is created!
 * @author Onaphade
 */
public class StudentCreator implements ObjectCreator<Student> {
  private Student student;

  /**
   * Creates student using the characters provided. Headers are not needed in this case, but are
   * passed in for other versions of ObjectCreator implementation.
   * @param headers of the CSV
   * @param studentChars of each student to be created.
   * @return the newly created student
   */
  public Student createObject(List<String> headers, String[] studentChars) {
    if (headers.size() != studentChars.length) {
      System.out.println("ERROR: row size does not match headers");
      return null;
    }
    student = new Student(studentChars);
    return student;
  }

}
