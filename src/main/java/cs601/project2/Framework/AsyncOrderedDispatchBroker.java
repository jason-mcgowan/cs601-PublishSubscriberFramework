/*
Design notes
-Tried using self-implemented blocking queue, was as fast but hard to read.
-Tried single thread executing each onEvent as a new task, super slow.
-Tried single thread executing each publish to all subscribers as a task, much faster.
-Checked both single-thread options with dummy time-consuming subscribers, little difference.
-Single thread per subscriber improved the run time immensely!
 */

package cs601.project2.Framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Publishes items to subscribers in an asynchronous manner. Items are guaranteed to be published to
 * subscribers in order.
 *
 * @author Jason McGowan
 */
public class AsyncOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final List<ExecutorService> threads = new ArrayList<>();

  @Override
  protected void handleNewSubscriber(Subscriber<T> subscriber) {
    super.handleNewSubscriber(subscriber);
    // Add a new single thread for each new subscriber
    synchronized (threads) {
      threads.add(Executors.newSingleThreadExecutor());
    }
  }

  @Override
  protected void publishNewItem(T item) {
    subscriberLock.readLock().lock();
    synchronized (threads) {
      try {
        // Each thread publishes the item to its associated subscriber
        for (int i = 0; i < subscribers.size(); i++) {
          Subscriber<T> subscriber = subscribers.get(i);
          threads.get(i).execute(() -> subscriber.onEvent(item));
        }
      } finally {
        subscriberLock.readLock().unlock();
      }
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    // Shutdown all the threads, so they can finish their tasks, then block until they're done.
    threads.forEach(ExecutorService::shutdown);
    for (ExecutorService thread : threads) {
      try {
        if (!thread.awaitTermination(1, TimeUnit.HOURS)) {
          // Could put some sort of error handling or exception here
          thread.shutdownNow();
        }
      } catch (InterruptedException unhandled) {
        // Don't think we need to do anything here
      }
    }
  }
}
