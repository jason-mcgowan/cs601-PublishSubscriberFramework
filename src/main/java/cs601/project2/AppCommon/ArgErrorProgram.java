package cs601.project2.AppCommon;

import cs601.project2.AppCommon.Program;
import cs601.project2.AppCommon.Services;

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
