package io.libsoft.asteroid.server.connection;

import java.util.List;
import java.util.UUID;
import javafx.scene.input.KeyCode;

public interface ConnectionManager {

  void gameStateSubscription(UUID uuid, Connection connection);

  void successfulConnection(UUID uuid, Connection connection);

  UUID getServerUUID();

  void control(UUID uuid, List<KeyCode> kc);

  void clearEntity(UUID uuid);

  UUID setOrGetUUID(String username);

  void connectionHold(UUID uuid);

}
