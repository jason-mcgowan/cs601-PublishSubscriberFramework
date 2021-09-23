package cs601.project2.Framework;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SynchronousOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final ExecutorService pool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors() * 2);

  @Override
  protected synchronized void publishNewItem(T item) {
    // Read lock so the subscriber list can't change while iterating
    subscriberLock.readLock().lock();
    List<Callable<T>> tasks;
    try {
      tasks = subscribers.stream()
          .map(sub -> (Callable<T>) () -> {
            sub.onEvent(item);
            return null;
          }).collect(Collectors.toList());
    } finally {
      subscriberLock.readLock().unlock();
    }
    try {
      pool.invokeAll(tasks);
    } catch (InterruptedException e) {
      // Doesn't need to be handled
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    pool.shutdown();
  }
}
