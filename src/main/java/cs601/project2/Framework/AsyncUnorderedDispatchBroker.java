package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Publishes items to subscribers in an asynchronous manner. Items are not guaranteed to be
 * published to subscribers in order.
 *
 * @author Jason McGowan
 */
public class AsyncUnorderedDispatchBroker<T> extends AbstractAsyncBroker<T> {

  @Override
  protected ExecutorService initializePool() {
    // I played around with different pool sizes. CPU count * 2 was slower, CPU count was about the
    // same. With count + 1 we guarantee at least two threads will be working.
    return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
  }
}
