package io.libsoft.asteroid.model;


import io.libsoft.messenger.service.GsonService;
import io.libsoft.physix.factory.VectorFactory;
import io.libsoft.physix.vector.MutableVector;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javafx.scene.input.KeyCode;

public class Entity {

  private final MutableVector velocity;
  private final MutableVector position;
  private transient final ModelSpace modelSpace;
  private UUID uuid = UUID.randomUUID();
  private boolean paused;
  private double SPEED = .02;

  public Entity(ModelSpace modelSpace) {
    this.modelSpace = modelSpace;
    velocity = VectorFactory.mutableZeroVector();
    position = VectorFactory.mutableZeroVector();
  }

  public static Entity randomEntity(ModelSpace modelSpace) {
    return new Entity(modelSpace);
  }

  public void pause() {
    paused = true;
  }

  public void unpause() {
    paused = false;
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
    position.setX(mod(position.getX(), modelSpace.getWidth()));
    position.setY(mod(position.getY(), modelSpace.getHeight()));
  }

  private double mod(double a, double b) {

    double r = a % b;
    return r < 0 ? r + b : r;
  }

  public double getX() {
    return position.getX();
  }

  public double getY() {
    return position.getY();
  }

  public boolean isPaused() {
    return paused;
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
    return GsonService.getAnnotater().toJson(this);
  }

  public void addForce(List<KeyCode> kc) {
    for (KeyCode keyCode : kc) {
      switch (keyCode) {

        case A:
          velocity.add(-SPEED, 0);
          break;
        case S:
          velocity.add(0, SPEED);
          break;
        case D:
          velocity.add(SPEED, 0);
          break;
        case W:
          velocity.add(0, -SPEED);
          break;
        case SPACE:
          velocity.multiply(.8);
          break;
      }
    }

  }
}
