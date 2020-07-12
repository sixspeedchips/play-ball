package io.libsoft.playball.model;

import com.sun.xml.internal.ws.resources.UtilMessages;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Space implements Runnable{


  private final HashMap<UUID, Entity> entities;
  private boolean running;


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
        Thread.sleep(1000);
      } catch (InterruptedException ignored) {
      }

    }
  }



}
