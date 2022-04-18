package edu.brown.cs.student.main;

import selenium.seleniumTest;

public class seleniumCommander implements CommandAcceptor {

  /**
   * Handles the command using the inputted array of Strings from command line.
   *
   * @param input from cmd line.
   */
  @Override
  public void handleCommand(String[] input) {
    if (input[0].equals("run_selenium")) { // command to run selenium tests
      seleniumTest sRun = new seleniumTest();
      try {
        sRun.start(); // starts running selenium tests
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
