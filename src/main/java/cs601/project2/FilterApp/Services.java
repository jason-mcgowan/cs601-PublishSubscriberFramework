package cs601.project2.FilterApp;

public final class Services {

  private UserInterface ui;
  private Program program;

  private Services() {
    ui = new ConsoleUi();
    program = new ArgErrorProgram("Environment not initialized");
  }

  public static synchronized Services getInstance() {
    return InstanceHolder.instance;
  }

  public UserInterface getUi() {
    return ui;
  }

  public Program getProgram() {
    return program;
  }

  public synchronized void setupEnvironment(String[] args) {
    program = Programs.getProgram(args);
  }

  private static final class InstanceHolder {
    private static final Services instance = new Services();
  }
}
