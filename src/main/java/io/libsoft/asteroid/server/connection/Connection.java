package io.libsoft.asteroid.server.connection;

import com.google.gson.GsonBuilder;
import io.libsoft.messenger.Message;
import io.libsoft.messenger.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

public class Connection implements Runnable {

  private final SubscriptionManager subscriptionManager;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private boolean running;
  private UUID connectionUUID;

  public Connection(Socket socket,
      SubscriptionManager subscriptionManager) {

    this.subscriptionManager = subscriptionManager;
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
    while (running) {
      try {
        Message m = (Message) ois.readObject();
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(m));
        switch (m.getMessageType()) {
          case REQ_UUID:
            connectionUUID = UUID.nameUUIDFromBytes(socket.toString().getBytes());
            Message r = Message.build()
                .messageType(MessageType.ASSIGN_UUID)
                .messageUUID(connectionUUID)
                .sign(subscriptionManager.getServerUUID());
            sendMessage(r);
            break;
          case ACCEPTED_UUID:
            subscriptionManager.successfulConnection(m.getSenderUUID(), this);
            break;
          case SUBSCRIBE:
            subscriptionManager.gameStateSubscription(this);
            break;

        }


      } catch (IOException | ClassNotFoundException e) {
        running = false;
        System.out.println("Connection closed by remote client.");
        subscriptionManager.clearEntity(connectionUUID);
      }
    }
  }

  @Override
  public String toString() {
    return connectionUUID.toString();
  }

  public interface SubscriptionManager {

    void gameStateSubscription(Connection connection);

    void successfulConnection(UUID uuid, Connection connection);

    UUID getServerUUID();

    void clearEntity(UUID uuid);
  }
}
