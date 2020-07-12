package io.libsoft.playball.test;


public class Main {


  public static void main(String[] args) {
    int port = 10000;
    String host = "localhost";

    Client client = null;
    client = new Client(host, port);
    client.start();


  }
}
