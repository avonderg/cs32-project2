package kdtreeclasses;

import edu.brown.cs.student.main.KDCommander;
import recommender.RecommenderCommander;
import junit.framework.TestCase;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class KDtreeTest extends TestCase {

  public void testInsert() {
    Node rootNode = new Node(new double[] {0,0,0}, 1);
    KDtree testkdTree = new KDtree(rootNode, 3);
    testkdTree.insert(new Node(new double[] {1, 0, 0}, 2));
    assertEquals(rootNode.getRight().getId(), 2);
    assertEquals(rootNode.getLeft(), null);
    testkdTree.insert(new Node(new double[] {5, -3, 0}, 3));
    assertEquals(rootNode.getRight().getLeft().getId(), 3);
    testkdTree.insert(new Node(new double[] {-3, 0, 0}, 4));
    assertEquals(rootNode.getLeft().getId(), 4);
    testkdTree.insert(new Node(new double[] {-5, 0, 0}, 4));
    assertEquals(rootNode.getLeft().getRight(), null);
    Node rootNode2 = new Node(new double[] {0, 0, 0}, 1);
    testkdTree = new KDtree(rootNode2, 3);
    testkdTree.insert(new Node(new double[]{0, 0}, 2));
    assertEquals(testkdTree.getRoot().getRight(), null);
    assertEquals(testkdTree.getRoot().getLeft(), null);
  }

  public void testBuildTree() {
    KDCommander testKDCommand = new KDCommander(new RecommenderCommander());
    testKDCommand.handleCommand(new String[] {"load_kd", "data/project1/proj1_small.csv"});
    KDtree testkdTree = testKDCommand.getTree();
    double rootVal = testkdTree.getRoot().getDataPoints()[0];
    double rightChildVal = testkdTree.getRoot().getRight().getDataPoints()[0];
    double leftChildVal = testkdTree.getRoot().getLeft().getDataPoints()[0];
    assertEquals(rootVal <= rightChildVal, true);
    assertEquals(leftChildVal < rootVal, true);
    assertEquals(leftChildVal < rightChildVal, true);
    rightChildVal = testkdTree.getRoot().getRight().getDataPoints()[1];
    double rightRightChildVal = testkdTree.getRoot().getRight().getRight().getDataPoints()[1];
    assertEquals(rightRightChildVal >= rightChildVal, true);
  }
  public void testSearch() {
    KDCommander testKDCommand = new KDCommander(new RecommenderCommander());
    testKDCommand.handleCommand(new String[] {"load_kd", "data/project1/proj1_small.csv"});
    KDtree testkdTree = testKDCommand.getTree();
    List<Integer> searchResults = testkdTree.search(3, 3, new EuclideanDistance());
    assertEquals(searchResults.size(), 3);
    assertEquals(searchResults.get(0), 19, 0.001);
    searchResults = testkdTree.search(0, 1, new EuclideanDistance());
    assertEquals(searchResults.size(), 0);
    searchResults = testkdTree.search(-2, 3, new EuclideanDistance());
    assertEquals(searchResults.size(), 0);
    searchResults = testkdTree.search(5, -5, new EuclideanDistance());
    assertEquals(searchResults.size(), 0);
  }
}