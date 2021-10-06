package cs601.project2.Framework.Remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {

  private PrintWriter toClient;

  public ClientConnection(Socket socket) {
    try {
      toClient = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void handleMessage(String message) {
    toClient.println(message);
    toClient.flush();
  }

}
