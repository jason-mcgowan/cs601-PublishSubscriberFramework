package cs601.project2.RemoteApp;

import cs601.project2.Framework.FilterSub;
import cs601.project2.Framework.Remote.RemoteBroker;
import cs601.project2.Framework.Subscriber;
import cs601.project2.Framework.SynchronousOrderedDispatchBroker;
import java.io.IOException;


/**
 * Client side of project2 part2 demonstration.
 */
public final class RemoteSubDemo {

  /**
   * Main entry point. Connects to the remote server over port 7777 on the local machine. Uses a
   * single RemoteBroker and Subscriber of type String which will print all received messages to the
   * console.
   */
  public static void main(String[] args) {
    RemoteBroker<String> rb = new RemoteBroker<>(new SynchronousOrderedDispatchBroker<>());
    Subscriber<String> sub = new FilterSub<>(x -> true, System.out::println);
    rb.subscribe(sub);
    try {
      rb.connectToProxy("localhost", 7777);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
