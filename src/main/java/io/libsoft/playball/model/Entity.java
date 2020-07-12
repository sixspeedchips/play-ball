package io.libsoft.playball.model;


import io.libsoft.physix.factory.VectorFactory;
import io.libsoft.physix.vector.MutableVector;

public class Entity {

  private final MutableVector velocity;
  private final Position position;

  public Entity() {
    velocity = VectorFactory.mutableZeroVector();
    position = new Position();
  }


  public double getX() {
    return position.getX();
  }

  public double getY() {
    return position.getY();
  }


}
