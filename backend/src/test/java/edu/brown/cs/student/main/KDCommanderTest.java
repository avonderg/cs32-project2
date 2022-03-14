package edu.brown.cs.student.main;

import recommender.RecommenderCommander;
import junit.framework.TestCase;

public class KDCommanderTest extends TestCase {

  public void testHandleCommand() {
    KDCommander testKD = new KDCommander(new RecommenderCommander());
    testKD.handleCommand(new String[] {"HELLLO"});
    assertEquals(testKD.getTree(), null);
    testKD.handleCommand(new String[] {"load_kd", "NOTHING"});
    assertEquals(testKD.getTree(), null);
    testKD.handleCommand(new String[] {"similar_kd", "3", "3"});
    testKD.handleCommand(new String[] {"load_kd", "data/project1/proj1_small.csv"});
    assertEquals(testKD.getTree() != null, true);
    testKD.handleCommand(new String[] {"similar_kd", "pizza"});
    testKD.handleCommand(new String[] {"similar_kd", "3", "no"});
  }
}