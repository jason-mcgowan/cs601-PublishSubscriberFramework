package cs601.project2.Framework;

import java.io.Serializable;

public class RemoteSubscriberProxy<T extends Serializable> implements Subscriber<T> {

  @Override
  public void onEvent(T item) {
    // todo
  }
}
