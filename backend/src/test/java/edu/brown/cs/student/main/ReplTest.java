package edu.brown.cs.student.main;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

public class ReplTest {

  Repl repl;

  @Test
  public void createCommandTest() {
    repl = new Repl(2);
    StarCommander commander = new StarCommander();
    repl.createCommand("command", commander);
    HashMap<String, CommandAcceptor> commands = repl.getCommands();
    assertTrue(commands.containsKey("command"));
    assertTrue(commands.get("command").equals(commander));
  }

  @Test
  public void setAndGetStaticVars() {
    repl = new Repl(2);
    assertFalse(Repl.isHeadersLoaded());
    assertFalse(Repl.isStudentDataLoaded());
    Repl.setStudentDataLoaded(true);
    Repl.setHeadersLoaded(true);
    assertTrue(Repl.isHeadersLoaded());
    assertTrue(Repl.isStudentDataLoaded());
  }

  @Test
  public void createRecommenderCommander() {
    repl = new Repl(2);
    assertTrue(Repl.getRecommenderCommander() == null);
    repl.createNewCommands();
    assertTrue(Repl.getRecommenderCommander() != null);
  }
}

