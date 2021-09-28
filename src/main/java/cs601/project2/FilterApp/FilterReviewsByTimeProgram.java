package cs601.project2.FilterApp;

import cs601.project2.Framework.Broker;
import cs601.project2.Framework.FilterSub;
import cs601.project2.Framework.Subscriber;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    ExecutorService pool = Executors.newCachedThreadPool();

    Subscriber<Review> oldFilter = new FilterSub<>(
        review -> review.getUnixReviewTime() < filterTime,
        review -> {
//          System.out.println("Old: " + review);
          // Todo write to file
        });
    Subscriber<Review> recentFilter = new FilterSub<>(
        review -> review.getUnixReviewTime() >= filterTime,
        review -> {
//          System.out.println("New: " + review);
          // Todo write to file
        });

    broker.subscribe(oldFilter);
    broker.subscribe(recentFilter);

    inputs.forEach(input -> pool.execute(() ->
        FileJsonParser.parseFile(input, Review.class)
            .forEach(broker::publish)
    ));

    pool.shutdown();
    broker.shutdown();
  }
}
