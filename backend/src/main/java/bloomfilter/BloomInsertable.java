package bloomfilter;

import java.util.List;

/**
 * Interface meant for use for objects that can be inserted into Bloom Filter Database.
 * Need getId for the hashmap in BloomFilterDatabase and need String data to extract for text.
 * @author Suraj Anand
 */
public interface BloomInsertable {
  /**
   * Getter for ID variable in class.
   * @return integer representing the id.
   */
  int getID();

  /**
   * Gets the data to insert for the Bloom Insertable.
   * @return List of Strings that are data to be inserted into Bloom Filter.
   */
  List<String> getDataToInsert();
}
