package edu.brown.cs.student.main;

/**
 * Class that contains two methods that are needed to make an element into a node and add it to the
 * kdtree.
 * @author VPandiar
 */
public interface NumericData {
  /**
   * function to extract necessary numeric values from a class that implements NumericData.
   * @return an array of doubles representing the numeric fields in a NumericData object
   */
  double[] extractNumeric();
  /**
   * function to return the id associated with this object.
   * @return the id
   */
  int getID();
}
