package kdtreeclasses;

import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;

public class EuclideanDistanceTest extends TestCase {

  public void testGetDistance() {
    EuclideanDistance sample = new EuclideanDistance();
    double[] dataPoints1 = {1.0, 1.0, 1.0};
    double[] dataPoints2 = {1.0, 1.0, 2.0};
    assertEquals(sample.getDistance(dataPoints1, dataPoints2), 1.0, 0.001);
    double[] dataPoints3 = {1.0, 1.0};
    assertEquals(sample.getDistance(dataPoints1, dataPoints3), -1.0, 0.001);
    double[] dataPoints4 = {20.0, 5.0, 3.0};
    assertEquals(sample.getDistance(dataPoints1, dataPoints4), 19.519221, 0.001);
  }
}