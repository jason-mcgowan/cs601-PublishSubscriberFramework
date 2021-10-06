package cs601.project2.RemoteApp;

import cs601.project2.Framework.AsyncOrderedDispatchBroker;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Remote.RemoteSubscriberProxy;
import java.io.IOException;

public final class ProxyDemo {

  public static void main(String[] args) {
    RemoteSubscriberProxy<String> proxy = new RemoteSubscriberProxy<>(7777);
    Broker<String> broker = new AsyncOrderedDispatchBroker<>();
    broker.subscribe(proxy);
    try {
      System.out.println(
          "Server running, accepting remote clients, press any key to begin publishing");
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Publishing...");
    broker.publish("Test 1");
    System.out.println("Press any key to stop server");
    try {
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
    broker.shutdown();
  }
}
