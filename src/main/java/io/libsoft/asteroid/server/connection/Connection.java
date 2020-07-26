package io.libsoft.asteroid.server.connection;

import com.google.gson.reflect.TypeToken;
import io.libsoft.messenger.Message;
import io.libsoft.messenger.MessageType;
import io.libsoft.messenger.jsonmessages.SetName;
import io.libsoft.messenger.service.GsonService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import javafx.scene.input.KeyCode;

public class Connection implements Runnable {

  private final ConnectionManager connectionManager;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private boolean running;
  private UUID connectionUUID;
  private String username;

  public Connection(Socket socket,
      ConnectionManager connectionManager) {

    this.connectionManager = connectionManager;
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


  public void sendMessage(String message) {
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
//        System.out.println(GsonService.getPprinter().toJson(m));
        switch (m.getMessageType()) {
          case ACCEPTED_UUID:
            connectionManager.successfulConnection(m.getSenderUUID(), this);
            break;
          case SUBSCRIBE:
            connectionManager.gameStateSubscription(connectionUUID, this);
            break;
          case SET_NAME:
            SetName payload = GsonService.getInstance().fromJson(m.getPayload(), SetName.class);
            username = payload.getUsername();
            connectionUUID = connectionManager.setOrGetUUID(username);
            String r = Message.build()
                .messageType(MessageType.ASSIGN_UUID)
                .payload(GsonService.getInstance().toJson(connectionUUID))
                .sign(connectionManager.getServerUUID())
                .toJson();

            sendMessage(r);
            break;
          case CONTROL:
            List<KeyCode> kc = GsonService.getInstance().fromJson(m.getPayload(), new TypeToken<List<KeyCode>>() {
            }.getType());
            connectionManager.control(connectionUUID, kc);
            break;
        }


      } catch (IOException | ClassNotFoundException e) {
        running = false;
        System.out.println("Connection closed by remote client.");
        connectionManager.connectionHold(connectionUUID);

      }
    }
  }

  @Override
  public String toString() {
    return connectionUUID.toString();
  }

}
