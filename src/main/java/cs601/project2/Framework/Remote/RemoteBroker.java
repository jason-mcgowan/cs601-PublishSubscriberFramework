package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Subscriber;
import cs601.project2.Framework.SynchronousOrderedDispatchBroker;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;

/**
 * Takes messages over a java.net.Socket connection. Messages must be objects parsed to JSON string
 * lines. Instances must be initialized with a {@link Broker} passed to the constructor {@link
 * #RemoteBroker(Broker)}. Connection to {@link RemoteSubscriberProxy} is then made using {@link
 * #connectToProxy(String, int)}. All messages received over this connection must be objects parsed
 * to JSON and sent as String lines. The messages will then be parsed back to type {@link T} and
 * immediately published using the provided {@link #broker}. Instance will shut itself down and the
 * {@link #broker} when the remote server sends the end of transmission message.
 *
 * @author Jason McGowan
 */
public class RemoteBroker<T> implements Broker<T> {

  public static final String POISON_PILL = "32214a33-b382-4b8a-891d-5675478ca834";

  private final Type type = new TypeToken<T>() {
  }.getType();
  private final Gson gson = new Gson();
  private final Broker<T> broker;
  private SocketMessenger messenger;
  private Socket socket;
  private boolean listening;

  private RemoteBroker() {
    this.broker = new SynchronousOrderedDispatchBroker<>();
  }

  public RemoteBroker(Broker<T> broker) {
    this.broker = broker;
  }

  /**
   * See {@link RemoteBroker} for instructions on use.
   */
  public synchronized void connectToProxy(String hostAddress, int port) throws IOException {
    socket = new Socket(hostAddress, port);
    messenger = new SocketMessenger(socket);
    listening = true;
    handleInputs();
  }

  // Helper method for readability. Publishes received messages or shuts down.
  private void handleInputs() {
    while (listening) {
      String message = messenger.receiveMessage();
      if (!message.equals(POISON_PILL)) {
        publish(gson.fromJson(message, type));
      } else {
        listening = false;
        shutdown();
      }
    }
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
  public synchronized void shutdown() {
    listening = false;
    try {
      messenger.shutdown();
      socket.close();
    } catch (IOException e) {
      // Add logging here if desired
    }
    broker.shutdown();
  }
}
