package starstructures;
import cvsprocessing.Galaxy;
import cvsprocessing.Star;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
public class GalaxyTest {

  String[] list1 = {"1", "1", "0", "0", "0"};
  String[] list2 = {"2", "2", "10", "10", "10"};
  String[] list3 = {"3", "3", "25", "20", "20"};
  Star star1 = new Star(list1);
  Star star2 = new Star(list2);
  Star star3 = new Star(list3);

  @Test
  public void testDistance() {
    Galaxy testGalaxy = new Galaxy();
    int length = testGalaxy.size();
    assertEquals(0, length);
    testGalaxy.addStar(star1);
    testGalaxy.addStar(star2);
    testGalaxy.addStar(star3);
    length = testGalaxy.size();
    assertEquals(3, length);
  }

  @Test
  public void testNaiveNeighbors() {
    Galaxy testGalaxy = new Galaxy();
    testGalaxy.addStar(star1);
    testGalaxy.addStar(star2);
    testGalaxy.addStar(star3);
    int[] result = testGalaxy.naiveNeighbors("2", "0", "0", "0");
    int[] output = {1, 2};
    assertArrayEquals(result, output);
    result = testGalaxy.naiveNeighbors("0", "0", "0", "0");
    assertArrayEquals(result, new int[0]);
    result = testGalaxy.naiveNeighbors("2", "10", "10", "10");
    output = new int[] {2, 1};
    assertArrayEquals(result, output);
  }

  @Test
  public void testNaiveNeighbors2() {
    Galaxy testGalaxy = new Galaxy();
    testGalaxy.addStar(star1);
    testGalaxy.addStar(star2);
    testGalaxy.addStar(star3);
    int[] result = testGalaxy.naiveNeighbors("2", "\"1\"");
    int[] output = {2, 3};
    assertArrayEquals(result, output);
    result = testGalaxy.naiveNeighbors("1", "\"2\"");
    assertArrayEquals(result, new int[] {1});
    result = testGalaxy.naiveNeighbors("0", "\"1\"");
    assertArrayEquals(result, new int[0]);
    result = testGalaxy.naiveNeighbors("-1", "\"1\"");
    assertArrayEquals(result, null);
    result = testGalaxy.naiveNeighbors("1", "\"4\"");
    assertArrayEquals(result, new int[0]);
  }

  @Test
  public void testNaiveNeighbors3() {
    Galaxy testGalaxy = new Galaxy();
    testGalaxy.addStar(star1);
    testGalaxy.addStar(star2);
    testGalaxy.addStar(star3);
    int[] result = testGalaxy.naiveNeighbors(new String[] {"naive_neighbors", "2", "\"1\""});
    assertArrayEquals(result, new int[] {2, 3});
    result = testGalaxy.naiveNeighbors(new String[] {"naive_neighbors", "1", "\"2\""});
    assertArrayEquals(result, new int[] {1});
    result = testGalaxy.naiveNeighbors(new String[] {"naive_neighbors", "0", "\"1\""});
    assertArrayEquals(result, new int[0]);
    result = testGalaxy.naiveNeighbors(new String[] {"naive_neighbors", "2", "10", "10", "10"});
    assertArrayEquals(result, new int[] {2, 1});
    result = testGalaxy.naiveNeighbors(new String[] {"Hello"});
    assertArrayEquals(result, null);
  }

}