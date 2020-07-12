package io.libsoft.playball.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Connection {

  private ObjectInputStream objectInputStream;
  private ObjectOutputStream objectOutputStream;


  public Connection(InputStream inputStream, OutputStream outputStream) {
    try {

      objectInputStream = new ObjectInputStream(inputStream);
      objectOutputStream = new ObjectOutputStream(outputStream);
    } catch (IOException ignored) {

    }


  }

  public ObjectInputStream getObjectInputStream() {
    return objectInputStream;
  }

  public ObjectOutputStream getObjectOutputStream() {
    return objectOutputStream;
  }
}
