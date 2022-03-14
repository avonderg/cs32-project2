package recommender;

import apiaggregator.ApiAggregator;
import apiaggregator.Type;
import client.ClientRequestGenerator;
import client.EndpointRequestGenerator;
import cvsprocessing.CSVReader;
import cvsprocessing.HeaderPair;
import cvsprocessing.HeaderPairCreator;
import cvsprocessing.Student;
import cvsprocessing.StudentCreator;
import database.DatabaseProxy;
import database.Permission;
import edu.brown.cs.student.main.CommandAcceptor;
import edu.brown.cs.student.main.Repl;
import json.APIResponseFormatter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Class which implements the Command Acceptor and is built to handle three commands.
 * 1.) headers_load. 2.) recsys_load 3.) recommend.
 * @author Onaphade
 */
public class RecommenderCommander implements CommandAcceptor {

  private List<HeaderPair> pairs;
  private HashMap<String, Boolean> types;
  private CSVReader headerReader;
  private CSVReader studentReader;
  private List<Student> students;
  private StudentRecommender recommender;

  /**
   * Handle command method which handles the following three commands.
   * 1.) headers_load. 2.) recsys_load 3.) recommend.
   * @param input from cmd line.
   */
  public void handleCommand(String[] input) {
    switch (input[0]) {
      case "headers_load":
        if (input.length != 2) {
          System.out.println("ERROR: Incorrect number of inputs following command");
          break;
        }
        String path = input[1];
        try {
          this.headerReader = new CSVReader(path, new HeaderPairCreator());
        } catch (IOException e) {
          System.out.println("ERROR: could not read file");
        }

        if (headerReader != null) {
          this.pairs = headerReader.getObjects();
          createTypes();
          System.out.println("Loaded header types.");
          Repl.setHeadersLoaded(true);
        }
        break;

      case "recsys_load":
        if (input.length != 3) {
          System.out.println("ERROR: Incorrect number of inputs following command");
          break;
        }

        if (!Repl.isHeadersLoaded()) {
          System.out.println("ERROR: HEADERS NOT LOADED!");
          break;
        }
        String loadType = input[1];
        String path2 = input[2];
        try {
          if (loadType.equals("CSV")) {
            loadRecSysCSV(path2); //will create the student list...
            recommender = new StudentRecommender(students);
          } else if (loadType.equals("API-DB")) {
            loadRecSysApiDB(path2); //will create the student list...
            recommender = new StudentRecommender(students);
          } else {
            System.out.println("ERROR: The type loading type is not valid. Must by CSV or API-DB!");
            break;
          }

          System.out.println("Loaded Recommender with " + students.size() + " student(s).");
          Repl.setStudentDataLoaded(true);

        } catch (Exception e) {
          System.out.println("ERROR: Could not properly execute recsys_load command!");
        }
        break;

      case "recommend":
        if (input.length != 3) {
          System.out.println("ERROR: Incorrect number of inputs following command");
          break;
        }

        if (!Repl.isStudentDataLoaded()) {
          System.out.println("ERROR: STUDENT DATA NOT LOADED!");
          break;
        }

        try {
          int numRecs = Integer.parseInt(input[1]);
          int studentID = Integer.parseInt(input[2]);
          List<Integer> recommendations = recommender.recommend(numRecs, studentID);

          if (recommendations.size() > 0) {
            for (int recommendation: recommendations) {
              System.out.println(recommendation);
            }
          } else {
            System.out.println("ERROR: No Recommendations Could be Returned!");
            break;
          }
        } catch (Exception e) {
          System.out.println("ERROR: Could not implement recommend command");
          break;
        }
        break;

      default:
        System.out.println("ERROR: Did not match expected input");
        break;
    }
  }

  /**
   * Method which handles the command recsys_load when the type of input for loading is a CSV.
   * @param path where the csv is located
   */
  public void loadRecSysCSV(String path) {
    try {
      studentReader = new CSVReader(path, new StudentCreator());
      students = studentReader.getObjects();
    } catch (IOException ioe) {
      System.out.println("ERROR: CSV Reader could not find the path to the CSV");
    }

  }

  /**
   * Method which handles the command recsys_load when the type of input for loading is a API-DB.
   * @param path of sqlite3 file.
   */
  public void loadRecSysApiDB(String path) {
    EndpointRequestGenerator requestGenerator = new ClientRequestGenerator();
    APIResponseFormatter formatter = new APIResponseFormatter();

    ApiAggregator<Student> aggregator =
        new ApiAggregator<Student>(Type.Info, formatter, requestGenerator);
    List<Student> apiStudents = aggregator.aggregate();

    // need to gather database proxy info

    HashMap<Integer, Student> dbStudents = getDBStudents(path);

    // need to add the database proxy info into the list of students...
    // database contains name,

    for (Student student: apiStudents) {
      int id = student.getID();
      Student dbStudent = dbStudents.get(id);
      student.setName(dbStudent.getName());
      student.setInterests(dbStudent.getInterests());
      student.setStrengths(dbStudent.getStrengths());
      student.setWeaknesses(dbStudent.getWeaknesses());
      student.setSkills(dbStudent.getSkills());
    }

    students = apiStudents;
  }

  /**
   * Creates Hashmap of Integer, Student which is the student ID and the Student. This is then
   * used in order to combine the database proxy data from this method to the API aggregator data.
   * @param path of sqlite3 file.
   * @return Hashmap of Integer, Student which is the student ID and the Student.
   */
  public HashMap<Integer, Student> getDBStudents(String path) {
    HashMap<Integer, Student> dbStudents = new HashMap<>();
    Map<String, Permission> dataPermissions = new HashMap<>();
    dataPermissions.put("INTERESTS", Permission.R);
    dataPermissions.put("NAMES", Permission.R);
    dataPermissions.put("SKILLS", Permission.R);
    dataPermissions.put("TRAITS", Permission.R);
    DatabaseProxy db = null;
    try {
      db = new DatabaseProxy(path, dataPermissions);
      // filling database with id, student name and email
      ResultSet namesEmails = db.runCommand("SELECT * FROM NAMES");
      while (namesEmails.next()) {
        Student newStudent = new Student();
        newStudent.setID(namesEmails.getInt(1));
        newStudent.setName(namesEmails.getString(2));
        dbStudents.put(namesEmails.getInt(1), newStudent);
      }
      ResultSet interests = db.runCommand("SELECT * FROM INTERESTS");
      while (interests.next()) {
        Student currStudent = dbStudents.get(interests.getInt(1));
        if (currStudent.getInterests() == null) {
          currStudent.setInterests(new String[] {interests.getString(2)});
        } else {
          String[] oldInterests = currStudent.getInterests();
          String[] newInterests = Arrays.copyOf(oldInterests, oldInterests.length + 1);
          newInterests[oldInterests.length] = interests.getString(2);
        }
      }
      ResultSet skills = db.runCommand("SELECT * FROM SKILLS");
      while (skills.next()) {
        Student currStudent = dbStudents.get(skills.getInt(1));
        if (currStudent.getSkills() == null) {
          currStudent.setSkills(new String[] {skills.getString(2)});
        } else {
          String[] oldSkills = currStudent.getSkills();
          String[] newSkills = Arrays.copyOf(oldSkills, oldSkills.length + 1);
          newSkills[oldSkills.length] = skills.getString(2);
        }
      }
      ResultSet traits = db.runCommand("SELECT * FROM TRAITS");
      while (traits.next()) {
        Student currStudent = dbStudents.get(traits.getInt(1));
        if (traits.getString(2).equalsIgnoreCase("weaknesses")) {
          if (currStudent.getWeaknesses() == null) {
            currStudent.setWeaknesses(new String[] {traits.getString(3)});
          } else {
            String[] oldWeaknesses = currStudent.getWeaknesses();
            String[] newWeaknesses = Arrays.copyOf(oldWeaknesses, oldWeaknesses.length + 1);
            newWeaknesses[oldWeaknesses.length] = traits.getString(3);
          }
        } else if (traits.getString(2).equalsIgnoreCase("strengths")) {
          if (currStudent.getStrengths() == null) {
            currStudent.setStrengths(new String[] {traits.getString(3)});
          } else {
            String[] oldStrengths = currStudent.getStrengths();
            String[] newStrengths = Arrays.copyOf(oldStrengths, oldStrengths.length + 1);
            newStrengths[oldStrengths.length] = traits.getString(3);
          }
        } else {
          System.out.println("ERROR: Neither a strength or weakness??");
        }
      }
    } catch (SQLException e) {
      System.out.println("ERROR: SQL Exception when loading DB");
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: Class exception when loading DB");
    } catch (ExecutionException e) {
      System.out.println("ERROR: Problem querying DB");
    }
    return dbStudents;
  }

  /**
   * Creates the hashmap of header,type pairs.
   */
  public void createTypes() {
    types = new HashMap<String, Boolean>();
    for (int i = 0; i < pairs.size(); i++) {
      HeaderPair pair = pairs.get(i);
      // print statement for testing
      //System.out.println("Printing Header Information...  " + "index: " + i + "  header name: "
          //+ pair.getHeaderName() + " header type (true = quant|false = qual): " + pair.getType());
      types.put(pair.getHeaderName(), pair.getType());
    }

  }

  /**
   *
   * @return types variable
   */
  public HashMap<String, Boolean> getTypes() {
    return types;
  }

  /**
   *
   * @return pairs variable
   */
  public List<HeaderPair> getPairs() {
    return pairs;
  }

  /**
   *
   * @return the headerReader
   */
  public CSVReader getHeaderReader() {
    return headerReader;
  }

  /**
   *
   * @return theStudentReader
   */
  public CSVReader getStudentReader() {
    return studentReader;
  }

  /**
   *
   * @return the students list
   */
  public List<Student> getStudents() {
    return students;
  }

  /**
   *
   * @return the recommender
   */
  public StudentRecommender getRecommender() {
    return recommender;
  }
}
