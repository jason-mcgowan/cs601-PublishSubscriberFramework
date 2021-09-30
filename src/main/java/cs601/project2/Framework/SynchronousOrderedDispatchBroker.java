package cs601.project2.Framework;

/*
Design notes:
-Tried with synchronizing on the subscriber list.
-Moved to using a read lock on subscribers, runs faster (about 4 seconds in test case).
-Tried using a thread pool to process each publish to the subscribers, was exceedingly slow.
Would only be useful in the case where you had subscribers with downtime.
 */

/**
 * Blocks on any publish call until all subscribers complete processing.
 *
 * @author Jason McGowan
 */
public class SynchronousOrderedDispatchBroker<T> extends AbstractBroker<T> {

  @Override
  protected synchronized void publishNewItem(T item) {
    // Read lock so the subscriber list can't change while iterating
    subscriberLock.readLock().lock();
    try {
      subscribers.forEach(sub -> sub.onEvent(item));
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {
    // Nothing extra to do here (no publish backlog)
  }
}
