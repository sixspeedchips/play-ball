package io.libsoft.playball.server.connection;

import com.google.gson.GsonBuilder;
import io.libsoft.playball.server.connection.message.Message;
import io.libsoft.playball.server.connection.message.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import javafx.collections.ObservableMap;

public class Connection implements Runnable {

  private final UUID serverUUID;
  private final ObservableMap<UUID, Connection> connectionLog;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private boolean running;


  public Connection(UUID serverUUID,
      Socket socket, ObservableMap<UUID, Connection> connectionLog) {
    this.serverUUID = serverUUID;
    this.connectionLog = connectionLog;
    try {
      this.socket = socket;
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    } catch (IOException ignored) {
    }

  }

  public ObjectInputStream getOis() {
    return ois;
  }

  public ObjectOutputStream getOos() {
    return oos;
  }


  public void sendMessage(Message message) {
    try {
      oos.writeObject(message);
    } catch (IOException ignored) {
    }
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
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(m));
        switch (m.getMessageType()) {
          case REQ_UUID:
            UUID uuid = UUID.nameUUIDFromBytes(socket.toString().getBytes());
            Message r = Message.build()
                .messageType(MessageType.ASSIGN_UUID)
                .messageUUID(uuid)
                .sign(serverUUID);
            sendMessage(r);
            break;
          case ACCEPTED_UUID:
            connectionLog.put(m.getSenderUUID(), this);
            break;
          case SET_UUID:
            connectionLog.put(m.getSenderUUID(), this);
            break;
        }


      } catch (IOException | ClassNotFoundException e) {
        running = false;
        System.out.println("Connection closed by remote client.");

      }
    }
  }
}
