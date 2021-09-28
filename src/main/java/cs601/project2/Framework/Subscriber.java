package cs601.project2.Framework;

public interface Subscriber<T> {

  /**
   * Called by the Broker when a new item has been published.
   *
   * @author Jason McGowan
   */
  void onEvent(T item);
}
