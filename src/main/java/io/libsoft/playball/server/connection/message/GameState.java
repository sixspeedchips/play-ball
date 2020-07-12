package io.libsoft.playball.server.connection.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.libsoft.playball.model.Entity;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class GameState implements Serializable {


  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private final List<PseudoEntity> pseudoEntities = new LinkedList<>();


  public List<PseudoEntity> getPseudoEntities() {
    return pseudoEntities;
  }

  public void addEntity(Entity entity) {
    pseudoEntities.add(new PseudoEntity(entity.getX(), entity.getY()));
  }


  @Override
  public String toString() {
    return gson.toJson(this);
  }

  private static class PseudoEntity implements Serializable {

    private final double x;
    private final double y;

    public PseudoEntity(double x, double y) {
      this.x = x;
      this.y = y;
    }


    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }
  }


}
