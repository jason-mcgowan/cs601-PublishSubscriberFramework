package cs601.project2.Framework;

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
  private final ExecutorService publisher = Executors.newSingleThreadExecutor();

  private void sendToSubscribers(T item) {
    subscriberLock.readLock().lock();
    try {
      subscribers.forEach(sub -> sub.onEvent(item));
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishNewItem(T item) {
    // todo
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    // todo
  }
}
