package cvsprocessing;

import bloomfilter.BloomInsertable;
import com.google.gson.annotations.SerializedName;
import edu.brown.cs.student.main.NumericData;
import edu.brown.cs.student.main.Repl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Student class represents a student to be put in a group based on their key characteristics which
 * are represented as instance variables.
 * @author Onaphade
 */
public class Student implements BloomInsertable, NumericData {

  @SerializedName("id")

  private int id;

  @SerializedName("name")
  private String name;

  @SerializedName("years_experience")
  private int yearsExp;
  private final int yearsExpIndex = 8;

  @SerializedName("communication_style")
  private String communicationStyle;
  private final int comStyleIndex = 9;

  @SerializedName("meeting_style")
  private String meetingStyle;
  private final int meetingStyleIndex = 11;

  @SerializedName("weekly_avail_hours")
  private int weeklyHours;
  private final int weeklyHoursIndex = 10;

  @SerializedName("meeting_time")
  private String meetingTime;
  private final int meetingTimeIndex = 12;

  private int softwareEngineeringConfidence;
  private final int softwareEngConIndex = 13;
  private String[] strengths;
  private final int strenIndex = 14;
  private String[] weaknesses;
  private final int weakIndex = 15;
  private String[] skills;
  private final int skillIndex = 16;
  private String[] interests;
  private final int interIndex = 17;
  private int numNumericFields;
  private List<HeaderPair> pairs;


  /**
   * Constructor of the Student class. Takes in the student parameters and then assigns them to the
   * local instance vars.
   * @param studentChars takes in list of student character traits and takes out the important ones
   *                     and stores them in local variables.
   */
  public Student(String[] studentChars) {
    try {
      initializeHeaders();
      id = Integer.parseInt(studentChars[0]);
      name = studentChars[1];
      yearsExp = Integer.parseInt(studentChars[yearsExpIndex]);
      communicationStyle = studentChars[comStyleIndex];
      weeklyHours = Integer.parseInt(studentChars[weeklyHoursIndex]);
      meetingStyle = studentChars[meetingStyleIndex];
      meetingTime = studentChars[meetingTimeIndex];
      softwareEngineeringConfidence = Integer.parseInt(studentChars[softwareEngConIndex]);
      strengths = studentChars[strenIndex].split(",");
      weaknesses = studentChars[weakIndex].split(",");
      skills = studentChars[skillIndex].split(",");
      interests = studentChars[interIndex].split(",");
      numNumericFields = 3;
    } catch (NumberFormatException e) {
      System.out.println("ERROR: Input cannot be parsed to double");
    } catch (NullPointerException e) {
      System.out.println("ERROR: The headers have not been loaded!s");
    }
  }

  /**
   * Alternate constructor.
   */
  public Student() {
    interests = null;
    skills = null;
    numNumericFields = 3;
  }

  /**
   * Method to initializeHeaders.
   */
  public void initializeHeaders() {
    if (Repl.isHeadersLoaded()) {
      pairs = Repl.getRecommenderCommander().getPairs();
    }
//    } else {
//      System.out.println("ERROR: THE HEADERS HAVE NOT BEEN LOADED!!!");
//    }
  }

  /**
   * function to extract necessary numeric values from a student.
   * @return an array of doubles representing the numeric fields in a student object
   */
  public double[] extractNumeric() {
    double[] dataPoints = new double[numNumericFields];
    dataPoints[0] = this.getYearsExp();
    dataPoints[1] = this.getWeeklyHours();
    dataPoints[2] = this.getSoftwareEngineeringConfidence();
    return dataPoints;
  }

  /**
   *
   * @return weeklyHours
   */
  public int getWeeklyHours() {
    return weeklyHours;
  }

  /**
   *
   * @param weeklyHours set weeklyHours instance variable
   */
  public void setWeeklyHours(int weeklyHours) {
    this.weeklyHours = weeklyHours;
  }

  /**
   *
   * @return the id
   */
  @Override
  public int getID() {
    return id;
  }

  /**
   *
   * @param iD to be set
   */
  public void setID(int iD) {
    this.id = iD;
  }

  /**
   *
   * @return the student name
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name to be set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * @return yearsExp
   */
  public int getYearsExp() {
    return yearsExp;
  }

  /**
   *
   * @param yearsExp to be set
   */
  public void setYearsExp(int yearsExp) {
    this.yearsExp = yearsExp;
  }

  /**
   *
   * @return communicationStyle
   */
  public String getCommunicationStyle() {
    return communicationStyle;
  }

  /**
   *
   * @param communicationStyle to be set
   */
  public void setCommunicationStyle(String communicationStyle) {
    this.communicationStyle = communicationStyle;
  }

  /**
   *
   * @return meetingStyle
   */
  public String getMeetingStyle() {
    return meetingStyle;
  }

  /**
   *
   * @param meetingStyle to be set
   */
  public void setMeetingStyle(String meetingStyle) {
    this.meetingStyle = meetingStyle;
  }

  /**
   *
   * @return string containing values of all important instance variables.
   */
  public String toStr() {
    return "Student{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", yearsExp=" + yearsExp
        + ", communicationStyle='" + communicationStyle + '\''
        + ", meetingStyle='" + meetingStyle + '\''
        + ", weeklyHours=" + weeklyHours
        + ", meetingTime='" + meetingTime + '\''
        + ", softwareEngineeringConfidence=" + softwareEngineeringConfidence
        + ", strengths=" + Arrays.toString(strengths)
        + ", weaknesses=" + Arrays.toString(weaknesses)
        + ", skills=" + Arrays.toString(skills)
        + ", interests=" + Arrays.toString(interests)
        + '}';
  }

  /**
   *
   * @return meetingTime
   */
  public String getMeetingTime() {
    return meetingTime;
  }

  /**
   *
   * @param meetingTime to be set
   */
  public void setMeetingTime(String meetingTime) {
    this.meetingTime = meetingTime;
  }

  /**
   *
   * @return softwareEngineeringConfidence
   */
  public int getSoftwareEngineeringConfidence() {
    return softwareEngineeringConfidence;
  }

  /**
   *
   * @param softwareEngineeringConfidence to be set
   */
  public void setSoftwareEngineeringConfidence(int softwareEngineeringConfidence) {
    this.softwareEngineeringConfidence = softwareEngineeringConfidence;
  }

  /**
   *
   * @return strengths
   */
  public String[] getStrengths() {
    return strengths;
  }

  /**
   *
   * @param strengths to be set
   */
  public void setStrengths(String[] strengths) {
    this.strengths = strengths;
  }

  /**
   *
   * @return weaknesses
   */
  public String[] getWeaknesses() {
    return weaknesses;
  }

  /**
   *
   * @param weaknesses to be set
   */
  public void setWeaknesses(String[] weaknesses) {
    this.weaknesses = weaknesses;
  }

  /**
   *
   * @return skills
   */
  public String[] getSkills() {
    return skills;
  }

  /**
   *
   * @param skills to be set
   */
  public void setSkills(String[] skills) {
    this.skills = skills;
  }

  /**
   *
   * @return interests
   */
  public String[] getInterests() {
    return interests;
  }

  /**
   *
   * @param interests to be set
   */
  public void setInterests(String[] interests) {
    this.interests = interests;
  }

  /**
   * Gets the student text data for bloom filter.
   * @return List of strings that represent the non numeric data of student
   * to be inserted into Bloom Filter.
   */
  @Override
  public List<String> getDataToInsert() {
    List<String> textData = new ArrayList<>();
    textData.add(communicationStyle);
    textData.add(meetingStyle);
    textData.add(meetingTime);

    for (String strength : strengths) {
      textData.add(strength);
    }
    for (String weakness : weaknesses) {
      textData.add(weakness);
    }
    for (String skill : skills) {
      textData.add(skill);
    }
    for (String interest : interests) {
      textData.add(interest);
    }
    return textData;
  }
}
