package edu.brown.cs.student.main;

import cvsprocessing.Galaxy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StarCommanderTest {
  private StarCommander commander;

  @Test
  public void testHandleCommands() {
    commander = new StarCommander();
    String[] list = {"stars", "data/stars/one-star.csv"};
    commander.handleCommand(list);
    Galaxy galaxy = commander.getGalaxy();
    assertEquals(galaxy.getGalaxy().size(), 1);
    assertEquals(galaxy.getGalaxy().get(0).getStarID(), 1);
    assertEquals(galaxy.getGalaxy().get(0).getProperName(), "Lonely Star");
  }
}
