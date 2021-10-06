package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Subscriber;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;

public class RemoteBroker<T> implements Broker<T> {

  private final Type type = new TypeToken<T>(){}.getType();
  private final Gson gson = new Gson();
  private final Broker<T> broker;
  private Socket socket;
  private BufferedReader fromProxy;
  private boolean listening;

  public RemoteBroker(Broker<T> broker) {
    this.broker = broker;
  }

  public synchronized void connectToProxy(String hostAddress, int port) throws IOException {
    socket = new Socket(hostAddress, port);
    fromProxy = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    listening = true;
    handleInputs();
  }

  private void handleInputs() throws IOException {
    while (listening) {
      String json = fromProxy.readLine();
      publish(gson.fromJson(json, type));
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
      socket.close();
      fromProxy.close();
    } catch (IOException e) {
      // Add logging here if desired
    }
    broker.shutdown();
  }
}
