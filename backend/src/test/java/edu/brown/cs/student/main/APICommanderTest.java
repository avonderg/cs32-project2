package edu.brown.cs.student.main;

import junit.framework.TestCase;
import org.junit.Test;

public class APICommanderTest {
  APICommander commander = new APICommander();
  @Test
  public void activeInfoTest() {
    String[] input = {"active", "info"};
    commander.handleCommand(input);
  }

  @Test
  public void activeMatchTest() {
    String[] input = {"active", "match"};
    commander.handleCommand(input);
  }

  @Test
  public void apiTest() {
    String[] input = {"api", "GET", "https://studentinfoapi.herokuapp.com/info-four",
        "url:auth=sanand;url:key=ADYCJAQQSHF"};
    commander.handleCommand(input);
  }

  @Test
  public void apiAggregatorInfoTest() {
    String[] input = {"api_aggregate", "info"};
    commander.handleCommand(input);
  }

  @Test
  public void apiAggregatorMatchTest() {
    String[] input = {"api_aggregate", "match"};
    commander.handleCommand(input);
  }
}