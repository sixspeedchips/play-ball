package io.libsoft.playball.server.connection.message;

import java.io.Serializable;
import java.util.UUID;

public interface Message extends Serializable {

  UUID getSenderUUID();
  MessageType getMessageType();
  void unpack();

}
