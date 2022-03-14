package bloomfilter;

import static org.junit.Assert.assertEquals;

import cvsprocessing.CSVReader;
import cvsprocessing.Student;
import cvsprocessing.StudentCreator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class BloomFilterDatabaseTest {
  private final int MAX_ELEMENTS_SET =36;
  List<Student> studentList;

  @Before
  public void createStudentList() {
    CSVReader<Student> reader = null;
    try {
       reader = new CSVReader("data/project1/proj1_small.csv", new StudentCreator());
    } catch (IOException e) {
      System.out.println("Error reading file testing");
    }
    studentList = reader.getObjects();
  }

  @Test
  public void createBloomFilterDatabaseTest() {
    BloomFilterDatabase<Student> studentDatabase = new BloomFilterDatabase(studentList);
    assertEquals(20, studentDatabase.getNumberBloomInsertables());
  }

  @Test
  public void kNeighborsBloomFilterDatabaseTest() {
    BloomFilterDatabase<Student> studentDatabase = new BloomFilterDatabase(studentList);
    BloomSimilarityMetric xNor = new BloomXNOR();
    BloomSimilarityMetric and = new BloomAND();

    List<Integer> allStudents = studentDatabase.kMostSimilar(25, 1, xNor);
    List<Integer> noStudents = studentDatabase.kMostSimilar(0, 1, and);
    assertEquals(19, allStudents.size());
    assertEquals(0, noStudents.size());
  }

  @Test
  public void xNorBloomFilterDatabaseTest() {
    BloomFilterDatabase<Student> studentDatabase = new BloomFilterDatabase(studentList);
    BloomSimilarityMetric xNor = new BloomXNOR();

    List<Integer> fiveSimilarStudents = studentDatabase.kMostSimilar(5, 1, xNor);
    assertEquals(11, fiveSimilarStudents.get(0).intValue());
    // 15 and 18 are tied so randomly switches 1 and 2 elements -- cannot deterministically test
    assertEquals(16, fiveSimilarStudents.get(3).intValue());
    assertEquals(3, fiveSimilarStudents.get(4).intValue());

    assertEquals(5, fiveSimilarStudents.size());
  }

  @Test
  public void andBloomFilterDatabaseTest() {
    BloomFilterDatabase<Student> studentDatabase = new BloomFilterDatabase(studentList);
    BloomSimilarityMetric and = new BloomAND();

    List<Integer> fiveSimilarStudents = studentDatabase.kMostSimilar(5, 1, and);
    // 15 and 18 are tied so randomly switches 1 and 2 elements -- cannot deterministically test
    assertEquals(11, fiveSimilarStudents.get(2).intValue());
    // 12 and 20 are tied so randomly switches 1 and 2 elements -- cannot deterministically test
    assertEquals(5, fiveSimilarStudents.size());
  }


}