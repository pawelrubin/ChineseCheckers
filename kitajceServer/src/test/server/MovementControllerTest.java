package server;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import server.Board;
import server.MovementController;
import server.Pawn;
import server.Field;


public class MovementControllerTest {
  private Board board;
  private MovementController controller;

  @Before
  public void prepareBoard() {
    this.board = new Board(6);
    this.controller = new MovementController(board);
  }

  @Test
  public void testMoveValidation() {
    Pawn pawn = board.getPawn(3, 4);
    Field field = board.getField(4, 5);
    assertTrue(controller.isValid(3,4, field, pawn.getColor()));
  }

  @Test
  public void testJumpValidation() {
    Pawn pawn = board.getPawn(2, 4);
    Field field = board.getField(4, 4);
    assertTrue(controller.isValid(2, 4, field, pawn.getColor()));
  }
}