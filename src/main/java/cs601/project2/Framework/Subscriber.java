package cs601.project2.Framework;

public interface Subscriber<T> {

  /**
   * Called by the Broker when a new item has been published.
   *
   * @param item
   */
  void onEvent(T item);
}
