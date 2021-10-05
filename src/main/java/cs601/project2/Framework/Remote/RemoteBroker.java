package cs601.project2.Framework.Remote;

import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Subscriber;

public class RemoteBroker<T> implements Broker<T> {

  private final Broker<T> broker;

  public RemoteBroker(Broker<T> broker) {
    this.broker = broker;
  }

  @Override
  public void publish(T item) {
    broker.publish(item);
  }

  @Override
  public void subscribe(Subscriber<T> subscriber) {
    broker.subscribe(subscriber);
  }

  @Override
  public void shutdown() {
    broker.shutdown();
  }
}
