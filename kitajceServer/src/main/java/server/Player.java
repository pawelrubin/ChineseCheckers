package server;

abstract class Player extends Thread {
  String color;
  volatile boolean alive;

  /**
   * Changes alive value to false in order to stop the thread
   */
  void kill() {
    alive = false;
  }
}
