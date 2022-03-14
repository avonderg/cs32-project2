package cvsprocessing;

/**
 * Header Pair class which represents a pair containing the name of the header and its type.
 * (quantitative or qualitative)
 * @author Onaphade
 */
public class HeaderPair {

  private String headerName;
  private Boolean type;

  /**
   * Constructor.
   * @param name to be set as headerName
   * @param t type, true if quantitative and false if qualitative
   */
  public HeaderPair(String name, Boolean t) {
    headerName = name;
    type = t;
  }

  /**
   *
   * @return headerName
   */
  public String getHeaderName() {
    return headerName;
  }

  /**
   *
   * @param headerName to be set as variable.
   */
  public void setHeaderName(String headerName) {
    this.headerName = headerName;
  }

  /**
   *
   * @return type of header (true = quantitative, false = qualitative).
   */
  public Boolean getType() {
    return type;
  }

  /**
   *
   * @param type to be set as type for header.
   */
  public void setType(Boolean type) {
    this.type = type;
  }
}
