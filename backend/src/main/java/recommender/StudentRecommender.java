package recommender;

import bloomfilter.BloomFilter;
import bloomfilter.BloomFilterDatabase;
import bloomfilter.BloomSimilarityMetric;
import bloomfilter.BloomXNOR;
import cvsprocessing.Student;
import edu.brown.cs.student.main.BloomCommander;
import edu.brown.cs.student.main.KDCommander;
import edu.brown.cs.student.main.Repl;
import kdtreeclasses.EuclideanDistance;
import kdtreeclasses.KDtree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * StudentRecommender Class which is responsible for all of the recommender setup, loading, and
 * also implementation.
 *
 */
public class StudentRecommender {

  private List<Integer> recommendedStudents;
  private List<Student> students;
  private BloomFilter filter;
  private KDtree kdTree;
  private KDCommander kdCommander;
  private BloomCommander bloomCommander;
  private BloomFilterDatabase bloomFilterDatabase;
  private Map<Integer, Double> bloomSims;
  private Map<Integer, Double> kdSims;
  private final int numDim = 3;
  private final double r = 0.1;
  private final int n = 36;

  /**
   * Constructor which creates the studentRecommender.
   * @param stu list of students
   */
  public StudentRecommender(List<Student> stu) {
    students = stu;
    recommendedStudents = new ArrayList<>();
    kdCommander = new KDCommander(Repl.getRecommenderCommander());
    bloomCommander = new BloomCommander(Repl.getRecommenderCommander());
    bloomFilterDatabase = new BloomFilterDatabase(students);
    createKDTree();
    createBloomFilter();
  }

  /**
   * Creates KDTree.
   */
  public void createKDTree() {
    kdTree = new KDtree(numDim, students);
  }

  /**
   * Creates bloom filter.
   */
  public void createBloomFilter() {
    filter = new BloomFilter(r, n);
  }

  /**
   * Method which handles the overall steps of the recommender algorithm.
   * @param num of recommendations
   * @param studentID Id of student for which the recs are for.
   * @return list of integers which are the ids of the recommended group members.
   */
  public List<Integer> recommend(int num, int studentID) {

    populateBloomSims(num, studentID);
    populateKDSims(num, studentID);
    List<Map<Integer, Double>> mapList  = normalizeKDBloom();
    Map<Integer, Double> averagedDistances = getAverages(mapList.get(0), mapList.get(1));
    recommendedStudents = getFirstK(averagedDistances, num);
    return recommendedStudents;
  }

  /**
   * Once the averageDistances have been found, gets the first k of them (k is from user input).
   * @param averagedDistances averaged distances
   * @param num num recs
   * @return list of integers which are the ids of the recommended group members.
   */
  public List<Integer> getFirstK(Map<Integer, Double> averagedDistances, int num) {
    averagedDistances = sortByValue(averagedDistances);
    List<Integer> toReturn = new ArrayList<>();
    int i = 1;
    for (int id: averagedDistances.keySet()) {
      if (i > num) {
        break;
      } else {
        toReturn.add(id);
        i++;
      }
    }

    return toReturn;
  }

  /**
   * Sorts the hashmap by value.
   * CITATION: THIS SORTING FUNCTION IS FROM GEEKS FOR GEEKS, LINK PASTED BELOW...
   * https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/#:~:text=Solution%3A%20
   * The%20idea%20is%20to,is%20sorted%20according%20to%20values.
   * @param hm map to be sorted
   * @return HashMap containing sorted values
   */
  public static HashMap<Integer, Double> sortByValue(Map<Integer, Double> hm) {
    // Create a list from elements of HashMap
    List<Map.Entry<Integer, Double>> list =
        new LinkedList<Map.Entry<Integer, Double>>(hm.entrySet());

    // Sort the list
    Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
      public int compare(Map.Entry<Integer, Double> o1,
                         Map.Entry<Integer, Double> o2) {
        return (o1.getValue()).compareTo(o2.getValue());
      }
    });

    // put data from sorted list to hashmap
    HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
    for (Map.Entry<Integer, Double> aa : list) {
      temp.put(aa.getKey(), aa.getValue());
    }
    return temp;
  }

  /**
   * Averages the 2 hashmaps.
   * @param m1 first map to be averaged.
   * @param m2 second map to be averaged.
   * @return returns hashmap with averages
   */
  public Map<Integer, Double> getAverages(Map<Integer, Double> m1, Map<Integer, Double> m2) {
    Map<Integer, Double> average = new HashMap<Integer, Double>();
    if (m1.keySet().equals(m2.keySet())) {
      for (int iD: m1.keySet()) {
        double score1 = m1.get(iD);
        double score2 = m2.get(iD);
        average.put(iD, ((1 - score1) + score2) / 2);
      }
    } else {
      System.out.println("ERROR: the students from bloom filter and kd tree are not the same");
    }

    return average;
  }

  /**
   * Finds the minimum value in the given map.
   *
   * CITATION:
   * This method is a version of code from nahmed's Recommender class. Fairly
   * straightforward function to find the minimum value.
   *
   * @param map The map to find the minimum value in.
   * @return The minimum value in the map or -1 if error.
   */
  public int findMinX(Map<Integer, Double> map) {
    // find the minimum value in the map
    double min = Double.MAX_VALUE;
    int minId = -1;
    for (Integer id: map.keySet()) {
      double value = map.get(id);
      if (value < min) {
        minId = id;
        min = value;
      }
    }
    return minId;
  }

  /**
   * Finds the maximum value in the given map.
   *
   * CITATION:
   * This method is a version of code from nahmed's Recommender class. Fairly
   * straightforward function.
   *
   * @param map The map to find the maximum value in.
   * @return The maximum value in the map or -1 if error.
   */
  public int findMaxX(Map<Integer, Double> map) {
    // find the maximum value in the map
    double max = Double.MIN_VALUE;
    int maxId = -1;
    for (int id : map.keySet()) {
      double x = map.get(id);
      if (x > max) {
        max = x;
        maxId = id;
      }
    }
    return maxId;
  }

  /**
   * Normalizes the kdMap and bloomMap and of studentIDs and distances.
   * CITATION:
   * This method is a modified version of code from nahmed's Recommender class.
   * @return list of normalized hashMaps
   */
  public List<Map<Integer, Double>> normalizeKDBloom() {
    // copy the map so we don't modify the original
    HashMap<Integer, Double> normalizedMap1 = new HashMap<>(bloomSims);

    //finding min and max
    double minX = normalizedMap1.get(findMinX(normalizedMap1));
    double maxX = normalizedMap1.get(findMaxX(normalizedMap1));

    // normalize
    for (Integer id : normalizedMap1.keySet()) {
      double x = normalizedMap1.get(id);
      normalizedMap1.put(id, (x - minX) / (maxX - minX));
    }

    HashMap<Integer, Double> normalizedMap2 = new HashMap<>(kdSims);

    //finding min and max
    minX = normalizedMap2.get(findMinX(normalizedMap2));
    maxX = normalizedMap2.get(findMaxX(normalizedMap2));

    // normalize the map
    for (Integer id : normalizedMap2.keySet()) {
      double x = normalizedMap2.get(id);
      normalizedMap2.put(id, (x - minX) / (maxX - minX));
    }

    List<Map<Integer, Double>> mapList = new ArrayList<Map<Integer, Double>>();
    mapList.add(normalizedMap1);
    mapList.add(normalizedMap2);

    return mapList;
  }

  /**
   * Gets similarity scores from bloom filter.
   * @param num of recs
   * @param studentID id of student for which the recs are for
   */
  public void populateBloomSims(int num, int studentID) {
    try {
      BloomSimilarityMetric bloomSimilarityMetric = new BloomXNOR();
      bloomFilterDatabase.kMostSimilar(students.size(), studentID, bloomSimilarityMetric);
      Map<Integer, Integer> preBloomSims = bloomFilterDatabase.getSims();
      Map<Integer, Double> preBloomSimsDouble = convertBloomSims(preBloomSims);
      bloomSims = preBloomSimsDouble;

    } catch (Exception e) {
      System.out.println("ERROR: Trouble getting data from bloom filter database");
    }
  }

  /**
   * converts type of bloom similarities map to be used later.
   * @param temp temp map to be converted
   * @return converted map
   */
  public Map<Integer, Double> convertBloomSims(Map<Integer, Integer> temp) {
    Map<Integer, Double> bloomSimilaritiesDouble = new HashMap<>();
    for (int id: temp.keySet()) {
      double value = temp.get(id).intValue();
      bloomSimilaritiesDouble.put(id, value);
    }
    return bloomSimilaritiesDouble;
  }

  /**
   * Gets the Kd similarities from kdTree.
   * @param num of recs
   * @param studentID id of student for which the recs are for
   */
  public void populateKDSims(int num, int studentID) {
    List<KDtree.Pair> pairs;
    try {
      kdTree.search(students.size(), studentID, new EuclideanDistance());
      pairs = kdTree.getGiveToRecommender();
      kdSims = new HashMap<Integer, Double>();
      for (KDtree.Pair pair: pairs) {
        kdSims.put(pair.getiNode().getId(), pair.getDistance());
      }

    } catch (Exception e) {
      System.out.println("ERROR: Trouble getting data from kdtree");
    }
  }
}
