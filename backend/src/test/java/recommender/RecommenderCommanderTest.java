package recommender;

import edu.brown.cs.student.main.Repl;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecommenderCommanderTest {

  Repl repl;

  @Test
  public void testHandlingCommands() {
    repl = new Repl(2);
    repl.createNewCommands();
    assertTrue(Repl.getRecommenderCommander() != null);
    RecommenderCommander cmdr = Repl.getRecommenderCommander();
    String[] input1 = {"headers_load", "data/csv/header_types.csv"};
    String[] input2 = {"recsys_load", "CSV", "data/project1/proj1_small.csv"};
    String[] input3 = {"recsys_load", "API-DB", "data/sql/data.sqlite3"};
    String[] input4 = {"recommend", "3", "5"};

    assertTrue(cmdr.getHeaderReader() == null);
    cmdr.handleCommand(input1);
    assertFalse(cmdr.getHeaderReader() == null);

    assertTrue(cmdr.getRecommender() == null);
    assertTrue(cmdr.getStudents() == null);
    cmdr.handleCommand(input2);

    assertFalse(cmdr.getRecommender() == null);
    assertFalse(cmdr.getStudents() == null);
    assertTrue(cmdr.getStudents().size() == 20);

    cmdr.handleCommand(input3);

    assertFalse(cmdr.getRecommender() == null);
    assertFalse(cmdr.getStudents() == null);
    assertTrue(cmdr.getStudents().size() == 60);

    cmdr.handleCommand(input4);
  }
}
