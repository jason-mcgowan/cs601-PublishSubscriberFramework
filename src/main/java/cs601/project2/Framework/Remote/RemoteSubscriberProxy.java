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

public class RemoteSubscriberProxy<T> implements Subscriber<T> {

  private final Type type = new TypeToken<T>() {
  }.getType();
  private final Gson gson = new Gson();
  private final List<ExecutorService> messageThreads = new LinkedList<>();
  private final List<SocketMessenger> socketMessengers = new LinkedList<>();
  private final ExecutorService connectionListenerThread = Executors.newSingleThreadExecutor();
  private final ReentrantLock messageLock = new ReentrantLock();
  private ServerSocket server;

  public RemoteSubscriberProxy(int port) {
    try {
      server = new ServerSocket(port);
    } catch (IOException e) {
      // Add logging if desired
    }
    connectionListenerThread.execute(this::listenForClients);
  }

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

  public void shutdown() {
    messageLock.lock();
    sendAllClientsMessage(RemoteBroker.POISON_PILL);
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
    sendAllClientsMessage(message);
  }

  private void sendAllClientsMessage(String message) {
    messageLock.lock();
    for (int i = 0; i < socketMessengers.size(); i++) {
      SocketMessenger sm = socketMessengers.get(i);
      messageThreads.get(i).execute(() -> sm.sendMessage(message));
    }
    messageLock.unlock();
  }
}
