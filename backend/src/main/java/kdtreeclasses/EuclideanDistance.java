package kdtreeclasses;

/**
 * Class that contains a method to take in two double arrays and computes euclidean distance.
 */
public class EuclideanDistance implements DistanceFunction {

  /**
   * Function that computes euclidean distance between two double arrays.
   * @param dataPoints1 a double array containing coordinates for point 1
   * @param dataPoints2 a double array containing coordinates for point 1
   * @return a double, representing the euclidean distance between the arrays.
   */
  public double getDistance(double[] dataPoints1, double[] dataPoints2) {
    if (dataPoints1.length != dataPoints2.length) {
      System.out.println("ERROR: Distance function failed");
      return -1.0;
    }
    double returnValue = 0;
    for (int i = 0; i < dataPoints1.length; i++) {
      returnValue += Math.pow(dataPoints1[i] - dataPoints2[i], 2);
    }
    returnValue = Math.sqrt(returnValue);
    return returnValue;
  }

}
