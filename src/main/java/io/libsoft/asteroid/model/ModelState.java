package io.libsoft.asteroid.model;

import io.libsoft.messenger.GameState;
import java.util.List;
import java.util.UUID;
import javafx.scene.input.KeyCode;

public interface ModelState {

  GameState getState();

  void removeEntityByUUID(UUID uuid);

  void setFrozen(UUID uuid, boolean frozen);

  boolean containsEntity(UUID uuid);

  void addEntity(UUID uuid);

  void updateEntityControl(UUID uuid, List<KeyCode> kc);
}
