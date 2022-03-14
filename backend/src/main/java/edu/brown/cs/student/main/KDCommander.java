package edu.brown.cs.student.main;

import cvsprocessing.CSVReader;
import cvsprocessing.StudentCreator;
import recommender.RecommenderCommander;
import kdtreeclasses.KDtree;
import kdtreeclasses.EuclideanDistance;
import java.io.IOException;
import java.util.List;

/**
 * Class which implements the CommandAcceptor interface which is responsible for handling all of the
 * kd tree commands. The class contains a kdTree and CSVReader which it uses in
 * order to implement the commands.
 * @author Onaphade
 * @author Vpandiar
 */
public class KDCommander implements CommandAcceptor {

  private KDtree tree = null;
  private int numDim = 3;
  private CSVReader reader;
  private RecommenderCommander headers;

  /**
   * Constructor of BloomCommander. Accepts a RecommenderCommander which contains a list of headers.
   * @param hdrs a RecommenderCommander which contains a list of headers.
   */
  public KDCommander(RecommenderCommander hdrs) {
    headers = hdrs;
  }

  /**
   * Function that handles commands associated with a kd tree.
   * @param input an array of strings representing user input
   */
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "load_kd":
        if (input.length != 2) {
          break;
        }
        String path = input[1];
        try {
          reader = new CSVReader(path, new StudentCreator());
        } catch (IOException e) {
          System.out.println("ERROR: could not read file");
        }
        if (reader != null) {
          tree = new KDtree(numDim, reader.getObjects());
          System.out.println("Read " + reader.getObjects().size() + " students from " + path);
        }
        break;
      case "similar_kd":
        try {
          int k = Integer.parseInt(input[1]);
          int userId = Integer.parseInt(input[2]);
          if (tree == null) {
            System.out.println("ERROR: no tree loaded");
          } else {
            List<Integer> ids = tree.search(k, userId, new EuclideanDistance());
            for (Integer id : ids) {
              System.out.println(id);
            }
          }
        } catch (NumberFormatException e) {
          System.out.println("ERROR: arguments are not formatted correctly");
        }
        break;
      default:
        System.out.println("ERROR: Did not match expected input");
        break;
    }
  }

  /**
   * returns the kdTree.
   * @return tree
   */
  public KDtree getTree() {
    return tree;
  }
}
