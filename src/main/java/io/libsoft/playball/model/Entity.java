package io.libsoft.playball.model;


import io.libsoft.physix.factory.VectorFactory;
import io.libsoft.physix.vector.MutableVector;
import java.util.Random;
import java.util.UUID;

public class Entity {

  private final MutableVector velocity;
  private final MutableVector position;
  private UUID uuid = UUID.randomUUID();

  public Entity() {
    velocity = VectorFactory.mutableZeroVector();
    position = VectorFactory.mutableZeroVector();
  }

  public static Entity randomEntity() {
    return new Entity();
  }


  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public void addForce(double x, double y) {
    velocity.add(x, y);
  }

  public void update() {
    position.add(velocity);
  }


  public double getX() {
    return position.getX();
  }

  public double getY() {
    return position.getY();
  }


  public Entity randomBounds(double x1, double y1, double x2, double y2) {

    double newX = new Random().nextDouble() * (x2 - x1) + x1;
    double newY = new Random().nextDouble() * (y2 - y1) + y1;
    position.multiply(0).add(newX, newY);
    return this;
  }

  public Entity randomVelocity(double x1, double y1, double x2, double y2) {
    double newX = new Random().nextDouble() * (x2 - x1) + x1;
    double newY = new Random().nextDouble() * (y2 - y1) + y1;
    velocity.multiply(0).add(newX, newY);
    return this;
  }

  @Override
  public String toString() {
    return "Entity{" +
        "velocity=" + velocity +
        ", position=" + position +
        ", uuid=" + uuid +
        '}';
  }
}
