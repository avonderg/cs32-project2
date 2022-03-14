package client;


/**
 * This simple class is for reading the API Key from your secret file
 * (THAT SHOULD NOT BE PUSHED TO GIT).
 * Credit: Got code from cs 32 api lab.
 */
public class ClientAuth {

  /**
   * Reads the API Key from the secret text file where we have stored it.
   * Refer to the handout for more on security
   * practices.
   *
   * @return a String of the api key.
   */
  public String getApiKey() {
    FileParser parser = new FileParser("config/secret/apikey.txt");
    return parser.readNewLine();
  }

  /**
   * Reads the CS LOGIN from the secret text file where we have stored it.
   * @return a string of the cs login.
   */
  public String getCSLogin() {
    FileParser parser = new FileParser("config/secret/cslogin.txt");
    return parser.readNewLine();
  }

}
