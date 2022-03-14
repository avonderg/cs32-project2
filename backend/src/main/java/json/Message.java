package json;

/**
 * Simple Message class for interacting with JSON parsers.
 * Credit: code for this class was found in the api-lab for Brown CS32.
 */
public class Message {
  private String message;

  /**
   * Simple constructor.
   *
   * @param message the message extracted from the JSON object.
   */
  public Message(String message) {
    this.message = message;
  }

  /**
   * Returns the extracted message.
   *
   * @return the extracted message.
   */
  public String getMessage() {
    return this.message;
  }
}
