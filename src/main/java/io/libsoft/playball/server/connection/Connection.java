package io.libsoft.playball.server.connection;

import io.libsoft.playball.server.connection.message.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Runnable{

  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private boolean running;



  public Connection(Socket socket) {
    try {
      this.socket = socket;
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    } catch (IOException ignored) { }

  }

  public ObjectInputStream getOis() {
    return ois;
  }

  public ObjectOutputStream getOos() {
    return oos;
  }


  public void send(Message message) {
    try {
      oos.writeObject(message);
    } catch (IOException ignored) {}
  }

  public Socket getSocket() {
    return socket;
  }

  @Override
  public void run() {
    running = true;
    while (running){
      try {
        Message m = (Message) ois.readObject();
        System.out.println(m.getMessageType());
      } catch (IOException | ClassNotFoundException e) {
        running = false;
        System.out.println("Connection closed by remote client.");

      }
    }
  }
}
