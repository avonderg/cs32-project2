package kdtreeclasses;

/**
 * Class for a node in the kd tree.
 */
public class Node {
  /**
   * double array containing each numerical data point for the node.
   */
  private double[] dataPoints;
  /**
   * left represents the left child of this node.
   */
  private Node left;
  /**
   * right represents the right child of this node.
   */
  private Node right;
  /**
   * integer representing the id associated with this node.
   */
  private int id;

  /**
   * Basic constructor for creating a node.
   * @param dataPoints an array containing the numeric data fields fo the node
   * @param id an integer representing the id associated with the node
   */
  public Node(double[] dataPoints, int id) {
    this.dataPoints = dataPoints;
    this.id = id;
  }

  /**
   * Method to set the left child of the node.
   * @param left the node to be set as the left child
   */
  public void setLeft(Node left) {
    this.left = left;
  }

  /**
   * Method to set the right child of the node.
   * @param right the node to be set as the right child
   */
  public void setRight(Node right) {
    this.right = right;
  }

  /**
   * basic getter for datapoints.
   * @return the double array that is datapoints.
   */
  public double[] getDataPoints() {
    return dataPoints;
  }

  /**
   * basic getter for the left node.
   * @return the left child node.
   */
  public Node getLeft() {
    return left;
  }

  /**
   * basic getter for the right node.
   * @return the right child node.
   */
  public Node getRight() {
    return right;
  }

  /**
   * basic getter for the id field.
   * @return the int representing the id.
   */
  public int getId() {
    return id;
  }
}
