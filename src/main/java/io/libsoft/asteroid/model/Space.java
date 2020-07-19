package io.libsoft.asteroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.libsoft.messenger.GameState;
import io.libsoft.messenger.PEntity;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.KeyCode;

public class Space implements ModelState, ModelSpace {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


  private final HashMap<UUID, Entity> entities;
  private final double WIDTH;
  private final double HEIGHT;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private boolean running;
  private GameState currentGameState;

  public Space() {
    entities = new HashMap<>();
    WIDTH = 500;
    HEIGHT = 500;

  }

  public void stop() {
    running = false;
  }


  private void update() {
    currentGameState = new GameState();
//    System.out.println(entities.values());
    for (Entity entity : entities.values()) {
      if (!entity.isPaused()) {
        entity.update();
        currentGameState.getPEntities().add(new PEntity(entity.getX(), entity.getY()));
      }
    }
  }

  public void start() {
    scheduler.scheduleAtFixedRate(this::update, 0, 10, TimeUnit.MILLISECONDS);
  }


  @Override
  public String toString() {
    return gson.toJson(entities);
  }


  @Override
  public GameState getState() {
    return currentGameState;
  }

  @Override
  public void removeEntityByUUID(UUID uuid) {
    entities.remove(uuid);
  }

  @Override
  public void setFrozen(UUID uuid, boolean frozen) {
    if (entities.get(uuid) == null) {
      return;
    }
    if (frozen) {
      entities.get(uuid).pause();
    } else {
      entities.get(uuid).unpause();
    }

  }

  @Override
  public boolean containsEntity(UUID uuid) {
    return entities.containsKey(uuid);
  }

  @Override
  public void addEntity(UUID uuid) {
    Entity entity = Entity.randomEntity(this)
        .randomBounds(200, 200, 300, 300);
//        .randomVelocity(-5e-1, -5e-1, 5e-1, 5e-1);

    entity.setUuid(uuid);
    entities.put(uuid, entity);

  }

  @Override
  public void updateEntityControl(UUID uuid, List<KeyCode> kc) {
    entities.get(uuid).addForce(kc);
  }

  @Override
  public double getWidth() {
    return WIDTH;
  }

  @Override
  public double getHeight() {
    return HEIGHT;
  }

}
