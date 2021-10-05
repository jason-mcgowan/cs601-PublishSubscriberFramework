package cs601.project2.Framework.Remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cs601.project2.Framework.Subscriber;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

public class RemoteSubscriberProxy<T> implements Subscriber<T> {

  private final Type type = new TypeToken<T>(){}.getType();
  private final Gson gson = new Gson();
  private final Collection<?> remotes = new LinkedList<>();

  @Override
  public void onEvent(T item) {
    sendToRemotes(gson.toJson(item, type));
  }

  private void sendToRemotes(String json){
    // todo
  }
}
