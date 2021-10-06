package cs601.project2.RemoteApp;

import cs601.project2.Framework.AsyncOrderedDispatchBroker;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Remote.RemoteSubscriberProxy;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Server side demonstration for project2 part 2.
 */
public final class ProxyDemo {

  /**
   * Main entry point. Uses a single broker and proxy server. Prints various status messages to the
   * console. Publishes 10 integers as strings through the broker to the proxy server.
   */
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
      System.out.println("Press any key to shut down server");
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
