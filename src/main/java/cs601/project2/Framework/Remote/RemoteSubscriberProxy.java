package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Subscriber;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Instances of this class facilitate communication between a local {@link
 * cs601.project2.Framework.Broker} and any number of remote {@link RemoteBroker} through a {@link
 * Socket}. Accepts all connection requests on the specified port. Any items published to this
 * {@link Subscriber} will be parsed to JSON and sent as a String to all remote connections.
 *
 * @author Jason McGowan
 */
public class RemoteSubscriberProxy<T> implements Subscriber<T> {

  private final Type type = new TypeToken<T>() {
  }.getType();
  private final Gson gson = new Gson();
  private final List<ExecutorService> messageThreads = new LinkedList<>();
  private final List<SocketMessenger> socketMessengers = new LinkedList<>();
  private final ExecutorService connectionListenerThread = Executors.newSingleThreadExecutor();
  private final ReentrantLock messageLock = new ReentrantLock();
  private ServerSocket server;

  /**
   * Constructor immediately opens a {@link ServerSocket} and begins listening for connections on
   * the specified port.
   *
   * @param port Port to listen for incoming connections.
   */
  public RemoteSubscriberProxy(int port) {
    try {
      server = new ServerSocket(port);
    } catch (IOException e) {
      // Add logging if desired
    }
    connectionListenerThread.execute(this::listenForClients);
  }

  // Waits for connection request, sets up a new thread and socket messenger.
  private void listenForClients() {
    try {
      while (!server.isClosed()) {
        Socket newConnection = server.accept();
        SocketMessenger client = new SocketMessenger(newConnection);
        messageLock.lock();
        socketMessengers.add(client);
        messageThreads.add(Executors.newSingleThreadExecutor());
        messageLock.unlock();
      }
    } catch (IOException e) {
      // Add logging if desired
    }
  }

  /**
   * Sends the "Poison Pill" end of transmission message to all remote brokers. Shuts down all
   * internal threads, sockets, and associated services.
   */
  public void shutdown() {
    messageLock.lock();
    sendClientsMessage(RemoteBroker.POISON_PILL);
    connectionListenerThread.shutdown();
    messageThreads.forEach(ExecutorService::shutdown);
    socketMessengers.forEach(SocketMessenger::shutdown);
    try {
      server.close();
    } catch (IOException e) {
      // Add logging if desired
    }
    messageLock.unlock();
  }

  @Override
  public void onEvent(T item) {
    String message = gson.toJson(item, type);
    sendClientsMessage(message);
  }

  private void sendClientsMessage(String message) {
    messageLock.lock();
    for (int i = 0; i < socketMessengers.size(); i++) {
      SocketMessenger sm = socketMessengers.get(i);
      messageThreads.get(i).execute(() -> sm.sendMessage(message));
    }
    messageLock.unlock();
  }
}
