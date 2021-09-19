package cs601.project2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynchronousOrderedDispatchBroker<T> implements Broker<T> {

  private final List<Subscriber<T>> subscribers = Collections.synchronizedList(new ArrayList<>());

  /**
   * Called by a publisher to publish a new item. The item will be delivered to all current
   * subscribers.
   *
   * @param item
   */
  @Override
  public synchronized void publish(T item) {
    subscribers.forEach(sub -> sub.onEvent(item));
  }

  /**
   * Called once by each subscriber. Subscriber will be registered and receive notification of all
   * future published items.
   *
   * @param subscriber
   */
  @Override
  public synchronized void subscribe(Subscriber<T> subscriber) {
    subscribers.add(subscriber);
  }

  /**
   * Indicates this broker should stop accepting new items to be published and shut down all
   * threads. The method will block until all items that have been published have been delivered to
   * all subscribers.
   */
  @Override
  public synchronized void shutdown() {
    subscribers.clear();
  }
}
