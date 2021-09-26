package cs601.project2.FilterApp;

import cs601.project2.Framework.AsyncOrderedDispatchBroker;
import cs601.project2.Framework.AsyncUnorderedDispatchBroker;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.FilterSub;
import cs601.project2.Framework.Subscriber;
import cs601.project2.Framework.SynchronousOrderedDispatchBroker;

public final class FilterDemo {

  private final static String APPS_FILENAME = "Apps_for_Android_5.json";
  private final static String HOME_KITCHEN_FILENAME = "Home_and_Kitchen_5.json";
  private final static long UNIX_FILTER_TIME = 1362268800;

  private FilterDemo() {
  }

  public static void main(String[] args) {

//    Broker<Review> broker = new SynchronousOrderedDispatchBroker<>();
//    Broker<Review> broker = new AsyncOrderedDispatchBroker<>();
    Broker<Review> broker = new AsyncUnorderedDispatchBroker<>();

    Subscriber<Review> oldFilter = new FilterSub<>(
        review -> review.getUnixReviewTime() < UNIX_FILTER_TIME,
        review -> {
//          System.out.println("Old: " + review);
          // Todo write to file
        });
    Subscriber<Review> newFilter = new FilterSub<>(
        review -> review.getUnixReviewTime() >= UNIX_FILTER_TIME,
        review -> {
//          System.out.println("New: " + review);
          // Todo write to file
        });

    broker.subscribe(oldFilter);
    broker.subscribe(newFilter);

    Runnable appRead = () -> FileJsonParser.parseFile(APPS_FILENAME, Review.class)
        .forEach(broker::publish);

    Runnable homeRead = () -> FileJsonParser.parseFile(HOME_KITCHEN_FILENAME, Review.class)
        .forEach(broker::publish);

    Thread appThread = new Thread(appRead, APPS_FILENAME);
    Thread homeThread = new Thread(homeRead, HOME_KITCHEN_FILENAME);

    appThread.start();
    homeThread.start();

    long start = System.currentTimeMillis();

    System.out.println("Threads started");
    try {
      appThread.join();
      System.out.println("Joined appthread");
      homeThread.join();
      System.out.println("Joined homethread");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Shutting down");
    broker.shutdown();
    System.out.println("Shutdown complete");
    System.out.println("Runtime: " + (System.currentTimeMillis() - start));
  }

}
