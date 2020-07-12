package io.libsoft.playball.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.libsoft.playball.server.connection.message.Message;
import io.libsoft.playball.server.connection.message.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

  private final ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
  private final String host;
  private final int port;
  private final Gson gson = new GsonBuilder()
      .setPrettyPrinting().create();
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;
  private UUID uuid;
  private ScheduledFuture<?> connectionService;
  private boolean running;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
    connect();

  }

  private void connect() {
    try {
      System.out.println("Attempting to connect to server...");
      socket = new Socket(host, port);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      System.out.println("Connected successfully.");
      if (connectionService != null) {
        connectionService.cancel(true);
        this.start();
      }
    } catch (ConnectException e) {
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void sendMessage(Message message) {
    try {
      oos.writeObject(message);
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void run() {
    if (uuid == null) {
      Message m = Message.build().messageType(MessageType.REQ_UUID).sign(uuid);
      sendMessage(m);
    }


    running = true;
    while (running) {
      Message message = null;
      try {
        message = (Message) ois.readObject();
        System.out.println(gson.toJson(message));

        // accepts the new UUID and replies with a subscription request
        // to the the current game state
        if (uuid == null && message.getMessageType() == MessageType.ASSIGN_UUID) {
          uuid = message.getMessageUUID();
          Message r = Message.build().messageType(MessageType.ACCEPTED_UUID).sign(uuid);
          sendMessage(r);
          Message m = Message.build().messageType(MessageType.SUBSCRIBE).sign(uuid);
          sendMessage(m);
        }
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Connection closed by server.");
        running = false;
        try {
          socket.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        connectionService = es.scheduleAtFixedRate(this::connect, 0, 5, TimeUnit.SECONDS);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }


    }

  }


  public void start() {
    Thread t = new Thread(this);
    t.start();
  }
}
