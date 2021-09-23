package cs601.project2.Framework;

public interface Broker<T> {

  /**
   * Called by a publisher to publish a new item. The
   * item will be delivered to all current subscribers.
   *
   * @param item
   */
  void publish(T item);

  /**
   * Called once by each subscriber. Subscriber will be
   * registered and receive notification of all future
   * published items.
   *
   * @param subscriber
   */
  void subscribe(Subscriber<T> subscriber);

  /**
   * Indicates this broker should stop accepting new
   * items to be published and shut down all threads.
   * The method will block until all items that have been
   * published have been delivered to all subscribers.
   */
  void shutdown();
}
