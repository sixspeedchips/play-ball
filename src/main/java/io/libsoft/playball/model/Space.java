package io.libsoft.playball.model;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Space implements Runnable{


  private final HashMap<UUID, Entity> entities;
  private boolean running;
  private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

  private final double WIDTH;
  private final double HEIGHT;

  public Space() {
    entities = new HashMap<>();
    WIDTH = 1000;
    HEIGHT = 1000;

  }

  public void stop(){
    running = false;
  }



  @Override
  public void run() {
    running = true;
    while (running){
      try {

        for (Entity entity : entities.values()) {
          entity.update();
        }
        System.out.println(this);
        Thread.sleep(1000);
      } catch (InterruptedException ignored) {
      }

    }
  }


  public void addEntity(Entity entity) {
    entities.put(entity.getUuid(), entity);
  }


  @Override
  public String toString() {
    return "Space{" +
        "entities=" + entities +
        '}';
  }
}
