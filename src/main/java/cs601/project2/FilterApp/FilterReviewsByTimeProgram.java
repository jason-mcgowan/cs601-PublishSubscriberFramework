package cs601.project2.FilterApp;

import cs601.project2.Framework.Broker;
import cs601.project2.Framework.FilterSub;
import cs601.project2.Framework.Subscriber;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Takes any number of input files, filters them by the provided unix time, and prints them to two
 * text files.
 *
 * @author Jason McGowan
 */
public class FilterReviewsByTimeProgram implements Program {

  Iterable<String> inputs;
  String recentFileName;
  String oldFileName;
  long filterTime;
  Broker<Review> broker;

  private FilterReviewsByTimeProgram() {
  }

  /**
   * All arguments must be verified valid separately.
   */
  public FilterReviewsByTimeProgram(Iterable<String> inputs, String recentFileName,
      String oldFileName, long filterTime, Broker<Review> broker) {
    this.inputs = inputs;
    this.recentFileName = recentFileName;
    this.oldFileName = oldFileName;
    this.filterTime = filterTime;
    this.broker = broker;
  }

  @Override
  public void execute() {
    // For benchmarking
    long start = System.currentTimeMillis();

    try (BufferedWriter oldWriter = new BufferedWriter(new FileWriter(oldFileName, true));
        BufferedWriter recentWriter = new BufferedWriter(new FileWriter(recentFileName, true))
    ) {

      // Create one subscriber each to filter out old or recent reviews and write them to file.
      Subscriber<Review> oldFilter = new FilterSub<>(
          review -> review.getUnixReviewTime() < filterTime,
          review -> appendToFile(oldWriter, review));
      Subscriber<Review> recentFilter = new FilterSub<>(
          review -> review.getUnixReviewTime() >= filterTime,
          review -> appendToFile(recentWriter, review));

      broker.subscribe(oldFilter);
      broker.subscribe(recentFilter);

      publishAll();

      broker.shutdown();

    } catch (IOException e) {
      Services.getInstance().getUi()
          .displayMessage("Error writing to file: " + e.getLocalizedMessage());
    }

    Services.getInstance().getUi()
        .displayMessage("Shutdown complete, run time: " + (System.currentTimeMillis() - start));
  }

  private void publishAll() {
    // Cached thread pool, so we'll have one thread for each input file
    ExecutorService pool = Executors.newCachedThreadPool();
    for (String input : inputs) {
      // Each file will be added to the thread pool for parsing and publishing
      pool.execute(() -> {
        Collection<Review> reviews = FileJsonParser.parseFile(input, Review.class);
        reviews.forEach(broker::publish);
      });
    }

    // Shutdown will allow the threads to finish publishing reviews.
    pool.shutdown();
    try {
      // Block until the shutdown is complete (all threads finished publishing)
      if (!pool.awaitTermination(1, TimeUnit.HOURS)) {
        pool.shutdownNow();
      }
    } catch (InterruptedException e) {
      // Nothing to do here
    }
  }

  private void appendToFile(BufferedWriter bw, Review review) {
    try {
      bw.write(review + System.lineSeparator());
    } catch (IOException e) {
      Services.getInstance().getUi().displayMessage("Error writing to file: " + bw);
    }
  }
}
