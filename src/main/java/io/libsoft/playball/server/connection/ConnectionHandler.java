package io.libsoft.playball.server.connection;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.collections.ObservableMap;

public class ConnectionHandler {

  private final ObservableMap<UUID, Connection> connections;
  private final List<Thread> connectionThreads;
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  private final UUID serverUUID = UUID.nameUUIDFromBytes("server".getBytes());

  public ConnectionHandler(ObservableMap<UUID, Connection> connections) {
    this.connections = connections;
    connectionThreads = new LinkedList<>();
  }

  public void handleConnection(Socket accepted) {
    System.out.println("connection established with " + accepted.toString());
    Connection connection = new Connection(serverUUID, accepted, connections);
    Thread thread = new Thread(connection);
    connectionThreads.add(thread);
    thread.start();
  }

  public Connection getConnectionByUUID(UUID uuid) {
    return connections.get(uuid);
  }


}
