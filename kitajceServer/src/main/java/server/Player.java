package server;

abstract class Player extends Thread {
  String color;
  volatile boolean alive;

  void kill() {
    alive = false;
  }
}
