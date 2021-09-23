package cs601.project2.FilterApp;

public final class Services {

  private static Services instance;

  private Services() {
  }

  public static synchronized Services getInstance() {

    if (instance == null) {
      instance = new Services();
    }
    return instance;

  }

  public synchronized void setupEnvironment(String[] args) {

  }
}
