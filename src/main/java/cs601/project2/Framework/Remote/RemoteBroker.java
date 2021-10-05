package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Broker;
import cs601.project2.Framework.Subscriber;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;

public class RemoteBroker<T> implements Broker<T> {

  private final Type type = new TypeToken<T>(){}.getType();
  private final Gson gson = new Gson();
  private final Broker<T> broker;
  private Socket socket;
  private DataInputStream stream;
  private boolean listening;

  public RemoteBroker(Broker<T> broker) {
    this.broker = broker;
  }

  public synchronized void connectToProxy(String hostAddress, int port) throws IOException {
    socket = new Socket(hostAddress, port);
    stream = new DataInputStream(socket.getInputStream());
    listening = true;
    handleInputs();
  }

  private void handleInputs() throws IOException {
    while (listening) {
      String json = stream.readUTF();
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
      stream.close();
    } catch (IOException e) {
      // Add logging here if desired
    }
    broker.shutdown();
  }
}
