package edu.brown.cs.student.main;


import databaseloader.TableCommander;
import recommender.RecommenderCommander;
import database.DatabaseCommander;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Repl class represents the repl which will run and execute all of the valid commands that it
 * reads from the terminal/command line.
 * @author Onaphade
 */
public class Repl {

  private HashMap<String, CommandAcceptor> commands;
  private static RecommenderCommander recommenderCommander;
  private static boolean headersLoaded = false;
  private static boolean studentDataLoaded = false;

  /**
   * Repl constructor.
   */
  public Repl() {
    this.commands = new HashMap<String, CommandAcceptor>();
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      boolean flag = true;
      createNewCommands();
      while (flag) {
        String input = reader.readLine();
        if (input != null && input.length() <= 0) {
          System.out.println("ERROR: No command given");
        } else {
          if (input == null) {
            flag = false;
          } else {
            evaluateInput(input);
          }
        }
      }
      reader.close();
    } catch (IOException e) {
      System.out.println("ERROR: Could not close BufferedReader");
    }
  }

  /**
   *
   * @param noLoop inputs a int to make a different repl constructor which does not have a loop
   */
  public Repl(int noLoop) {
    this.commands = new HashMap<String, CommandAcceptor>();
  }

  /**
   * Creates new commands to be added to the hashMap!
   */
  public void createNewCommands() {
    // PROJECT 1
    recommenderCommander = new RecommenderCommander();
    BloomCommander bloomCommander = new BloomCommander(recommenderCommander);
    KDCommander kdCommander = new KDCommander(recommenderCommander);
    StarCommander starCommander = new StarCommander();
    APICommander apiCommander = new APICommander();
    DatabaseCommander dbCommander = new DatabaseCommander();

    createCommand("insert_bf", bloomCommander);
    createCommand("create_bf", bloomCommander);
    createCommand("query_bf", bloomCommander);
    createCommand("load_bf", bloomCommander);
    createCommand("similar_bf", bloomCommander);
    createCommand("load_kd", kdCommander);
    createCommand("similar_kd", kdCommander);
    createCommand("naive_neighbors", starCommander);
    createCommand("stars", starCommander);
    createCommand("headers_load", recommenderCommander);
    createCommand("recsys_load", recommenderCommander);
    createCommand("recommend", recommenderCommander);
    createCommand("active", apiCommander);
    createCommand("api", apiCommander);
    createCommand("api_aggregate", apiCommander);
    createCommand("load_db_data", dbCommander);
    createCommand("load_db_horoscopes", dbCommander);
    createCommand("load_db_zoo", dbCommander);
    createCommand("query", dbCommander);


    // PROJECT 2
    TableCommander tableCommander = new TableCommander();
    createCommand("load_db", tableCommander);

  }

  /**
   * Method dedicated to evaluating REPL input.
   *
   * @param input which was read from command line by buffered reader.
   */
  public void evaluateInput(String input) {
    try {
      // split the input on spaces not in commas,
      // then get the first element which will be the command
      // REGEX logic from stack overflow post
      // https://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-
      // not-surrounded-by-single-or-doubledouble/366532#366532

      ArrayList list = new ArrayList();
      Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
      Matcher regexMatcher = regex.matcher(input);
      while (regexMatcher.find()) {
        list.add(regexMatcher.group());
      }

      String[] splitOnSpace = (String[]) list.toArray(new String[0]);
      String command = splitOnSpace[0];

      //check if the command is contained in the hashMap of commands. If it is not throw an error.
      // if it is in the hashMap then
      if (this.commands.containsKey(command)) {
        this.commands.get(command).handleCommand(splitOnSpace);
      } else {
        System.out
            .println("ERROR: THERE IS NO COMMAND LIKE THE INPUT RECEIVED. Consider Creating One!");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
      System.out.println("ERROR: Ran into issues when evaluating the input!");
    }
  }

  /**
   * Method which will create new commands when called.
   *
   * @param command  keyword
   * @param acceptor something that accepts commands.
   */
  public void createCommand(String command, CommandAcceptor acceptor) {
    this.commands.put(command, acceptor);
  }

  /**
   *
   * @return commands
   */
  public HashMap<String, CommandAcceptor> getCommands() {
    return commands;
  }

  /**
   *
   * @return the recommenderCommander
   */
  public static RecommenderCommander getRecommenderCommander() {
    return recommenderCommander;
  }

  /**
   *
   * @param recommenderCommander to be changed
   */
  public static void setRecommenderCommander(RecommenderCommander recommenderCommander) {
    Repl.recommenderCommander = recommenderCommander;
  }

  /**
   *
   * @return if headers have been loaded.
   */
  public static boolean isHeadersLoaded() {
    return headersLoaded;
  }

  /**
   *
   * @param headersLoaded new value for headersLoaded
   */
  public static void setHeadersLoaded(boolean headersLoaded) {
    Repl.headersLoaded = headersLoaded;
  }

  /**
   *
   * @return if student data has been loaded.
   */
  public static boolean isStudentDataLoaded() {
    return studentDataLoaded;
  }

  /**
   *
   * @param studentDataLoaded new value for studentDataLoaded
   */
  public static void setStudentDataLoaded(boolean studentDataLoaded) {
    Repl.studentDataLoaded = studentDataLoaded;
  }
}
