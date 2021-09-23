package cs601.project2.Framework;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PublishQueue<T> {

  private final Queue<T> queue = new LinkedList<>();
  private final ReentrantLock lock = new ReentrantLock(true);
  private final Condition notEmpty = lock.newCondition();
  private final Condition notFull = lock.newCondition();
  private final int maxSize;

  public PublishQueue() {
    this(15);
  }

  public PublishQueue(int maxSize) {
    this.maxSize = maxSize;
  }

  public T poll() throws InterruptedException {
    lock.lock();
    try {
      while (queue.isEmpty()) {
        notEmpty.await();
      }
      T item = queue.poll();
      notFull.signalAll();
      return item;
    } finally {
      lock.unlock();
    }
  }

  public void add(T item) throws InterruptedException {
    lock.lock();
    try {
      while (queue.size() == maxSize) {
        notFull.await();
      }
      queue.add(item);
      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
  }
}
