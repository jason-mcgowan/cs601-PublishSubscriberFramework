package cs601.project2.Framework.Remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketMessenger {

  private BufferedWriter send;
  private BufferedReader receive;

  public SocketMessenger(Socket socket) {
    try {
      send = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      // Add logging if desired
    }
  }

  public void sendMessage(String message) {
    try {
      send.write(message);
      send.newLine();
      send.flush();
    } catch (IOException e) {
      // Add logging if desired
    }
  }

  public String receiveMessage() {
    try {
      return receive.readLine();
    } catch (IOException e) {
      // Add logging if desired
    }
    return null;
  }

  public void shutdown() {
    try {
      send.close();
      receive.close();
    } catch (IOException e) {
      // Add logging if desired
    }
  }

}
