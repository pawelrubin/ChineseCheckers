public class Game {
  private int numOfPlayers;
  Board board;
  private Player currentPlayer;

  public Game(int numOfPlayers) {
    this.numOfPlayers = numOfPlayers;
    board = new Board(numOfPlayers);
  }


}
