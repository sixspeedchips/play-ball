package io.libsoft.playball.server.connection;

import io.libsoft.playball.server.connection.message.MUUIDAssignment;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionHandler {

  private final ConcurrentHashMap<UUID, Connection> connections;
  private final ConcurrentLinkedQueue<Thread> connectionThreads;
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  private final UUID serverUUID = UUID.nameUUIDFromBytes("server".getBytes());


  public ConnectionHandler() {
    connections = new ConcurrentHashMap<>();
    connectionThreads = new ConcurrentLinkedQueue<>();
  }

  public void handleConnection(Socket accepted) {
    System.out.println("connection established with " + accepted.toString());
    Connection connection = new Connection(accepted);
    UUID uuid = UUID.nameUUIDFromBytes(accepted.getInetAddress().toString().getBytes());
    Thread thread = new Thread(connection);
    connectionThreads.add(thread);
    connection.send(new MUUIDAssignment(serverUUID, uuid));
    thread.start();
    connections.put(uuid, connection);

  }

  public Connection getConnectionByUUID(UUID uuid) {
    return connections.get(uuid);
  }


}
