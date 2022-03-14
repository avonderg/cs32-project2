package bloomfilter;

/**
 * Example class of a bloom similarity metric (not in use).
 * Potential use for strategy pattern.
 * Implements the bitwise AND comparison for Two Bloom Filters
 * which outputs 1 only when both the inserted Bloom Filters
 * have an internal state of 1 at a certain position.
 * @author Suraj Anand
 */
public class BloomAND implements BloomSimilarityMetric {

  /**
   * Strategy pattern to calculate similarity of 2 bloom filters by bitwise AND then sum.
   * @param bloomFilter1 first bloom filter input
   * @param bloomFilter2 second bloom filter input
   * @return integer >=0 for similarity or -1 if errors
   */
  @Override
  public int calculateSimilarity(BloomFilter bloomFilter1, BloomFilter bloomFilter2) {
    int countAND = 0;

    String bf1InternalState = bloomFilter1.toString();
    String bf2InternalState = bloomFilter2.toString();
    char[] bf1InternalStateChars = bf1InternalState.toCharArray();
    char[] bf2InternalStateChars = bf2InternalState.toCharArray();

    if (bf1InternalStateChars.length != bf2InternalStateChars.length) {
      return -1;
    }

    int lenBloomFilters = bf1InternalState.length();

    for (int i = 0; i < lenBloomFilters; i++) {
      if (bf1InternalStateChars[i] == '1' && bf2InternalStateChars[i] == '1') {
        countAND++;
      }
    }
    return countAND;
  }
}
