package cs601.project2.Framework.Remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Class to hold the reader and writer of a socket connection. Contains methods to send and receive
 * string messages over the connection, and to shut down same reader and writer.
 *
 * @author Jason McGowan
 */
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

  /**
   * Sends the message as a String line to the socket listener. A new line character is
   * automatically appended as part of the protocol.
   */
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

  /**
   * Closes the socket stream reader and writer.
   */
  public void shutdown() {
    try {
      send.close();
      receive.close();
    } catch (IOException e) {
      // Add logging if desired
    }
  }

}
