package io.libsoft.playball.server.connection.message;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

  private final UUID messageUUID;
  private final UUID senderUUID;
  private final UUID recipientUUID;
  private final MessageType messageType;

  private Message(Builder builder) {
    messageUUID = builder.messageUUID;
    senderUUID = builder.senderUUID;
    recipientUUID = builder.recipientUUID;
    messageType = builder.messageType;
  }

  public static Builder build() {
    return new Builder();
  }

  public UUID getMessageUUID() {
    return messageUUID;
  }

  public UUID getSenderUUID() {
    return senderUUID;
  }

  public UUID getRecipientUUID() {
    return recipientUUID;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public static final class Builder {

    private MessageType messageType;
    private UUID messageUUID;
    private UUID senderUUID;
    private UUID recipientUUID;

    private Builder() {

    }

    public Builder messageUUID(UUID messageUUID) {
      this.messageUUID = messageUUID;
      return this;
    }


    public Builder recipientUUID(UUID recipientUUID) {
      this.recipientUUID = recipientUUID;
      return this;
    }

    public Builder messageType(MessageType messageType) {
      this.messageType = messageType;
      return this;
    }

    public Message sign(UUID senderUUID) {
      this.senderUUID = senderUUID;
      return new Message(this);
    }
  }
}
