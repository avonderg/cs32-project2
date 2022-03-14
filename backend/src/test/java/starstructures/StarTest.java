package starstructures;
import cvsprocessing.Star;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StarTest {
  private double delta = 0.01;

  @Test
  public void testDistance1() {
    String[] list = {"1", "1", "0", "0", "0"};
    Star star1 = new Star(list);
    double output = star1.distanceFromPoint(1, 0, 0);
    assertEquals(1, output, delta);
  }

  @Test
  public void testDistance2() {
    String[] list = {"2", "2", "10.0", "5.7", "3.2"};
    Star star2 = new Star(list);
    double output = star2.distanceFromPoint(5.0, 2.0, 1.0);
    assertEquals(6.597727, output, delta);
  }

  @Test
  public void testDistance3() {
    String[] list = {"3", "3", "-10.0", "-15.7", "3.2"};
    Star star3 = new Star(list);
    double output = star3.distanceFromPoint(5.0, 2.0, 1.0);
    assertEquals(23.30515, output, delta);
  }
}