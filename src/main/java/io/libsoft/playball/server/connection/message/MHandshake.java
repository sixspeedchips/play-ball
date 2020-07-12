package io.libsoft.playball.server.connection.message;

import java.net.Socket;
import java.util.UUID;

public class MHandshake implements Message {

  private final UUID uuid;

  public MHandshake(Socket socket, UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void unpack() {
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.HANDSHAKE;
  }

  @Override
  public UUID getSenderUUID() {
    return uuid;
  }

}
