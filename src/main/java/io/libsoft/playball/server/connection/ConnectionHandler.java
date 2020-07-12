package io.libsoft.playball.server.connection;

import io.libsoft.playball.server.connection.Connection.SubscriptionService;
import io.libsoft.playball.server.connection.message.GameState;
import io.libsoft.playball.server.connection.message.Message;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableMap;

public class ConnectionHandler implements SubscriptionService {

  private final List<Thread> connectionThreads = new LinkedList<>();
  private final List<Connection> subscribers = new LinkedList<>();
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  private final UUID serverUUID = UUID.nameUUIDFromBytes("server".getBytes());


  private final ObservableMap<UUID, Connection> connectionsLog;
  private final SpaceState spaceStateGetter;

  public ConnectionHandler(ObservableMap<UUID, Connection> connectionsLog, SpaceState spaceState) {
    this.connectionsLog = connectionsLog;
    this.spaceStateGetter = spaceState;
    executor.scheduleAtFixedRate(this::broadcast, 0, 1000, TimeUnit.MILLISECONDS);

  }


  private void broadcast() {
    Message m;
    GameState g = spaceStateGetter.getState();
    for (Connection subscriber : subscribers) {
      m = Message.build().gameState(g).sign(serverUUID);
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

  public Connection getConnectionByUUID(UUID uuid) {
    return connectionsLog.get(uuid);
  }

  public interface SpaceState {

    GameState getState();
  }


}
