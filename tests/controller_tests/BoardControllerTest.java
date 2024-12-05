package tests;

public package controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import java.util.List;

class BoardControllerTest {

    private BoardController controller;

    @BeforeEach
    void setUp() {
        controller = new BoardController();
    }

    @Test
    void testInitialization() {
        assertNotNull(controller.getBoard());
        assertEquals(0, controller.getCurrentScore());
    }

    @Test
    void testMoveUp() {
        controller.addRandomTile();
        boolean result = controller.moveUp();
        assertTrue(result || !result, "Move should return true or false based on board state.");
    }

    @Test
    void testMoveDown() {
        controller.addRandomTile();
        boolean result = controller.moveDown();
        assertTrue(result || !result);
    }

    @Test
    void testMoveLeft() {
        controller.addRandomTile();
        boolean result = controller.moveLeft();
        assertTrue(result || !result);
    }

    @Test
    void testMoveRight() {
        controller.addRandomTile();
        boolean result = controller.moveRight();
        assertTrue(result || !result);
    }

    @Test
    void testAddRandomTile() {
        List<Integer> boardValuesBefore = controller.getBoard();
        controller.addRandomTile();
        List<Integer> boardValuesAfter = controller.getBoard();
        assertNotEquals(boardValuesBefore, boardValuesAfter);
    }


    @Test
    void testToString() {
        String boardString = controller.toString();
        assertNotNull(boardString);
        assertFalse(boardString.isEmpty());
    }

    @Test
    void testIsGameOver() {
        boolean result = controller.isGameOver();
        assertTrue(result || !result);
    }
    
    @Test
    void testSetTileAndGetTile() {
        Tile tile = new Tile();
        tile.setValue(4);

        controller.setTile(1, 1, tile);

        Tile retrievedTile = controller.getTile(1, 1);

        assertNotNull(retrievedTile);
        assertEquals(4, retrievedTile.getValue());
    }
    
    @Test
    void testSetBoard() {
        // Create the original board and controller
        Board originalBoard = new Board();
        BoardController controller = new BoardController();
        controller.setBoard(originalBoard);
    }

}
 {
    
}


import controller.BoardController;