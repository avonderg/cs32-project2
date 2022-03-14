package edu.brown.cs.student.main;

import cvsprocessing.Student;
import recommender.RecommenderCommander;
import org.junit.Test;


public class BloomCommanderTest {

  @Test
  public void testHandleCommand() {
    BloomCommander<Student> testBloom = new BloomCommander<Student>(new RecommenderCommander());
    testBloom.handleCommand(new String[] {"HELLLO"});
    testBloom.handleCommand(new String[] {"similar_bf", "3", "3"});
    testBloom.handleCommand(new String[] {"load_bf", "data/project1/proj1_small.csv"});
    testBloom.handleCommand(new String[] {"similar_bf", "pizza"});
    testBloom.handleCommand(new String[] {"similar_bf", "3", "no"});
    testBloom.handleCommand(new String[] {"create_bf", "0.05", "10"});
    testBloom.handleCommand(new String[] {"insert_bf", "hi"});
    testBloom.handleCommand(new String[] {"query_bf", "hi"});
  }
}