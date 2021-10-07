package cs601.project2.AppCommon;

/**
 * Displays messages to the system console.
 *
 * @author Jason McGowan
 */
public class ConsoleUi implements UserInterface {

  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }
}
