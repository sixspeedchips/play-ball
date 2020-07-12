package io.libsoft.playball.server;


import java.io.IOException;
import java.net.ServerSocket;

public class Server extends ServerSocket implements Runnable {

  private ConnectionHandler connectionHandler;

  public Server(int port) throws IOException {
    super(port);
    connectionHandler = new ConnectionHandler();
  }

  @Override
  public void run() {
    System.out.println("Awaiting clients on port: " + getLocalPort());
    while (true){
      try {
        connectionHandler.handleConnection(accept());
      } catch (IOException ignored) {}

    }


  }

}
