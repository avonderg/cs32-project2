package cvsprocessing;

import java.util.List;

/**
 * Class responsible for creating the star using the star's constructor. The starCreator
 * class implements the objectCreator interface and therefore also implements the method
 * createObject() method where the star is created!
 * @author Onaphade
 */
public class StarCreator implements ObjectCreator<Star> {
  private Star star;

  /**
   * Creates star using the characters provided. Headers are not needed in this case, but are
   * passed in for other versions of ObjectCreator implementation.
   * @param headers of the CSV
   * @param starChars of each star to be created.
   * @return the newly created star
   */
  public Star createObject(List<String> headers, String[] starChars) {
    star = new Star(starChars);
    return star;
  }
}
