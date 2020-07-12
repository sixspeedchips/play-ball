package io.libsoft.playball.server;


import io.libsoft.playball.model.Entity;
import io.libsoft.playball.model.Space;
import io.libsoft.playball.server.connection.Connection;
import io.libsoft.playball.server.connection.ConnectionHandler;
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
    connectionHandler = new ConnectionHandler(loggedConnections, space::getCurrentGameState);

    modelThread = new Thread(space);
    modelThread.start();

    loggedConnections.addListener((MapChangeListener<? super UUID, ? super Connection>) change -> {
      Entity entity = Entity.randomEntity()
          .randomBounds(0, 0, 100, 100)
          .randomVelocity(0, 0, 1e-5, 1e-5);
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
