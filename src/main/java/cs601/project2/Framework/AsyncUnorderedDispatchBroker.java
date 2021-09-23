package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncUnorderedDispatchBroker<T> extends AbstractBroker<T> {

  private final ExecutorService threadPool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors() * 2);

  @Override
  protected void publishNewItem(T item) {
    // todo
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    threadPool.shutdown();
    try {
      threadPool.awaitTermination(1, TimeUnit.HOURS);
    } catch (InterruptedException unhandled) {
      // Don't think we need to do anything here
    }
  }
}
