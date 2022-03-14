package cvsprocessing;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StarCreatorTest {

  private StarCreator creator;

  @Test
  public void testStarCreator(){
    creator = new StarCreator();
    List<String> headers = new ArrayList<String>();
    headers.add("StarID");
    headers.add("ProperName");
    headers.add("X");
    headers.add("Y");
    headers.add("Z");
    String[] chars = {"1", "Cool Star", "2.9", "3.982", "2.443"};
    Star star = creator.createObject(headers, chars);

    assertEquals(star.getStarID(),1);
    assertEquals(star.getProperName(),"Cool Star");
    assertEquals(star.getX(),2.9, .001);
    assertEquals(star.getY(),3.982, .001);
    assertEquals(star.getZ(),2.443, .001);
  }

}
