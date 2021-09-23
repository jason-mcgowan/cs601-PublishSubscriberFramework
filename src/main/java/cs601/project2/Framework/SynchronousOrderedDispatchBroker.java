package cs601.project2.Framework;

public class SynchronousOrderedDispatchBroker<T> extends AbstractBroker<T> {

  @Override
  protected void publishNewItem(T item) {
    // Lock to make sure no other subscribers are added mid-publish
    subscriberLock.readLock().lock();
    try {
      // Synchronize on this Broker to prevent other threads from interleaving
      synchronized (this) {
        subscribers.forEach(subscriber -> subscriber.onEvent(item));
      }
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    // Nothing needed here as all items are published as received
  }
}
