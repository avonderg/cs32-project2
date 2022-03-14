package cvsprocessing;

import java.util.List;

/**
 * Class which implements object creator and makes HeaderPairs from a file containing header names
 * and their respective types.
 * @author Onaphade
 */

public class HeaderPairCreator implements ObjectCreator<HeaderPair> {

  private HeaderPair headerPair;

  /**
   * createObject method of headerPair which creates a headerPair from an input row from a CSV.
   * Because the description of the header (qualitative or quantitative) can be misspelled or
   * malformed in many ways this method uses a String comparison helper method to determine if the
   * description of the header is qualitative (type is false) or quantitative (type is true).
   * @param headers of the CSV
   * @param input the name and type of header.
   * @return the created HeaderPair
   */
  public HeaderPair createObject(List<String> headers, String[] input) {
    String name = input[0];
    String description = input[1];
    boolean type = true;

    String qual = "qualitative";
    String quant = "quantitative";

    // Calculating the similarity between the given input string and the options (qual and quant)
    // Found this logic on stack overflow and used it below and in the similiarity() method.
    // Citation: https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java

    double simQual = similarity(qual, description);
    double simQuant = similarity(quant, description);

    if (simQual > simQuant) {
      type = false;
    } else if (simQual < simQuant) {
      type = true;
    } else {
      System.out.println("ERROR: HEADER TYPE DESCRIPTION TOO SIMILAR "
          + "TO QUANTITATIVE AND QUALITATIVE");
    }

    headerPair = new HeaderPair(name, type);
    return headerPair;
  }

  /**
   * Calculates the similarity (a number within 0 and 1) between two strings.
   * @param s1 first string (qual or quant)
   * @param s2 second string (type to compare to qual or quant
   * @return similarity score
   */
  public static double similarity(String s1, String s2) {
    String longer = s1, shorter = s2;
    if (s1.length() < s2.length()) { // longer should always have greater length
      longer = s2; shorter = s1;
    }
    int longerLength = longer.length();
    if (longerLength == 0) {
      return 1.0; /* both strings are zero length */
    }
    return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
  }

  // Example implementation of the Levenshtein Edit Distance
  // See http://rosettacode.org/wiki/Levenshtein_distance#Java

  /**
   * Method to edit distance.
   * @param s1 first input String
   * @param s2 second input String
   * @return returns the new distance
   */
  public static int editDistance(String s1, String s2) {
    s1 = s1.toLowerCase();
    s2 = s2.toLowerCase();

    int[] costs = new int[s2.length() + 1];
    for (int i = 0; i <= s1.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0) {
          costs[j] = j;
        } else {
          if (j > 0) {
            int newValue = costs[j - 1];
            if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
              newValue = Math.min(Math.min(newValue, lastValue),
                  costs[j]) + 1;
            }
            costs[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0) {
        costs[s2.length()] = lastValue;
      }
    }
    return costs[s2.length()];
  }
}
