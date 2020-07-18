package io.libsoft.asteroid.server;


import io.libsoft.asteroid.model.Entity;
import io.libsoft.asteroid.model.Space;
import io.libsoft.asteroid.server.connection.Connection;
import io.libsoft.asteroid.server.connection.ConnectionHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class Server extends ServerSocket implements Runnable {

  private final ConnectionHandler connectionHandler;
  private final ObservableMap<UUID, Connection> loggedConnections;
  private final Space space;
  private final Thread modelThread;

  public Server(int port, int backlog, InetAddress bindAddr) throws IOException {
    super(port, backlog, bindAddr);
    space = new Space();
    loggedConnections = FXCollections.observableHashMap();
    connectionHandler = new ConnectionHandler(loggedConnections, space);

    modelThread = new Thread(space);
    modelThread.start();

    loggedConnections.addListener((MapChangeListener<? super UUID, ? super Connection>) change -> {
      Entity entity = Entity.randomEntity()
          .randomBounds(200, 200, 300, 300)
          .randomVelocity(-1e-1, -1e-1, 1e-1, 1e-1);
      System.out.println(change.getKey());
      entity.setUuid(change.getKey());
      space.addEntity(entity);
    });

  }


  @Override
  public void run() {


    // TODO switch to a proper logging method, suggest using log4j
    while (true){
      try {
        System.out.println("Awaiting clients on port: " + getLocalPort() + " Host: " + getInetAddress());
        Socket accepted = accept();
        System.out.println("Accepted " + accepted);
        connectionHandler.handleConnection(accepted);
      } catch (IOException ignored) {}

    }


  }


}
