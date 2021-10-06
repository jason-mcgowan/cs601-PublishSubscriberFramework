package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Subscriber;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteSubscriberProxy<T> implements Subscriber<T> {

  private final Type type = new TypeToken<T>() {
  }.getType();
  private final Gson gson = new Gson();
  private final ExecutorService clients = Executors.newCachedThreadPool();
  private final Collection<ClientConnection> clientConnections = new LinkedList<>();
  private boolean serverListening;

  public RemoteSubscriberProxy(int port) {
    serverListening = true;
    ExecutorService newConnectionThread = Executors.newSingleThreadExecutor();
    newConnectionThread.execute(() -> listenForClients(port));
  }

  private void listenForClients(int port) {
    try {
      ServerSocket server = new ServerSocket(port);

      while (serverListening) {
        Socket newConnection = server.accept();
        ClientConnection client = new ClientConnection(newConnection);
        clientConnections.add(client);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public synchronized void onEvent(T item) {
    clients.execute(() -> sendItem(item));
  }

  private void sendItem(T item) {
    for (ClientConnection client : clientConnections) {
      client.handleMessage(item.toString());
    }
  }
}
