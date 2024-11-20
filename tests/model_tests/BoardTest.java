package tests.model_tests;

import model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    @Test
    public void testBoardSize() {
        Board board = new Board();
        assertEquals(4, board.getBoardSize());
    }
}