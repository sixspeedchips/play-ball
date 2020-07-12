package io.libsoft.playball.test;

import io.libsoft.playball.server.connection.message.MHandshake;
import io.libsoft.playball.server.connection.message.Message;
import io.libsoft.playball.server.connection.message.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private final ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
  private UUID uuid;

  public Client(String host, int port) {
    try {
      socket = new Socket(host, port);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      System.exit(-1);
      e.printStackTrace();
    }

  }


  public void sendMessage() {
    try {

      oos.writeObject(new MHandshake(socket, uuid));

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void run() {
    final Future<?> uuidGet = es.submit(this::sendMessage);



    while (true) {
      try {
        Message message = (Message) ois.readObject();

        if (message.getMessageType() ==  MessageType.ASSIGN_UUID){
          uuid = message.getSenderUUID();
          oos.writeObject();
        }
      } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }

  }


}
