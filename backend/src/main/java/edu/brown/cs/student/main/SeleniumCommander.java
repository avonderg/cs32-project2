package edu.brown.cs.student.main;

import selenium.SeleniumTest;

/**
 * Commander Class tests live table.
 */
public class SeleniumCommander implements CommandAcceptor {

    /**
     * Handles the command using the inputted array of Strings from command line.
     *
     * @param input from cmd line.
     */
  @Override
  public void handleCommand(String[] input) {
    if (input[0].equals("run_selenium")) { // command to run selenium tests
      SeleniumTest sRun = new SeleniumTest();
      try {
        sRun.start(); // starts running selenium tests
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
