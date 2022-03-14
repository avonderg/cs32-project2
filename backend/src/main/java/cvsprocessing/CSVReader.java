package cvsprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSVReader class has 2 main functions. First, it takes in the CSV, parses it, and checks the
 * parsed result for formatting issues. In parsing the CSV the CSVReader classes uses an abstract
 * implementation, such that it can use the csv to create any type of object, only requiring an
 * appropriate StudentCreator.
 * @param <T> generic type representing the type of object which the CSV Reader will be creating.
 * @author Om Naphade
 * @version Version 3 Week 2
 */

public class CSVReader<T> {

  private String fileLocation;
  private ObjectCreator<T> creator;
  private List<T> objects;
  private List<String> headers;
  private List<HeaderPair> pairs;

  /**
   * Constructor for CSVReader... Reads all the lines from the csv and removes the first line
   * which is the set of headers. Also checks the read data for validity and then takes each line
   * and uses the objectCreator to make an object and add it to the list of objects.
   * @param file (csv)
   * @param objectCreator The object creator which will be used to make a new object of type T for
   *                      each line of the CSV.
   * @throws IOException for file issues
   */
  public CSVReader(String file, ObjectCreator<T> objectCreator) throws IOException {
    fileLocation = file;
    objects = new ArrayList<T>();
    ArrayList<String[]> data = new ArrayList();
    String line = "";
    creator = objectCreator;

    //citation: Modeled my Buffered Reader mechanism from https://www.javatpoint.com/how-to-read-csv-file-in-java
    BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
    while ((line = reader.readLine()) != null) {
      data.add(splitLine(line));
    }
    headers = Arrays.asList(data.get(0));
    data.remove(0);

    // creates a new object for each new line of data, passing in the csv row where the data
    // is further parsed in the constructor of whatever implements the ObjectCreator Interface.
    for (int i = 0; i < data.size(); i++) {
      T toAdd = creator.createObject(headers, data.get(i));
      if (toAdd != null) {
        objects.add(toAdd);
      }
    }
  }

  /**
   *
   * @param line line to be split on commas not contained in quotes
   * @return the line split on commas not contained in quotation marks...
   * citation: I got the regex statements from the following online source...
   * https://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
   */
  public String[] splitLine(String line) {
    String[] array;
    array = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    return array;
  }


  /**
   *
   * @return boolean depending on if the file location exists.
   */
  public boolean checkLoc() {
    try {
      String line = "";
      BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
      while ((line = reader.readLine()) != null) {
        int x = 2;
      }
      return true;
    } catch (IOException e) {
      System.out.println("ERROR: File Not Found");
      return false;
    }
  }

  /**
   *
   * @return objects (list of objects created from given csv)
   */
  public List<T> getObjects() {
    return objects;
  }
}
