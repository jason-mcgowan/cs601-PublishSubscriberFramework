package cs601.project2.FilterApp;

import java.util.LinkedList;

/**
 * Static methods to help process arguments submitted to a main method.
 *
 * @author Jason McGowan
 */
public final class MainArgsHelper {

  // Private default constructor to prevent instantiation
  private MainArgsHelper() {
  }

  /**
   * Searches for the given flag and returns the immediately following argument. Returns null if the
   * flag does not exist or has no following argument.
   *
   * @param flag The key term to search for
   * @param args Args array from the main method
   * @return The term immediately following flag. If flag is not found or is the last term, returns
   * null.
   */
  public static String getFlagContext(String flag, String[] args) {
    // Checks up to penultimate element for the flag, returning the following argument
    for (int i = 0; i < args.length - 1; i++) {
      if (flag.equalsIgnoreCase(args[i])) {
        return args[i + 1];
      }
    }
    // Didn't find the flag in first length-1 arguments, return null
    return null;
  }

  /**
   * Searches for the given flag and returns the immediately following argument every time it
   * appears.
   *
   * @return All terms immediately following the flag
   */
  public static Iterable<String> getMultiFlagContext(String flag, String[] args) {
    LinkedList<String> result = new LinkedList<>();
    for (int i = 0; i < args.length - 1; i++) {
      if (flag.equalsIgnoreCase(args[i])) {
        result.add(args[i + 1]);
      }
    }
    return result;
  }

  /**
   * Returns true when the arguments array contains the flag.
   *
   * @param flag Flag to check for
   * @param args Arguments to check
   * @return Whether flag exists
   */
  public static boolean checkForFlag(String flag, String[] args) {
    for (String arg : args) {
      if (arg.equalsIgnoreCase(flag)) {
        return true;
      }
    }
    return false;
  }
}
