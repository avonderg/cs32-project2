package kdtreeclasses;

/**
 * An interface containing a function to compute distance between two double arrays.
 */
public interface DistanceFunction {
  /**
   * Function that computes distance between two double arrays.
   * @param dataPoints1 a double array containing coordinates for point 1
   * @param dataPoints2 a double array containing coordinates for point 1
   * @return a double, representing the euclidean distance between the arrays.
   */
  double getDistance(double[] dataPoints1, double[] dataPoints2);
}
