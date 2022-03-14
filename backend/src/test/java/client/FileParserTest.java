package client;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileParserTest {

  @Test
  public void parseFileTest() {
    FileParser parser = new FileParser("config/secret/cslogin.txt");
    assertEquals("sanand14", parser.readNewLine()) ;
  }

}