package cs601.project2.FilterApp;

import cs601.project2.AppCommon.ArgErrorProgram;
import cs601.project2.AppCommon.MainArgsHelper;
import cs601.project2.AppCommon.Program;
import cs601.project2.Framework.AsyncOrderedDispatchBroker;
import cs601.project2.Framework.AsyncUnorderedDispatchBroker;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.SynchronousOrderedDispatchBroker;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Contains factory method for obtaining a Program.
 *
 * @author Jason McGowan
 */
public final class Programs {

  private static final String INPUT = "-input";
  private static final String OLD_FILE = "-old";
  private static final String RECENT_FILE = "-recent";
  private static final String ASYNC_ORDERED = "-ao";
  private static final String ASYNC_UNORDERED = "-au";
  private static final String TIME = "-time";

  private Programs() {
  }

  public static Program getProgram(String[] args) {
    Broker<Review> broker = getBroker(args);
    String oldFile = MainArgsHelper.getFlagContext(OLD_FILE, args);
    String recentFile = MainArgsHelper.getFlagContext(RECENT_FILE, args);
    String timeString = MainArgsHelper.getFlagContext(TIME, args);
    Iterable<String> inputs = MainArgsHelper.getMultiFlagContext(INPUT, args);

    try {
      verifyArgsExist(oldFile, recentFile, timeString, inputs);
    } catch (IllegalArgumentException e) {
      return new ArgErrorProgram(e.getLocalizedMessage());
    }

    long time = 0;
    try {
      time = Long.parseLong(timeString);
    } catch (NumberFormatException e) {
      return new ArgErrorProgram("Filter time not parsable to long: " + timeString);
    }

    return new FilterReviewsByTimeProgram(inputs, recentFile, oldFile, time, broker);
  }

  private static void verifyArgsExist(String oldFile, String recentFile, String timeString,
      Iterable<String> inputs) throws IllegalArgumentException {
    if (oldFile == null || recentFile == null || timeString == null) {
      throw new IllegalArgumentException("Not all mandatory flags provided");
    }
    if (Files.exists(Paths.get(oldFile))) {
      throw new IllegalArgumentException("Error: Old file exists: " + oldFile);
    }
    if (Files.exists(Paths.get(recentFile))) {
      throw new IllegalArgumentException("Error: Recent file exists: " + recentFile);
    }
    int inputCount = 0;
    for (String input : inputs) {
      inputCount++;
      if (!Files.exists(Paths.get(input))) {
        throw new IllegalArgumentException("Input file does not exist: " + input);
      }
    }
    if (inputCount == 0) {
      throw new IllegalArgumentException("Error, no " + INPUT + " flags found");
    }
  }

  private static Broker<Review> getBroker(String[] args) {
    Broker<Review> broker;
    if (MainArgsHelper.checkForFlag(ASYNC_ORDERED, args)) {
      broker = new AsyncOrderedDispatchBroker<>();
    } else if (MainArgsHelper.checkForFlag(ASYNC_UNORDERED, args)) {
      broker = new AsyncUnorderedDispatchBroker<>();
    } else {
      broker = new SynchronousOrderedDispatchBroker<>();
    }
    return broker;
  }
}
