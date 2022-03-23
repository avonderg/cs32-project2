package databaseloader;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class LoadTableTest {

//  @Test
//  public void testLoadNullPath() {
//    Exception e = assertThrows(
//        IllegalArgumentException.class,
//        () -> new TableLoader(null));
//
//    assertEquals(
//        "ERROR: Cannot connect to database.",
//        e.getMessage());
//  }
//
//  @Test
//  public void testInvalidFiletype() {
//    Exception e = assertThrows(
//        IllegalArgumentException.class,
//        () -> new TableLoader("../data/test.txt"));
//
//    assertEquals(
//        "ERROR: Cannot connect to database.",
//        e.getMessage());
//  }

  @Test
  public void testFileNotExist() {
//    Exception e = assertThrows(
//        IllegalArgumentException.class,
//        () -> new TableLoader("../data/test.sqlite3"));
//
//    assertEquals(
//        "ERROR: File does not exist.",
//        e.getMessage());
  }

}
