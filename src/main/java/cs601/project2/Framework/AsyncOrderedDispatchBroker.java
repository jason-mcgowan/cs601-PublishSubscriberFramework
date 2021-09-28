package cs601.project2.Framework;

/*
Design notes:
1. This likely could have been implemented without the PublishQueue field by passing items to the
single publisher thread.execute method as they arrive. However, I created the PublishQueue class
to practice implementing and using a blocking queue.
 */


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Publishes items to subscribers in an asynchronous manner. Items are guaranteed to be published to
 * subscribers in order.
 *
 * @author Jason McGowan
 */
public class AsyncOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final PublishQueue<T> itemsToPublish = new PublishQueue<>();

  public AsyncOrderedDispatchBroker() {
    publishItemsAsTheyArrive();
  }

  private void publishItemsAsTheyArrive() {
    ExecutorService publisher = Executors.newSingleThreadExecutor();
    while (!isShutdown || !itemsToPublish.isEmpty()) {
      try {
        publisher.execute(sendToSubsTask(itemsToPublish.poll()));
      } catch (InterruptedException e) {
        // Nothing to do on interruption
      }
    }
    publisher.shutdown();
  }

  private Runnable sendToSubsTask(T item) {
    subscriberLock.readLock().lock();
    try {
      return () -> subscribers.forEach(sub -> sub.onEvent(item));
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishNewItem(T item) {
    try {
      itemsToPublish.add(item);
    } catch (InterruptedException e) {
      // Nothing to do on interruption
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    // Shutting down is handled in the publishItemsAsTheyArrive() method
  }
}
