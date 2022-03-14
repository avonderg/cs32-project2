package cvsprocessing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Class that stores multiple stars and has functions to find naive neighbors.
 * @author VPandiar
 * @author Onaphade
 */
public class Galaxy {
  /**
   * ArrayList containing stars.
   */
  private List<Star> galaxy;

  /**
   * constructor.
   * @param gal galaxy
   */
  public Galaxy(List<Star> gal) {
    galaxy = gal;
  }

  /**
   * Function to add star to the galaxy.
   * @param inStar an object of type star to be added to a galaxy.
   */
  public void addStar(Star inStar) {
    galaxy.add(inStar);
  }

  /**
   * Constructor.
   */
  public Galaxy() {
    galaxy = new ArrayList<>();
  }

  /**
   * Function that returns the number of stars in the galaxy.
   * @return integer representing the number of stars in the galaxy.
   */
  public int size() {
    return galaxy.size();
  }

  /**
   * Function that calls other naive neighbor functions, depending on number of parameters.
   * @param input Array of strings, each representing a word, last may be a name of a star
   * @return Integer array of starIDs that are closest to the selected location or star
   */
  public int[] naiveNeighbors(String[] input) {
    if (input.length == 5) {
      return this.naiveNeighbors(input[1], input[2], input[3], input[4]);
    } else if (input.length == 3) {
      return this.naiveNeighbors(input[1], input[2]);
    }
    return null;
  }

  /**
   * Function that calls another naive neighbor function, this function parses the strings so the
   * next function can find the nearest k stars to the position x,y,z.
   * @param k a String, representing the number of stars to be returned.
   * @param x a String, representing the x coordinate of the position.
   * @param y a String, representing the y coordinate of the position.
   * @param z a String, representing the z coordinate of the position.
   * @return Integer array of k starIDs that are closest to the x,y,z position.
   */
  public int[] naiveNeighbors(String k, String x, String y, String z) {
    try {
      double doubleX = Double.parseDouble(x);
      double doubleY = Double.parseDouble(y);
      double doubleZ = Double.parseDouble(z);
      int intK = Integer.parseInt(k);
      return naiveNeighbors(intK, doubleX, doubleY, doubleZ, null);
    } catch (NumberFormatException e) {
      System.out.println("ERROR: Data cannot be parsed.");
    }
    return new int[0];
  }

  /**
   * Function that calls another naive neighbor function, this function parses the strings so the
   * next function can find the nearest k stars to the star of name "name". This function also
   * finds the star of name "name"
   * @param k a String, representing the number of stars to be returned.
   * @param name a String, a name of a star
   * @return Integer array of k starIDs that are closest to star of name "name", not including
   * the star of name "name"
   */
  public int[] naiveNeighbors(String k, String name) {
    int intK;
    try {
      intK = Integer.parseInt(k);
    } catch (NumberFormatException e) {
      System.out.println("ERROR: Data cannot be parsed");
      return new int[0];
    }
    if (name.charAt(0) != '"' || name.charAt(name.length() - 1) != '"') {
      System.out.println("ERROR: Invalid Input");
      return new int[0];
    }
    name = name.replace("\"", "");
    if (name.equals("")) {
      System.out.println("ERROR: Cannot search for empty string.");
      return new int[0];
    }
    Star lookingFor = null;
    for (Star star : galaxy) {
      if (star.getProperName().equals(name)) {
        lookingFor = star;
        break;
      }
    }
    if (lookingFor == null) {
      System.out.println("ERROR: Star not found");
      return new int[0];
    }
    return naiveNeighbors(intK, lookingFor.getX(), lookingFor.getY(),
        lookingFor.getZ(), lookingFor);
  }

  /**
   * Function that actually runs the search for the naive neighbors. Finds the k nearest stars
   * to position x, y, z.
   * @param k an int representing the number of stars to return.
   * @param x a double representing the x position.
   * @param y a double representing the y position.
   * @param z a double representing the z position.
   * @param lookingFor this parameter is nonnull if the search is being done around a specific
   *                   star, since this star needs to be removed from the list that is to be
   *                   returned. If null, proceed normally.
   * @return an integer array of k starIDs that are closest to position x, y, z.
   */
  public int[] naiveNeighbors(int k, double x, double y, double z, Star lookingFor) {
    List<Star> filteredList = galaxy;
    if (lookingFor != null) {
      filteredList = galaxy.stream().filter(entry -> entry != lookingFor)
          .collect(Collectors.toList());
    }
    if (k < 0) {
      return null;
    }
    Collections.shuffle(filteredList, new Random(System.nanoTime()));
    filteredList.sort((s1, s2) -> {
      double s1Distance = s1.distanceFromPoint(x, y, z);
      double s2Distance = s2.distanceFromPoint(x, y, z);
      return Double.compare(s1Distance, s2Distance);
    });

    int endValue = Math.min(k, filteredList.size());

    int[] returnValue = new int[endValue];
    for (int j = 0; j < endValue; j++) {
      returnValue[j] = filteredList.get(j).getStarID();
    }
    return returnValue;
  }

  /**
   *
   * @return the galaxy.
   */
  public List<Star> getGalaxy() {
    return galaxy;
  }

  /**
   *
   * @param galaxy set the galaxy to input.
   */
  public void setGalaxy(List<Star> galaxy) {
    this.galaxy = galaxy;
  }
}
