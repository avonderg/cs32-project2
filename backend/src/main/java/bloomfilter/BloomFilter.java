package bloomfilter;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bloom Filter class represents the bloom filter data structure parameterized for type T.
 * Contains a set of bits either turned on or off to figure out whether or not a certain
 * element is either definitely not or maybe in a set.
 *
 * @param <T> The type of elements entered to the bloom filter.
 */
public class BloomFilter<T> {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private static final MessageDigest HASH_FUNCTION = BloomFilter.initializeHashFunction();

  private static final int HEX_COMPARISON1 = 0xFF;
  private static final int HEX_COMPARISON2 = 0x0F;
  private static final int RADIX = 16;


  private double r;
  // the false positive rate
  private int n;
  // the expected maximum number of elements
  private int k;
  // the number of hash functions to compute
  private int m;
  // the length of the bit array

  private BitSet bitArray;
  // the internal representation -- no getter for security, can only be seen via toString

  /**
   * Constructs a new Bloom Filter object. Calculates the k (number of
   * hash functions to compute) and m (length of the bit array) based on inputs
   * and uses that information internally.
   *

   * @param r maximum false positive rate wanted between 0 and 1 exclusive param for r
   * @param n maximum number of elements greater than 0 or malformed

   */
  public BloomFilter(double r, int n) {
    this.r = r;
    this.n = n;
    k = (int) Math.ceil(-1 * Math.log(r) / Math.log(2));
    // k = ceil[-1 * log_2(r)]

    m = (int) Math.ceil(k * n / Math.log(2));
    // m =  ceil[kn/ln(2)]

    bitArray = new BitSet(m);
  }

  /**
   * Inserts an element into the bloom filter.
   *
   * @param element to be inserted into the bloom filter
   * @return integer 0 if works or -1 if errors
   */
  public int insertElement(T element) {
    // computes the remainder of each of the hashes when divided by m so that can set bits
    List<BigInteger> remainders = this.getIndicesFromElement(element);
    if (remainders == null) {
      return -1;
    }

    for (BigInteger index : remainders) {
      bitArray.set(index.intValue());
    }
    return 0;
  }

  /**
   * Queries the bloom filter to tell whether the element might be in the
   * filter or definitely is not.
   *
   * @param element the element that is queried.
   * @return integer 0 if works or -1 if errors
   */
  public int queryElement(T element) {
    // make method int rather than boolean to return errors if occur
    boolean elementIsIn = true;

    List<BigInteger> remainders = this.getIndicesFromElement(element);
    if (remainders == null) {
      return -1;
    }

    for (BigInteger index : remainders) {
      elementIsIn = elementIsIn && bitArray.get(index.intValue());
      // all index bits have to be set for this to stay true
    }
    return (elementIsIn ? 1 : 0);
    // converts the boolean to a 1 if true and 0 if false
  }

  /**
   * Gets the indices in the bit array that correspond to this element by running through
   * k hash functions and then taking mod m.
   *
   * @param element the element to get indices for.
   * @return list of BigIntegers representing all these indexes.
   */
  private List<BigInteger> getIndicesFromElement(T element) {
    byte[] data;

    try {
      data = convertToByteData(element);
    } catch (IOException e) {
      System.out.println("ERROR: Could not convert element to byte data");
      return null;
    }

    BigInteger[] hashes = BloomFilter.createHashes(data, this.k);

    List<BigInteger> remainders =
        Arrays.stream(hashes).map(hash -> hash.mod(BigInteger.valueOf(this.m))).collect(
            Collectors.toList());
    return remainders;
  }

  /**
   * Computes the similarity between this Bloom Filter and another.
   * @param bloomFilter another bloom filter to compute similarity against
   * @param similarityMetric strategy patten algorithm to compute the similarity
   * @return integer -1 if errors and >=0 for similarity comparison
   */
  public int similarity(BloomFilter bloomFilter, BloomSimilarityMetric similarityMetric) {
    return similarityMetric.calculateSimilarity(this, bloomFilter);
  }

  /**
   * Converts data from type T to byte array by calling toString.
   *
   * @param data the data to be converted
   * @return byte array representing the data
   * @throws IOException
   */
  private byte[] convertToByteData(T data) throws IOException {
    String stringData = data.toString();
    byte[] bytes = stringData.getBytes(CHARSET);
    // code to get bytes from #452 on ed

    return bytes;
  }

  /**
   * Generates hashes based on the contents of an array of bytes, converts the result into
   * BigIntegers, and stores them in an array. The hash function is called until the required number
   * of BigIntegers are produced.
   * For each call to the hash function a salt is prepended to the data. The salt is increased by 1
   * for each call.
   *
   * @param data      input data.
   * @param numHashes number of hashes/BigIntegers to produce.
   * @return array of BigInteger hashes
   */
  public static BigInteger[] createHashes(byte[] data, int numHashes) {
    BigInteger[] result = new BigInteger[numHashes];

    int k = 0;
    BigInteger salt = BigInteger.valueOf(0);
    while (k < numHashes) {
      HASH_FUNCTION.update(salt.toByteArray());
      salt = salt.add(BigInteger.valueOf(1));
      byte[] hash = HASH_FUNCTION.digest(data);
      HASH_FUNCTION.reset();

      // convert hash byte array to hex string, then to BigInteger
      String hexHash = bytesToHex(hash);
      result[k] = new BigInteger(hexHash, RADIX);
      k++;
    }
    return result;
  }

  /**
   * Converts a byte array to a hex string.
   * Source: https://stackoverflow.com/a/9855338
   *
   * @param bytes the byte array to convert
   * @return the hex string
   */
  private static String bytesToHex(byte[] bytes) {
    byte[] hexArray = "0123456789ABCDEF".getBytes(CHARSET);
    byte[] hexChars = new byte[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & HEX_COMPARISON1;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & HEX_COMPARISON2];
    }
    return new String(hexChars, CHARSET);
  }

  /**
   * Initializes the hash function.
   *
   * @return MessageDigest for SHA-1 hashing function
   */
  private static MessageDigest initializeHashFunction() {
    try {
      return MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }

  /**
   * Getter for r.
   *
   * @return false positive rate
   */
  public double getR() {
    return r;
  }

  /**
   * Getter for n.
   *
   * @return maximum number of elements
   */
  public int getN() {
    return n;
  }

  /**
   * Getter for k.
   *
   * @return the number of hashing functions
   */
  public int getK() {
    return k;
  }

  /**
   * Getter for m.
   *
   * @return length of bit array
   */
  public int getM() {
    return m;
  }

  /**
   * ToString function for Bloom Filter.
   *
   * @return String representing Bloom Filter internal state
   */
  @Override
  public String toString() {
    String output = "";
    for (int i = 0; i < m; i++) {
      output += bitArray.get(i) ? "1" : "0";
    }
    return output;
  }
}
