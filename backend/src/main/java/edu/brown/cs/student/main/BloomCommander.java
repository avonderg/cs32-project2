package edu.brown.cs.student.main;


import bloomfilter.BloomFilter;
import bloomfilter.BloomFilterDatabase;
import bloomfilter.BloomSimilarityMetric;
import bloomfilter.BloomXNOR;
import cvsprocessing.CSVReader;
import cvsprocessing.Student;
import cvsprocessing.StudentCreator;
import recommender.RecommenderCommander;

import java.io.IOException;
import java.util.List;

/**
 * Class which implements the CommandAcceptor interface which is responsible for handling all of the
 * bloom filter commands. The class contains a BloomFilter and BloomFilterDatabase which it uses in
 * order to implement the commands.
 * @author Onaphade
 * @author Sanand14
 * @param <T> Object that Bloomfilter is parameterized by
 */
public class BloomCommander<T> implements CommandAcceptor {

  private BloomFilter<T> filter;
  private BloomFilterDatabase studentDatabase;
  private RecommenderCommander headers;

  /**
   * Constructor of BloomCommander. Accepts a RecommenderCommander which contains a list of headers.
   * @param hdrs a RecommenderCommander which contains a list of headers.
   */
  public BloomCommander(RecommenderCommander hdrs) {
    headers = hdrs;
  }

  /**
   * Handles all of the commands that the bloom filter needs to implement.
   * @param input from the command line, of which the first element is the command (already parsed)
   */
  public void handleCommand(String[] input) {
    int ret;

    switch (input[0]) {
      case "create_bf":
        filter = createBloomFilter(input);
        if (filter != null) {
          System.out.println(filter);
        }
        break;
      case "insert_bf":
        ret = insertElement(input);
        if (ret == 0) {
          System.out.println(filter);
        }
        break;
      case "query_bf":
        ret = completeQuery(input);
        if (ret == 0) {
          System.out.println("\"" + input[1] + "\"" + " is definitely not in the set.");
        } else if (ret == 1) {
          System.out.println("\"" + input[1] + "\"" + " might be in the set.");
        }
        break;
      case "load_bf":
        studentDatabase = createBloomFilterStudentDatabase(input);
        if (studentDatabase != null) {
          System.out.println(
              "Read " + studentDatabase.getNumberBloomInsertables() + " students from " + input[1]);
        }
        break;
      case "similar_bf":
        List<Integer> similarStudents = similarKStudents(input);
        if (similarStudents != null) {
          for (Integer studentID : similarStudents) {
            System.out.println(studentID);
          }
        }
        break;
      default:
        System.out.println("ERROR: No such command");
        break;
    }

  }

  /**
   * Creates bloom filter.
   * @param input from cmd line.
   * @return
   */
  private BloomFilter createBloomFilter(String[] input) {
    double r;
    int n;

    if (input.length != 3) {
      System.out.println("ERROR: Wrong number of parameters");
      return null;
    }

    try {
      r = Double.parseDouble(input[1]);
      n = Integer.parseInt(input[2]);
      if (!(r <= 1 && r > 0)) {
        System.out.println("ERROR: 0 < r <= 1");
        return null;
      }
      if (n < 0) {
        System.out.println("ERROR: n must be nonnegative");
        return null;
      }
    } catch (Exception e) {
      System.out.println("ERROR: Inputs are not well formed");
      return null;
    }


    return new BloomFilter(r, n);
  }

  /**
   * Creates bloom filter database.
   * @param input from cmd line.
   * @return
   */
  private BloomFilterDatabase createBloomFilterStudentDatabase(String[] input) {
    CSVReader reader = null;
    if (input.length != 2) {
      System.out.println("ERROR: Wrong number of parameters");
      return null;
    }
    try {
      reader = new CSVReader(input[1], new StudentCreator());
    } catch (IOException e) {
      System.out.println("ERROR: Could not read file, may not exist");
      return null;
    }
    List<Student> students = reader.getObjects();
    return new BloomFilterDatabase(students);
  }

  /**
   * inserts element into the bloom filter.
   * @param input from cmd line.
   * @return
   */
  private int insertElement(String[] input) {
    if (filter == null) {
      System.out.println("ERROR: Need to create filter first");
      return -1;
    }
    if (input.length != 2) {
      System.out.println("ERROR: Wrong number of parameters");
      return -1;
    }

    T element = (T) input[1];
    return filter.insertElement(element);
  }

  /**
   * completes query in bloom filter.
   * @param input
   * @return
   */
  private int completeQuery(String[] input) {
    if (filter == null) {
      System.out.println("ERROR: Need to create filter first");
      return -1;
    }
    if (input.length != 2) {
      System.out.println("ERROR: Wrong number of parameters");
      return -1;
    }

    T element = (T) input[1];
    return filter.queryElement(element);
  }

  /**
   * Caller for the kMostSimilar method of the bloom filter database.
   * @param input String input passed to the command
   * @return list of k student ids that are most similar to input id
   */
  private List<Integer> similarKStudents(String[] input) {
    int k;
    int studentId;

    if (studentDatabase == null) {
      System.out.println("ERROR: Need to load students first");
      return null;
    }

    if (input.length != 3) {
      System.out.println("ERROR: Wrong number of parameters");
      return null;
    }
    try {
      k = Integer.parseInt(input[1]);
      studentId = Integer.parseInt(input[2]);
    } catch (Exception e) {
      System.out.println("ERROR: parameters passed of wrong type ");
      return null;
    }
    BloomSimilarityMetric bloomSimilarityMetric = new BloomXNOR();
    return studentDatabase.kMostSimilar(k, studentId, bloomSimilarityMetric);
  }
}
