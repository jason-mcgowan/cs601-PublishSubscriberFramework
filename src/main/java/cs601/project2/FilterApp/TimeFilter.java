package cs601.project2.FilterApp;

/**
 * {@link #main(String[])}.
 *
 * @author Jason McGowan
 */
public final class TimeFilter {

  private TimeFilter() {
  }

  /**
   * Takes as input any number of 5-core Amazon reviews and filters them into an old and new text
   * file based on the given unix filter time. Will abort if any input file names are invalid or if
   * either of the output files already exist. Time is the unix time to filter between old and new,
   * and must be parsable to long. Default broker is synchronous, ordered.
   *
   * <p>Optional flags are:
   *
   * <p>-ao (runs an asynchronous, ordered dispatch broker)
   *
   * <p>-au (runs an asynchronous, unordered dispatch broker)
   *
   * @param args Of the form: -input file1 -input file2 -input... -recent recentFileName -old
   *             oldFileName -time 1234567890
   */
  public static void main(String[] args) {
    Services.getInstance().setupEnvironment(args);
    Services.getInstance().getProgram().execute();
  }
}
