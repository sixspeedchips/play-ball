package io.libsoft.asteroid.server;


import io.libsoft.asteroid.model.Space;
import io.libsoft.asteroid.server.connection.ConnectionHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ServerSocket implements Runnable {

  private final ConnectionHandler connectionHandler;
  private final Space space;

  public Server(int port, int backlog, InetAddress bindAddr) throws IOException {
    super(port, backlog, bindAddr);
    space = new Space();
    space.start();
    connectionHandler = new ConnectionHandler(space);

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
