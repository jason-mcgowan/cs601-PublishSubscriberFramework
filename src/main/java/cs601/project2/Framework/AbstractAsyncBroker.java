package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAsyncBroker<T> extends AbstractBroker<T> {

  protected final ExecutorService pool;

  protected AbstractAsyncBroker() {
    pool = initializePool();
  }

  protected abstract ExecutorService initializePool();

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