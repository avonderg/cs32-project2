package bloomfilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bloom Filter Database represents a
 * database of bloomInsertables where each bloomInsertable is
 * represented by a Bloom Filter. This is mainly used
 * for calculating the similarity of non-numerical attributes
 * of the bloomInsertables.
 *
 * @param <T> generic type that implements the BloomInsertable interface
 * @author Suraj Anand
 */
public class BloomFilterDatabase<T extends BloomInsertable> {
  static final int MAX_ELEMENTS_SET = 36;
  // Students have 4 string fields and 4 string array fields. Assuming arrays <= 8 elements,
  // (reasonable after looking at data) -- 4 + 4 * 8 = 36
  static final double FALSE_POSITIVE_SET = 0.1;
  // from the specification give

  private int numberBloomInsertables;
  private Map<Integer, BloomFilter> bloomInsertables;
  private Map<Integer, Integer> sims;


  /**
   * Creates a new BloomFilterBloomInsertableDatabase object.
   *
   * @param bloomInsertables a well formed List of bloomInsertables
   */
  public BloomFilterDatabase(List<BloomInsertable> bloomInsertables) {
    int idBloomInsertable;
    this.bloomInsertables = new HashMap<>();
    for (BloomInsertable bloomInsertable
        : bloomInsertables) {
      idBloomInsertable = bloomInsertable.getID();
      BloomFilter bloomFilterBloomInsertable = createBloomFilter(bloomInsertable
      );
      this.bloomInsertables.put(Integer.valueOf(idBloomInsertable), bloomFilterBloomInsertable);
    }
    this.numberBloomInsertables = bloomInsertables.size();
  }

  /**
   * Computes the k most similar bloomInsertable
   * s in the class
   * based off the similarity of the bloom filters.
   *
   * @param k                integer number of bloomInsertables to find similar >= 0
   * @param id               id of the bloomInsertable, expected to be in the list of
   *                         bloomInsertables passed to constructor
   * @param similarityMetric the metric of bloom filter similarity
   *                         (implements BloomSimilarityMetric)
   * @return List of ids of the k bloomInsertables who are most similar
   */
  public List<Integer> kMostSimilar(int k, int id, BloomSimilarityMetric similarityMetric) {
    if (k == 0) {
      return new ArrayList<>();
    }

    BloomFilter<String> foundBloomInsertable = this.bloomInsertables.get(Integer.valueOf(id));
    if (foundBloomInsertable == null) {
      System.out.println("ERROR: No BloomInsertable of that Name Found");
      return new ArrayList<>();
    }

    Map<Integer, Integer> similarities = new HashMap<>();
    for (Integer idOfBloomInsertable : this.bloomInsertables.keySet()) {
      if (id != idOfBloomInsertable.intValue()) {
        BloomFilter<String> bloomFilterOfBloomInsertable =
            this.bloomInsertables.get(idOfBloomInsertable);
        int similarity = bloomFilterOfBloomInsertable.similarity(this.bloomInsertables.get(id),
            similarityMetric);
        similarities.put(idOfBloomInsertable, Integer.valueOf(similarity));
      }
    }

    sims = similarities;

    List<Map.Entry<Integer, Integer>> similaritiesList = new ArrayList<>(similarities.entrySet());

    Collections.shuffle(similaritiesList);
    Collections.sort(similaritiesList, Collections.reverseOrder(Map.Entry.comparingByValue()));
    // randomly shuffled before sort so that if ties, it is random (reliant on sort using mergesort)

    int endIndex = Math.min(k, similaritiesList.size());
    // computes the last index to prevent index out of bounds error

    List<Map.Entry<Integer, Integer>> kMostSimilar = similaritiesList.subList(0, endIndex);
    List<Integer> kMostSimilarIDs =
        kMostSimilar.stream().map(me -> me.getKey()).collect(Collectors.toList());

    return kMostSimilarIDs;
  }

  /**
   * Creates a Bloom Filter from the bloomInsertable
   * given.
   * @param bloomInsertable a passed bloomInsertable object with nonnull fields
   * @return a Bloom Filter representing that bloomInsertable
   */
  private BloomFilter createBloomFilter(BloomInsertable bloomInsertable) {
    List<String> dataToInsert = bloomInsertable.getDataToInsert();

    BloomFilter<String> bloomFilter = new BloomFilter<>(FALSE_POSITIVE_SET, MAX_ELEMENTS_SET);

    for (String datum : dataToInsert) {
      bloomFilter.insertElement(datum);
    }
    return bloomFilter;
  }

  /**
   * Getter for number of bloomInsertables variable.
   *
   * @return the number of bloomInsertable
   * s in the database.
   */
  public int getNumberBloomInsertables() {
    return numberBloomInsertables;
  }

  /**
   * Gets the similarity scores for different ids.
   * @return sims the computed similarities
   */
  public Map<Integer, Integer> getSims() {
    return sims;
  }
}


