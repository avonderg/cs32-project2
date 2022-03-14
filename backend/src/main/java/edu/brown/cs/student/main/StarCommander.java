package edu.brown.cs.student.main;

import cvsprocessing.CSVReader;
import cvsprocessing.Galaxy;
import cvsprocessing.StarCreator;

import java.io.IOException;

/**
 * Class which implements the CommandAcceptor interface which is responsible for handling all of the
 * star commands. The class contains a star and CSVReader which it uses in
 * order to implement the commands.
 * @author Onaphade
 */
public class StarCommander implements CommandAcceptor {

  private CSVReader reader;
  private Galaxy galaxy;

  /**
   * Handles all of the commands that the stars functionality needs to implement.
   * @param input from cmd line.
   */
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "stars":
        if (input.length != 2) {
          System.out.println("ERROR: number of inputs is wrong");
          return;
        }
        String path = input[1];
        try {
          reader = new CSVReader(path, new StarCreator());
        } catch (IOException e) {
          System.out.println("ERROR: could not read file");
        }
        if (reader != null) {
          galaxy = new Galaxy(reader.getObjects());
          System.out.println("Read " + galaxy.size() + " stars from " + path);
        }
        break;
      case "naive_neighbors":
        if (galaxy != null) {
          int[]result = galaxy.naiveNeighbors(input);
          if (result == null) {
            System.out.println("ERROR: wrong inputs");
          } else {
            for (int i : result) {
              System.out.println(i);
            }
          }
        } else {
          System.out.println("ERROR: galaxy not there");
        }
        break;
      default:
        System.out.println("ERROR: not a command");
        break;
    }
  }

  /**
   *
   * @return the reader
   */
  public CSVReader getReader() {
    return reader;
  }

  /**
   *
   * @param reader to replace current reader
   */
  public void setReader(CSVReader reader) {
    this.reader = reader;
  }

  /**
   *
   * @return the set of Galaxy
   */
  public Galaxy getGalaxy() {
    return galaxy;
  }

  /**
   *
   * @param galaxy to replace current galaxy
   */
  public void setGalaxy(Galaxy galaxy) {
    this.galaxy = galaxy;
  }
}
