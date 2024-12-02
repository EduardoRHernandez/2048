package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
public class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new Random(42));
    }
 
    @Test
    void testInitialization() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(0, board.getTile(i, j).getValue());
                assertFalse(board.getTile(i, j).isMerged());
            }
        }
    }

    @Test
    void testSetAndGetTile() {
        Tile tile = new Tile();
        tile.setValue(2);
        board.setTile(0, 0, tile);
        assertEquals(2, board.getTile(0, 0).getValue());
        assertFalse(board.getTile(0, 0).isMerged());
    }

    @Test
    void testResetMergedState() {
        Tile tile = new Tile();
        tile.setValue(2);
        tile.setMerged(true);
        board.setTile(0, 0, tile);
        board.resetMergedState();
        assertFalse(board.getTile(0, 0).isMerged());
    }

    @Test
    void testMoveLeft() {
        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(2);

        board.setTile(0, 0, tile1);
        board.setTile(0, 1, tile2);
        board.moveLeft();

        assertEquals(4, board.getTile(0, 0).getValue(), "Tile at (0, 0) should have value 4 after merge.");
        assertEquals(0, board.getTile(0, 1).getValue(), "Tile at (0, 1) should be empty after merge.");
        assertFalse(board.getTile(0, 0).isMerged(), "Tile at (0, 0) should not retain merged state after reset.");
    }

    @Test
    void testMoveRight() {
        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(2);

        board.setTile(0, 2, tile1);
        board.setTile(0, 3, tile2);
        board.moveRight();

        System.out.println(board.toString());
        assertEquals(4, board.getTile(0, 3).getValue(), "Tile at (0, 3) should have value 4 after merge.");
        assertEquals(0, board.getTile(0, 2).getValue(), "Tile at (0, 2) should be empty after merge.");
    }
    
    @Test 
    void testMoveUp() {
    	Tile tile1 = new Tile();
    	tile1.setValue(2);
    	Tile tile2 = new Tile();
    	tile2.setValue(2);
    	
    	board.setTile(0, 0, tile1);
    	board.setTile(0, 1, tile2);
    	board.moveUp();
    	
    	assertEquals(4, board.getTile(0,0).getValue());
    	assertEquals(0, board.getTile(0, 1).getValue());
    }
    
    @Test 
    void testMoveDown() {
    	Tile tile1 = new Tile();
    	tile1.setValue(2);
    	Tile tile2 = new Tile();
    	tile2.setValue(2);
    	
    	board.setTile(0, 2, tile1);
    	board.setTile(0, 3, tile2);
    	board.moveDown();
    	
    	assertEquals(4, board.getTile(0,3).getValue());
    	assertEquals(0, board.getTile(0, 2).getValue());
    }

    @Test
    void testAddRandomTile() {
        board.addRandomTile();
        int nonZeroTiles = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board.getTile(i, j).getValue() != 0) {
                    nonZeroTiles++;
                }
            }
        }

        assertEquals(1, nonZeroTiles);
    }

    @Test
    void testIsGameOver() {
    	int val = 2;
        	for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Tile tile = new Tile();
                    tile.setValue(val);
                    board.setTile(i, j, tile);
                    val += 2;
                }
            }

        assertTrue(board.isGameOver());
    }

    @Test
    void testToString() {
        Tile tile = new Tile();
        tile.setValue(2);
        board.setTile(0, 0, tile);

        String boardString = board.toString();
        assertTrue(boardString.contains("2"));
        assertTrue(boardString.contains("0"));
    }
}
