package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Subscriber;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class RemoteSubscriberProxy<T> implements Subscriber<T> {

  private final Type type = new TypeToken<T>(){}.getType();
  private final Gson gson = new Gson();
  private final Collection<Socket> clients = new LinkedList<>();
  private final Collection<DataOutputStream> streams = new LinkedList<>();
  private boolean serverListening;

  public synchronized void startServer(int port) throws IOException {
    serverListening = true;
    ServerSocket server  = new ServerSocket(port);
    while (serverListening) {
      Socket client = server.accept();
      clients.add(client);
      streams.add(new DataOutputStream(client.getOutputStream()));
    }
  }

  public synchronized void stopServer() throws IOException {
    serverListening = false;
    for (DataOutputStream stream : streams) {
      stream.close();
    }
    for (Socket client : clients) {
      client.close();
    }
  }

  @Override
  public synchronized void onEvent(T item) {
    try {
      messageClients(gson.toJson(item, type));
    } catch (IOException e) {
      // Add logging here if desired
    }
  }

  private void messageClients(String json) throws IOException {
    for (DataOutputStream stream : streams) {
      stream.writeUTF(json);
      stream.flush();
    }
  }
}
