package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final PublishQueue<T> itemsToPublish = new PublishQueue<>();
  private final ExecutorService publisher = Executors.newSingleThreadExecutor();

  private void sendToSubcribers(T item) {
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
