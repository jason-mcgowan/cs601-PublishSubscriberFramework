package cs601.project2.Framework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Basic implementation of the Broker interface.
 *
 * @author Jason McGowan
 */
public abstract class AbstractBroker<T> implements Broker<T> {

  protected final List<Subscriber<T>> subscribers;
  protected final ReentrantReadWriteLock subscriberLock;
  protected volatile boolean isShutdown;

  protected AbstractBroker() {
    subscribers = new ArrayList<>();
    subscriberLock = new ReentrantReadWriteLock(true);
    isShutdown = false;
  }

  @Override
  public final void publish(T item) {
    throwExceptionIfShutdown();
    publishNewItem(item);
  }

  protected abstract void publishNewItem(T item);

  @Override
  public final void subscribe(Subscriber<T> subscriber) {
    throwExceptionIfShutdown();
    subscriberLock.writeLock().lock();
    try {
      handleNewSubscriber(subscriber);
    } finally {
      subscriberLock.writeLock().unlock();
    }
  }

  protected void handleNewSubscriber(Subscriber<T> subscriber) {
    subscribers.add(subscriber);
  }

  @Override
  public final void shutdown() {
    throwExceptionIfShutdown();
    subscriberLock.writeLock().lock();
    try {
      isShutdown = true;
      publishRemainingBeforeShutdown();
    } finally {
      subscriberLock.writeLock().unlock();
    }
  }

  protected abstract void publishRemainingBeforeShutdown();

  private void throwExceptionIfShutdown() {
    if (isShutdown) {
      throw new IllegalStateException("Broker is shutdown");
    }
  }
}