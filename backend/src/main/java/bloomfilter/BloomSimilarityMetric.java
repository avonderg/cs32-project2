package bloomfilter;

/**
 * Interface for any class that wants to be able to calculate the similarity between two
 * Bloom Filters.
 * @author Suraj Anand
 */
public interface BloomSimilarityMetric {

  /**
   * Calculates the similarity between two bloom filters by strategy pattern.
   * @param bloomFilter1 First bloom filter
   * @param bloomFilter2 Second bloom filter
   * @return integer >=0 for similarity or -1 if error
   */
  int calculateSimilarity(BloomFilter bloomFilter1, BloomFilter bloomFilter2);
}
