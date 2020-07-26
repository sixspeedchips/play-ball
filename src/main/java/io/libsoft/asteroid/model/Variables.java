package io.libsoft.asteroid.model;

import io.libsoft.messenger.service.GsonService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Variables {

  private double height = 2000;
  private double width = height * (16d / 9);
  private double acceleration = .1;
  private double angularVelocityModifier = .4;
  private double friction = 1e-3;
  private double brake = .98;

  public Variables() {
    Properties prop = new Properties();
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
      prop.load(inputStream);

      height = Double.parseDouble(prop.getProperty("height", String.valueOf(height)));
      width = Double.parseDouble(prop.getProperty("width", String.valueOf(width)));
      acceleration = Double.parseDouble(prop.getProperty("acceleration", String.valueOf(acceleration)));
      angularVelocityModifier = Double
          .parseDouble(prop.getProperty("angularVelocityMod", String.valueOf(angularVelocityModifier)));
      friction = Double.parseDouble(prop.getProperty("friction", String.valueOf(friction)));
      brake = Double.parseDouble(prop.getProperty("brake", String.valueOf(brake)));
      System.out.println("Set properties from file.");
      System.out.println(GsonService.getPprinter().toJson(this));

    } catch (IOException | NullPointerException ignored) {
      System.out.println("Failed to load properties.");
    }

  }


  public double getHeight() {
    return height;
  }

  public double getWidth() {
    return width;
  }

  public double getAcceleration() {
    return acceleration;
  }

  public double getAngularVelocityModifier() {
    return angularVelocityModifier;
  }

  public double getFriction() {
    return friction;
  }

  public double getBrake() {
    return brake;
  }
}
