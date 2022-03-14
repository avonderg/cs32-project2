package cvsprocessing;

/**
 * Class Star. This class represents each star that can be created from the CSV.
 * @author Om Naphade
 * @version Version 5
 */
public class Star {

  private int starID;
  private String properName;
  private double x;
  private double y;
  private double z;

  /**
   * Constructor of Star Class.
   * @param chars list of characteristics for each star.
   */
  public Star(String[] chars) {
    try {
      starID = Integer.parseInt(chars[0]);
      properName = chars[1];
      x = Double.parseDouble(chars[2]);
      y = Double.parseDouble(chars[3]);
      z = Double.parseDouble(chars[4]);
    } catch (NumberFormatException e) {
      System.out.println("ERROR: Input cannot be parsed to double");
    }
  }

  /**
   *
   * @return starID
   */
  public int getStarID() {
    return starID;
  }

  /**
   *
   * @param starID of star
   */
  public void setStarID(int starID) {
    this.starID = starID;
  }

  /**
   *
   * @return properName
   */
  public String getProperName() {
    return properName;
  }

  /**
   *
   * @param properName of star
   */
  public void setProperName(String properName) {
    this.properName = properName;
  }

  /**
   *
   * @return x-value
   */
  public double getX() {
    return x;
  }


  /**
   *
   * @param x of star
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   *
   * @return y-value
   */
  public double getY() {
    return y;
  }

  /**
   *
   * @param y of star
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   *
   * @return z-value
   */
  public double getZ() {
    return z;
  }

  /**
   *
   * @param z of star
   */
  public void setZ(double z) {
    this.z = z;
  }

  /**
   * Function that calculates distance of star from a point.
   * @param xCoord double representing x coord of point.
   * @param yCoord double representing y coord of point.
   * @param zCoord double representing z coord of point.
   * @return double representing distance of star from point.
   */
  public double distanceFromPoint(double xCoord, double yCoord, double zCoord) {
    double xDistance = Math.pow(xCoord - x, 2.0);
    double yDistance = Math.pow(yCoord - y, 2.0);
    double zDistance = Math.pow(zCoord - z, 2.0);
    return Math.sqrt(xDistance + yDistance + zDistance);
  }
}

