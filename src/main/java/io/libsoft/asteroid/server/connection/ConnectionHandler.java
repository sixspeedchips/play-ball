package io.libsoft.asteroid.server.connection;

import io.libsoft.asteroid.model.ModelState;
import io.libsoft.messenger.GameState;
import io.libsoft.messenger.Message;
import io.libsoft.messenger.MessageType;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.KeyCode;

public class ConnectionHandler implements ConnectionManager {

  private final List<Thread> connectionThreads = new LinkedList<>();
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  private final UUID serverUUID = UUID.nameUUIDFromBytes("server".getBytes());

  private final Map<String, UUID> usernames = new HashMap<>();
  private final Map<UUID, Connection> connectionsLog = new HashMap<>();
  private final Map<UUID, Connection> subscribers = new HashMap<>();
  private final ModelState modelState;

  public ConnectionHandler(ModelState modelState) {
    this.modelState = modelState;
    executor.scheduleAtFixedRate(this::broadcast, 0, 16, TimeUnit.MILLISECONDS);

  }


  private void broadcast() {
    String m;
    GameState g = modelState.getState();
    for (Connection subscriber : subscribers.values()) {
      m = Message.build().messageType(MessageType.GAME_STATE).gameState(g).sign(serverUUID).toJson();
      subscriber.sendMessage(m);
    }
  }


  public void handleConnection(Socket accepted) {
    System.out.println("connection established with " + accepted.toString());
    Connection connection = new Connection(accepted, this);
    Thread thread = new Thread(connection);
    connectionThreads.add(thread);
    thread.start();

  }

  @Override
  public void gameStateSubscription(UUID uuid, Connection connection) {
    subscribers.put(uuid, connection);
  }

  @Override
  public void successfulConnection(UUID uuid, Connection connection) {
    if (modelState.containsEntity(uuid)) {
      modelState.setFrozen(uuid, false);
    } else {
      modelState.addEntity(uuid);
    }
    connectionsLog.put(uuid, connection);

  }

  @Override
  public UUID getServerUUID() {
    return serverUUID;
  }

  @Override
  public void control(UUID uuid, List<KeyCode> kc) {
    modelState.updateEntityControl(uuid, kc);
  }

  @Override
  public void clearEntity(UUID uuid) {
    modelState.removeEntityByUUID(uuid);
  }

  @Override
  public UUID setOrGetUUID(String username) {
    UUID uuid = usernames.getOrDefault(username, UUID.nameUUIDFromBytes(username.getBytes()));
    if (!usernames.containsKey(username)) {
      usernames.put(username, uuid);
    }
    return uuid;
  }

  @Override
  public void connectionHold(UUID uuid) {
    modelState.setFrozen(uuid, true);
    connectionsLog.remove(uuid);
    subscribers.remove(uuid);

  }


}
