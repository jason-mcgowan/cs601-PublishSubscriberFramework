package cs601.project2;

import java.util.ArrayList;

public class SynchronousOrderedDispatchBroker<T> implements Broker<T> {

  private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

  /**
   * Called by a publisher to publish a new item. The item will be delivered to all current
   * subscribers.
   *
   * @param item
   */
  @Override
  public synchronized void publish(T item) {
    subscribers.forEach(sub-> sub.onEvent(item));
  }

  /**
   * Called once by each subscriber. Subscriber will be registered and receive notification of all
   * future published items.
   *
   * @param subscriber
   */
  @Override
  public void subscribe(Subscriber<T> subscriber) {
    subscribers.add(subscriber);
  }

  /**
   * Indicates this broker should stop accepting new items to be published and shut down all
   * threads. The method will block until all items that have been published have been delivered to
   * all subscribers.
   */
  @Override
  public void shutdown() {

  }
}
