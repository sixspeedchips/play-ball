package io.libsoft.asteroid.model;


import io.libsoft.messenger.service.GsonService;
import io.libsoft.physix.vector.Vector2d;
import io.libsoft.physix.vector.VectorFactory;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javafx.scene.input.KeyCode;

public class Entity {

  private final Vector2d velocity;
  private final Vector2d acceleration;
  private final Vector2d position;
  private transient final ModelSpace modelSpace;

  private double theta = 0;
  private boolean paused;
  private double angularVelocity = 0;
  private UUID uuid;

  public Entity(ModelSpace modelSpace) {
    this.modelSpace = modelSpace;
    velocity = VectorFactory.mutableZeroVector();
    position = VectorFactory.mutableZeroVector();
    acceleration = VectorFactory.mutableZeroVector();
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
    double reduceV = Math.exp(-(1 + velocity.getMagnitude()) * modelSpace.getStateSpaceConstraint().getFriction());
    double reduceA =
        Math.exp(-(1 + velocity.getMagnitude()) * modelSpace.getStateSpaceConstraint().getFriction() * 1e-2);
    double angularReduction = Math.exp(-(1 + angularVelocity) / 13);

    acceleration.multiply(reduceA);
    velocity.multiply(reduceV);
    theta += angularVelocity;
    angularVelocity *= angularReduction;
    velocity.add(acceleration);
    position.add(velocity);
    position.setX(mod(position.getX(), modelSpace.getStateSpaceConstraint().getWidth()));
    position.setY(mod(position.getY(), modelSpace.getStateSpaceConstraint().getHeight()));
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
    double x, y;
    for (KeyCode keyCode : kc) {
      switch (keyCode) {

        case A:
//          updateHeading(-Math.PI / 180*4);
          angularVelocity -= Math.PI / 180 * modelSpace.getStateSpaceConstraint().getAngularVelocityModifier();
          break;
        case S:
          x = modelSpace.getStateSpaceConstraint().getAcceleration() * Math.cos(theta);
          y = modelSpace.getStateSpaceConstraint().getAcceleration() * Math.sin(theta);
          acceleration.add(-x, -y);
          break;
        case D:
//          updateHeading(Math.PI / 180*4);
          angularVelocity += Math.PI / 180 * modelSpace.getStateSpaceConstraint().getAngularVelocityModifier();
          break;
        case W:
          x = modelSpace.getStateSpaceConstraint().getAcceleration() * Math.cos(theta);
          y = modelSpace.getStateSpaceConstraint().getAcceleration() * Math.sin(theta);
          acceleration.add(x, y);
          break;
        case SPACE:
          acceleration.multiply(modelSpace.getStateSpaceConstraint().getBrake());
          velocity.multiply(modelSpace.getStateSpaceConstraint().getBrake());
          angularVelocity *= modelSpace.getStateSpaceConstraint().getBrake();
          break;
      }
    }

  }

  private void updateHeading(double heading) {
    this.theta = mod(this.theta + heading, 2 * Math.PI);
  }

  public double getTheta() {
    return theta;
  }
}
