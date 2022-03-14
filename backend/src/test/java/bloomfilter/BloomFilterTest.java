package bloomfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BloomFilterTest {

  @Test
  public void bloomFilterCreationTest(){
    BloomFilter<String> bloomFilterSmall = new BloomFilter<>(0.5, 1);

    assertEquals(1, bloomFilterSmall.getK());
    assertEquals(2, bloomFilterSmall.getM());
    assertEquals(0.5, bloomFilterSmall.getR(), 0.01);
    assertEquals(1, bloomFilterSmall.getN());

    BloomFilter<String> bloomFilterBig = new BloomFilter<>(0.1, 1000);

    assertEquals(4, bloomFilterBig.getK());
    assertEquals(5771, bloomFilterBig.getM());
    assertEquals(0.1, bloomFilterBig.getR(), 0.01);
    assertEquals(1000, bloomFilterBig.getN());
  }

  @Test
  public void bloomFilterAddTest(){
    BloomFilter<String> bloomFilter = new BloomFilter<>(0.5, 4);
    bloomFilter.insertElement("jones");
    assertEquals("000001", bloomFilter.toString());
    bloomFilter.insertElement("total");
    assertEquals("000011", bloomFilter.toString());
    bloomFilter.insertElement("cmon");
    assertEquals("010011", bloomFilter.toString());
  }

  @Test
  public void bloomFilterQueryTest(){
    BloomFilter<String> bloomFilter = new BloomFilter<>(0.2, 4);
    bloomFilter.insertElement("jones");

    int retJones = bloomFilter.queryElement("jones");
    int retRandom = bloomFilter.queryElement("random");

    assertEquals("000010100001000000", bloomFilter.toString());
    assertEquals(1, retJones);
    assertEquals(0, retRandom);
  }

}