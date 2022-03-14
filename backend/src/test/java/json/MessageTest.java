package json;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTest{

  @Test
  public void messageTest() {
    Message message = new Message("hello");
    assertEquals("hello", message.getMessage());
  }

}