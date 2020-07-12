package io.libsoft.playball.model;

public class Main {


  public static void main(String[] args) throws InterruptedException {

    Space space = new Space();

    Thread t = new Thread(space);
    t.start();
    t.join();
    System.exit(0);

  }
}

