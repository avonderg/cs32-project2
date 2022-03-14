package kdtreeclasses;

import edu.brown.cs.student.main.NumericData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * kdTree class represents a k-d tree, which is essentially a binary tree with multiple dimension.
 */
public class KDtree {
  /**
   * Node representing the root of the tree.
   */
  private Node root;
  /**
   * integer representing the number of each node in the tree has.
   */
  private final int numberOfDimensions;
  /**
   * HashMap mapping each id to a Node.
   */
  private final HashMap<Integer, Node> idToNode = new HashMap<>();

  private List<Pair> giveToRecommender;

  /**
   * Constructor for the tree, builds the entire tree.
   *
   * @param numberOfDimensions the number of dimensions each node in the tree has
   * @param listOfData the items that need to be inserted into the tree
   */
  public KDtree(int numberOfDimensions, List<? extends NumericData> listOfData) {
    this.numberOfDimensions = numberOfDimensions;
    try {
      ArrayList<Node> listOfNodes = new ArrayList<>();
      List<NumericData> dataList = (List<NumericData>) listOfData;
      for (int i = 0; i < dataList.size(); i++) {
        NumericData currentData = dataList.get(i);
        double[] extracted = currentData.extractNumeric();
        int id = currentData.getID();
        Node node = new Node(extracted, id);
        listOfNodes.add(node);
      }
      listOfNodes.sort(Comparator.comparingDouble(a -> a.getDataPoints()[0]));
      Node rootNode = listOfNodes.get(listOfNodes.size() / 2);
      this.insert(rootNode);
      listOfNodes.remove(rootNode);
      for (Node currentNode : listOfNodes) {
        this.insert(currentNode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Basic constructor for tree, for testing purposes.
   * @param root the root node of the tree.
   * @param numberOfDimensions the number of dimensions for the tree.
   */
  public KDtree(Node root, int numberOfDimensions) {
    this.root = root;
    this.numberOfDimensions = numberOfDimensions;
  }

  /**
   * Wrapper function to insert a node into the tree.
   * @param toInsert, the node that should be inserted into the tree
   */
  void insert(Node toInsert) {
    // check if a node with the same id is being inserted
    if (idToNode.get(toInsert.getId()) != null) {
      System.out.println("ERROR: Node with this id already exists in tree");
    } else if (toInsert.getDataPoints().length != numberOfDimensions) {
      System.out.println("ERROR: Node with this many dimensions cannot be added");
    } else if (this.root == null) {
      idToNode.put(toInsert.getId(), toInsert);
      this.root = toInsert;
    } else {
      insert(this.root, toInsert, 0);
    }
  }

  /**
   * Function that actually inserts the node into the tree.
   *
   * @param currNode, we use the value of this node to decide whether to go left, right, or place
   *                  the node
   * @param toInsert, the node that should be inserted into the tree
   * @param depth,    integer representing what level of the tree we are currently on
   * @return the root node or the node that has just been inserted, we need this for setting the
   * children of each node
   */
  Node insert(Node currNode, Node toInsert, int depth) {
    if (currNode == null) {
      idToNode.put(toInsert.getId(), toInsert);
      return toInsert;
    }
    int dimension = depth % numberOfDimensions;
    depth++;
    if (toInsert.getDataPoints()[dimension] < currNode.getDataPoints()[dimension]) {
      currNode.setLeft(insert(currNode.getLeft(), toInsert, depth));
    } else {
      currNode.setRight(insert(currNode.getRight(), toInsert, depth));
    }
    return currNode;
  }

  /**
   * function to add a node and its distance into a sorted arraylist.
   *
   * @param k,           integer representing desired length of arraylist
   * @param listOfPairs, a list of pairs that pairs will be inserted into
   * @param toAdd,       the pair that is trying to be added into the list
   */
  void addPair(int k, List<Pair> listOfPairs, Pair toAdd) {
    if (listOfPairs.size() == 0) {
      listOfPairs.add(toAdd);
      return;
    }
    int i = listOfPairs.size();
    while (i > 0 && Double.compare(listOfPairs.get(i - 1).distance, toAdd.distance) > 0) {
      i--;
    }
    listOfPairs.add(i, toAdd);
    if (listOfPairs.size() > k) {
      listOfPairs.remove(listOfPairs.size() - 1);
    }
  }

  /**
   * search method that returns the k nearest neighbors to the searchFor node.
   *
   * @param k,           integer representing desired number of neighbors
   * @param depth,       integer representing depth of tree at root node
   * @param currNode,        node that is currently being compared to the searchFor node
   * @param searchFor,   we want to find the k nearest neighbors of this node
   * @param returnValue, a list of pairs, nodes to distances, will eventually be the k nearest
   *                     neighbors
   * @param distanceFunction, an object which contains a distance function
   */
  void search(int k, int depth, Node currNode, Node searchFor, List<Pair> returnValue,
              DistanceFunction distanceFunction) {
    if (currNode == null) {
      return;
    }
    double distance = distanceFunction.getDistance(currNode.getDataPoints(),
        searchFor.getDataPoints());
    Pair newPair = new Pair(currNode, distance);
    if (searchFor.getId() != currNode.getId()) {
      addPair(k, returnValue, newPair);
    }
    int dimension = depth % numberOfDimensions;
    double axisDistance = Math.abs(searchFor.getDataPoints()[dimension]
        - currNode.getDataPoints()[dimension]);
    double lastDistance = returnValue.get(returnValue.size() - 1).distance;
    depth++;
    if (((lastDistance - axisDistance) >= 0) || (returnValue.size() < k)) {
      search(k, depth, currNode.getRight(), searchFor, returnValue, distanceFunction);
      search(k, depth, currNode.getLeft(), searchFor, returnValue, distanceFunction);
    } else {
      if (searchFor.getDataPoints()[dimension] >= currNode.getDataPoints()[dimension]) {
        search(k, depth, currNode.getRight(), searchFor, returnValue, distanceFunction);
      } else {
        search(k, depth, currNode.getLeft(), searchFor, returnValue, distanceFunction);
      }
    }
  }

  /**
   * wrapper function for the search method, does some basic checks.
   *
   * @param k        integer representing desired number of neighbors
   * @param searchID the id of the node we want to find the k nearest neighbors for
   * @param distanceFunction object containing a distance function
   * @return returns the k nearest neighbors as a list of ids
   */
  public List<Integer> search(int k, int searchID, DistanceFunction distanceFunction) {
    List<Integer> returnValue = new ArrayList<>();
    Node searchFor = idToNode.get(searchID);
    if (k == 0) {
      return returnValue;
    } else if (k < 0) {
      System.out.println("ERROR: invalid number for k");
      return returnValue;
    } else if (searchFor == null) {
      System.out.println("ERROR: Node not found");
      return returnValue;
    }
    ArrayList<Pair> listOfPairs = new ArrayList<>();
    giveToRecommender = listOfPairs;
    search(k, 0, this.root, searchFor, listOfPairs, distanceFunction);
    for (Pair currentPair : listOfPairs) {
      returnValue.add(currentPair.iNode.getId());
    }
    return returnValue;
  }

  /**
   * Basic getter for the root, for testing purposes.
   * @return the root node of the tree.
   */
  public Node getRoot() {
    return root;
  }

  /**
   *
   * @return the list of euclidean distances...
   */
  public List<Pair> getGiveToRecommender() {
    return giveToRecommender;
  }

  /**
   * Short class used to associate a node with its distance away from a given point.
   */
  public static class Pair {
    /**
     * The node associated with this pair.
     */
    private final Node iNode;
    /**
     * The distance of the associated node from a given point.
     */
    private final Double distance;

    /**
     * Basic constructor for a pair.
     *
     * @param iNode,    node associated with this pair
     * @param distance, distance of the associated node from a given point
     */
    Pair(Node iNode, Double distance) {
      this.iNode = iNode;
      this.distance = distance;
    }

    /**
     *
     * @return iNode
     */
    public Node getiNode() {
      return iNode;
    }

    /**
     *
     * @return distance.
     */
    public double getDistance() {
      return distance;
    }
  }
}
