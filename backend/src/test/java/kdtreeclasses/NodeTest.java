package kdtreeclasses;

import junit.framework.TestCase;

public class NodeTest extends TestCase {

  public void test() {
    Node sampleNode = new Node(new double[]{1.0, 2.0, 3.0}, 1);
    sampleNode.setLeft(new Node(new double[]{2.0, 3.0, 1.0}, 2));
    assertEquals(sampleNode.getLeft().getId(), 2);
    assertEquals(sampleNode.getRight(), null);
    sampleNode.setRight(new Node(new double[]{0.5, 0.2, 0.4}, 3));
    assertEquals(sampleNode.getRight().getId(), 3);
    assertEquals(sampleNode.getDataPoints()[1], 2.0, 0.001);
  }
}