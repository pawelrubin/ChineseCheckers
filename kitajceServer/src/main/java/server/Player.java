package server;

abstract class Player extends Thread {
  String color;
  boolean alive;

  void kill() {
    alive = false;
  }
}
