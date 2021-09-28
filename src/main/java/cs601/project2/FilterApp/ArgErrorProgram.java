package cs601.project2.FilterApp;

/**
 * Program to display an error if the program environment was not correctly established.
 *
 * @author Jason McGowan
 */
public class ArgErrorProgram implements Program {

  private String errorMessage;

  private ArgErrorProgram() {
  }

  public ArgErrorProgram(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public void execute() {
    Services.getInstance().getUi().displayMessage(errorMessage);
  }
}
