package io.libsoft.asteroid.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonService {


  public static Gson getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {

    private static final Gson INSTANCE;

    static {
      INSTANCE = new GsonBuilder().setPrettyPrinting().create();
    }

  }
}
