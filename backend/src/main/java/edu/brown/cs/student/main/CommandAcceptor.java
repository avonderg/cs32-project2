package edu.brown.cs.student.main;

/**
 * Interface which represents the tye of class which will be able to handle a generic command when
 * given the command line input that was given when the command was called. Has handleCommand()
 * method to handle the command.
 * @author Onaphade
 */
public interface CommandAcceptor {

  /**
   * Handles the command using the inputted array of Strings from command line.
   *
   * @param input from cmd line.
   */
  void handleCommand(String[] input);

}


