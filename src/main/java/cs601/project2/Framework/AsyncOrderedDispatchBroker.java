package cs601.project2.Framework;

import java.util.Deque;
import java.util.LinkedList;

public class AsyncOrderedDispatchBroker<T> extends AbstractBroker<T> {

  private final Deque<T> itemsToPublish = new LinkedList<>();
  private final Thread reader = new Thread(this::readPublishers);

  public AsyncOrderedDispatchBroker() {
    reader.start();
  }

  private void readPublishers() {
    synchronized (itemsToPublish) {
      while (itemsToPublish.isEmpty()) {
        try {
          itemsToPublish.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    subscribers.forEach(sub -> sub.onEvent(itemsToPublish.poll()));
    // Todo check if this is blocking while publishers try to add
  }

  @Override
  protected void publishNewItem(T item) {
    synchronized (itemsToPublish) {
      itemsToPublish.add(item);
      itemsToPublish.notifyAll();
    }
  }

  @Override
  protected void publishRemainingBeforeShutdown() {

  }
}
