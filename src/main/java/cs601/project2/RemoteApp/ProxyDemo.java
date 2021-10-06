package cs601.project2.RemoteApp;

import cs601.project2.Framework.AsyncOrderedDispatchBroker;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Remote.RemoteSubscriberProxy;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public final class ProxyDemo {

  public static void main(String[] args) {
    RemoteSubscriberProxy<String> proxy = new RemoteSubscriberProxy<>(7777);
    Broker<String> broker = new AsyncOrderedDispatchBroker<>();
    broker.subscribe(proxy);
    System.out.println("Broker and Proxy server running");
    try {
      System.out.println("Press any key to begin publishing");
      System.in.read();
      System.out.println("Publishing...");
      publishStrings(broker);
      System.out.println("Press any key to stop server");
      System.in.read();
    } catch (IOException e) {
      e.printStackTrace();
    }
    broker.shutdown();
    proxy.shutdown();
  }

  private static void publishStrings(Broker<String> broker) {
    for (int i = 0; i < 10; i++) {
      int num = ThreadLocalRandom.current().nextInt(0, 10);
      System.out.println("Publishing: " + num);
      broker.publish(Integer.toString(num));
    }
  }
}
