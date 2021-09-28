package cs601.project2.FilterApp;

public class ArgErrorProgram implements Program {

  private String errorMessage;

  private ArgErrorProgram() {}

  public ArgErrorProgram(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public void execute() {
    Services.getInstance().getUi().displayMessage(errorMessage);
  }
}
