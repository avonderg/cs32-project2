package cvsprocessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CSVReaderTest {
  private CSVReader csvReader;


  /**
   * Tests the general functionality of the CSV reader for students
   */
  @Test
  public void testCSVReaderStudentGeneral() {
    try {
      csvReader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
    List<Student> students = csvReader.getObjects();
    assertEquals(students.size(), 20);
    assertEquals(students.get(0).getID(),1);
    assertEquals(students.get(0).getName(),"Stanton Swalough");
    assertEquals(students.get(19).getID(),20);
    assertEquals(students.get(5).getYearsExp(),7);
  }

  /**
   * Tests the general functionality of the CSV reader for stars
   */
  @Test
  public void testCSVReaderStarsGeneral() {
    try {
      csvReader = new CSVReader("data/onboarding/three-star.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
    List<Star> stars = csvReader.getObjects();
    assertEquals(stars.size(), 3);
    assertEquals(stars.get(0).getStarID(),1);
    assertEquals(stars.get(2).getStarID(),3);
    assertEquals(stars.get(0).getProperName(),"Star One");
    assertEquals(stars.get(1).getProperName(),"Star Two");
    assertTrue(stars.get(1).getX() == 2);
  }

  @Test
  public void testSplitLine(){
    try {
      csvReader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
    String[] result1 = csvReader.splitLine("Hello,My,Name,is,Om");
    String[] result2 = csvReader.splitLine("Hello,\"My, Name\",is,Om");

    assertEquals(result1[0], "Hello");
    assertEquals(result1[1], "My");
    assertEquals(result1[2], "Name");
    assertEquals(result1[3], "is");
    assertEquals(result1[4], "Om");

    assertEquals(result2[0], "Hello");
    assertEquals(result2[1], "\"My, Name\"");
    assertEquals(result2[2], "is");
    assertEquals(result2[3], "Om");
  }

  @Test
  public void testCheckLoc(){
    try {
      csvReader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
    boolean result1 = csvReader.checkLoc();
    assertTrue(result1);

    try {
      csvReader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
  }

  @Test
  public void testGetObjects(){
    try {
      csvReader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file");
      return;
    }
    List<Student> students = csvReader.getObjects();
    assertEquals(students.size(), 20);
  }



}
