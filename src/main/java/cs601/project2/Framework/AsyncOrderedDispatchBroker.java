package cs601.project2.Framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final PublishQueue<T> itemsToPublish = new PublishQueue<>();
  private final Thread reader = new Thread(this::readPublishers);


  public AsyncOrderedDispatchBroker() {
    reader.start();
    ExecutorService consumer = Executors.newSingleThreadExecutor();
  }

  private void readPublishers() {
    while (true) {
      T item = null;
      try {
        item = itemsToPublish.poll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      sendToSubcribers(item);
    }
  }

  private void sendToSubcribers(T item) {
    subscriberLock.readLock().lock();
    try {
      subscribers.forEach(sub -> sub.onEvent(item));
    } finally {
      subscriberLock.readLock().unlock();
    }
  }

  @Override
  protected void publishNewItem(T item) {
    synchronized (itemsToPublish) {
      try {
        itemsToPublish.add(item);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      itemsToPublish.notifyAll();
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {

  }
}
