package cvsprocessing;

import java.util.List;


/**
 * Interface which serves as a model for something that can create an object of a certain type, T.
 * Has one method, createObject() which has the role of using headers and/or the element in order to
 * create an object using the line of parsed csv info it receives.
 * @author Onaphade
 * @param <T> generic object to be created
 */
public interface ObjectCreator<T> {

  /**
   * The headers of the CSV are passed in along with the elements for each object in each line of
   * the CSV. They can then be used to create each individual object along with the headers. The
   * creation of the object can be done in any way, which is what makes this implementation of the
   * CSV reader using this interface so generalized.
   * @param headers of the CSV
   * @param elements of each row of the CSV to be used to create the new object.
   * @return the object created.
   */
  T createObject(List<String> headers, String[] elements);

}
