package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Publishes items to subscribers in an asynchronous manner. Items are not guaranteed to be
 * published to subscribers in order.
 *
 * @author Jason McGowan
 */
public class AsyncUnorderedDispatchBroker<T> extends AbstractBroker<T> {

  private final ExecutorService pool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors());

  @Override
  protected void publishNewItem(T item) {
    subscriberLock.readLock().lock();
    try {
      pool.execute(() ->
          subscribers.forEach(sub -> sub.onEvent(item))
      );
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    pool.shutdown();
    try {
      if (!pool.awaitTermination(1, TimeUnit.HOURS)) {
        // Could put some sort of error handling or exception here
        pool.shutdownNow();
      }
    } catch (InterruptedException unhandled) {
      // Don't think we need to do anything here
    }
  }
}
