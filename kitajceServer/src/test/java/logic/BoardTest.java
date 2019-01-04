package logic;

import logic.Board;
import logic.Field;
import logic.Pawn;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
  private Board board;

  @Before
  public void prepareBoard() {
    board = new Board(2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentException() {
    board = new Board(5);
  }

  @Test
  public void shouldBeOkay() {
    board = new Board(4);
    board = new Board(3);
    board = new Board(6);
  }

  @Test
  public void shouldReturnField() {
    Field field = board.getField(4, 6);
    assertNotNull(field);
  }

  @Test
  public void shouldReturnNullField() {
    Field field = board.getField(0, 0);
    assertNull(field);
  }

  @Test
  public void shouldReturnPawn() {
    Field field = board.getField(14, 12);
    assertNotNull(field);
  }

  @Test
  public void shouldReturnNullPawn() {
    Pawn pawn = board.getPawn(4, 6);
    assertNull(pawn);
  }

  @Test
  public void pawnShouldMoveInArray() {
    board.movePawn(13, 12, 12, 11);
    assertNull(board.getPawn(13, 12));
  }

  @Test
  public void distance() {
    Field a = board.getField(4, 6);
    Field b = board.getField(4, 8);
    double distance = board.distance(a, b);
    assertEquals( Math.sqrt(Math.pow(Math.abs(a.getX() - b.getX()), 2) +
            Math.pow(Math.abs(a.getY() - b.getY()), 2)), distance, 0.001);
  }

  @Test
  public void shouldReturn10Pawns() {
    List<Pawn> pawns = board.getPawnsByColor("YELLOW");
    assertEquals(10, pawns.size());
  }



}