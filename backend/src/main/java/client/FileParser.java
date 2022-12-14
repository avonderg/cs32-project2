package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A generic file parser.
 * Credit: CS 32 code from api-lab
 */
public class FileParser {

  private BufferedReader bufRead = null;

  /**
   * A FP constructor.
   *
   * @param file - a String file path
   */
  public FileParser(String file) {

    try {
      this.bufRead = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException f) {
      System.out.println("ERROR: File not found");
    }
  }

  /**
   * Reads a new line in the file and returns a String.
   *
   * @return a read String line
   */
  public String readNewLine() {
    if (bufRead != null) {
      try {
        String ln = bufRead.readLine();
        return ln;
      } catch (IOException e) {
        System.out.println("ERROR: Read");
        return null;
      }
    } else {
      return null;
    }
  }
}
