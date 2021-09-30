/*
Design notes:
-Tried cached pool, was much slower than fixed
-Tried different fixed thread pool sizes. CPU count * 2 was slow.
-Tried adding each onEvent as individual tasks to the pool, was slower.
-Fastest was with each task being to publish to all subscribers
-This was verified with dummy time-consuming subscribers, too
-Work stealing pool seems fastest, at least on my machine.
 */

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

  protected final ExecutorService pool = Executors.newWorkStealingPool();

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
