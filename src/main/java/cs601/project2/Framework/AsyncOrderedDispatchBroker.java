package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Publishes items to subscribers in an asynchronous manner. Items are guaranteed to be published to
 * subscribers in order.
 *
 * @author Jason McGowan
 */
public class AsyncOrderedDispatchBroker<T> extends AbstractAsyncBroker<T> {

  @Override
  protected ExecutorService initializePool() {
    return Executors.newSingleThreadExecutor();
  }
}
