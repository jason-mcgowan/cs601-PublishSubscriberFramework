package cs601.project2.Framework;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractBroker<T> implements Broker<T> {

  protected final List<Subscriber<T>> subscribers;
  protected final ReentrantReadWriteLock subscriberLock;
  protected volatile boolean isShutdown;

  protected AbstractBroker() {
    subscribers = new LinkedList<>();
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
      subscribers.add(subscriber);
    } finally {
      subscriberLock.writeLock().unlock();
    }
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