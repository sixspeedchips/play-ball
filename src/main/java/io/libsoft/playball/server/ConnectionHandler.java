package io.libsoft.playball.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ConnectionHandler {

  private HashMap<UUID, Socket> connections;


  public ConnectionHandler() {
    connections = new HashMap<>();
  }

  public void handleConnection(Socket accepted) {
    try {
      InputStream is = accepted.getInputStream();
      OutputStream os = accepted.getOutputStream();
    } catch (IOException ignored) {}

  }

}
