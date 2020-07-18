package io.libsoft.asteroid.server.connection;

import io.libsoft.asteroid.server.connection.Connection.SubscriptionManager;
import io.libsoft.messenger.GameState;
import io.libsoft.messenger.Message;
import io.libsoft.messenger.MessageType;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableMap;

public class ConnectionHandler implements SubscriptionManager {

  private final List<Thread> connectionThreads = new LinkedList<>();
  private final List<Connection> subscribers = new LinkedList<>();
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  private final UUID serverUUID = UUID.nameUUIDFromBytes("server".getBytes());


  private final ObservableMap<UUID, Connection> connectionsLog;
  private final ModelState modelState;

  public ConnectionHandler(ObservableMap<UUID, Connection> connectionsLog, ModelState modelState) {
    this.connectionsLog = connectionsLog;
    this.modelState = modelState;
    executor.scheduleAtFixedRate(this::broadcast, 0, 50, TimeUnit.MILLISECONDS);

  }


  private void broadcast() {
    Message m;
    GameState g = modelState.getState();
    for (Connection subscriber : subscribers) {
      m = Message.build().messageType(MessageType.GAME_STATE).gameState(g).sign(serverUUID);
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
  public void gameStateSubscription(Connection connection) {
    subscribers.add(connection);
  }

  @Override
  public void successfulConnection(UUID uuid, Connection connection) {
    connectionsLog.put(uuid, connection);
  }

  @Override
  public UUID getServerUUID() {
    return serverUUID;
  }

  @Override
  public void clearEntity(UUID uuid) {
    modelState.removeEntityByUUID(uuid);
  }


  public Connection getConnectionByUUID(UUID uuid) {
    return connectionsLog.get(uuid);
  }

  public interface ModelState {

    GameState getState();

    void removeEntityByUUID(UUID uuid);
  }


}
