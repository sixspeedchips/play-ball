package io.libsoft.playball.server.connection.message;

import java.util.UUID;

public class MUUIDAssignment implements Message {

  private final UUID server;
  private final UUID assigned;

  public MUUIDAssignment(UUID server, UUID assigned) {
    this.server = server;
    this.assigned = assigned;
  }

  @Override
  public UUID getSenderUUID() {
    return server;
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.ASSIGN_UUID;
  }


  @Override
  public void unpack() {

  }

}
