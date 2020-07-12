package io.libsoft.playball.server;


import io.libsoft.playball.server.connection.ConnectionHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import sun.rmi.runtime.Log;

public class Server extends ServerSocket implements Runnable {

  private final ConnectionHandler connectionHandler;

  public Server(int port, int backlog, InetAddress bindAddr) throws IOException {
    super(port, backlog, bindAddr);
    connectionHandler = new ConnectionHandler();
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
