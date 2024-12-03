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
        board.move(Directions.LEFT);

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
        Tile tile3 = new Tile();
        tile3.setValue(2);
        Tile tile4 = new Tile();
        tile4.setValue(2);
        Tile tile5 = new Tile();
        tile5.setValue(2);
        Tile tile6 = new Tile();
        tile6.setValue(2);

        board.setTile(0, 1, tile1);
        board.setTile(0, 3, tile2);
        board.setTile(1, 1, tile3);
        board.setTile(2, 0, tile4);
        board.setTile(3, 1, tile5);
        board.setTile(3, 3, tile6);
        board.move(Directions.RIGHT);

        assertEquals(4, board.getTile(0, 3).getValue(), "Tile at (0, 3) should have value 4 after merge.");
        assertEquals(0, board.getTile(0, 2).getValue(), "Tile at (0, 2) should be empty after merge.");
    }
     
    @Test 
    void testMoveUp() {
    	Tile tile1 = new Tile();
    	tile1.setValue(2);
    	Tile tile2 = new Tile();
    	tile2.setValue(2);
    	
    	board.setTile(0, 2, tile1);
    	board.setTile(1, 2, tile2);
    	board.move(Directions.UP);
    	
    	assertEquals(4, board.getTile(0,2).getValue());
    	assertEquals(0, board.getTile(1, 2).getValue());
    }
    
    @Test 
    void testMoveDown() {
    	Tile tile1 = new Tile();
    	tile1.setValue(2);
    	Tile tile2 = new Tile();
    	tile2.setValue(2);
    	
    	board.setTile(0, 2, tile1);
    	board.setTile(1, 2, tile2);
    	board.move(Directions.DOWN);
    	
    	assertEquals(4, board.getTile(3,2).getValue());
    	assertEquals(0, board.getTile(1, 2).getValue());
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
    void testCurrentScore() {
    	Tile tile1 = new Tile();
    	tile1.setValue(4);
    	Tile tile2 = new Tile();
    	tile2.setValue(4);
    	
    	board.setTile(0, 2, tile1);
    	board.setTile(1, 2, tile2);
    	board.move(Directions.DOWN);
    	
    	assertEquals(8, board.getCurrentScore());
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
    
    @Test
    void testDefaultConstructor() {
        Board board = new Board();
        assertNotNull(board);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(0, board.getTile(i, j).getValue());
            }
        }
    }

    @Test
    void testGetBoardValues() {
        Board board = new Board(new Random(42));

        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(4);

        board.setTile(0, 0, tile1);
        board.setTile(1, 1, tile2);

        List<Integer> boardValues = board.getBoardValues();

        assertEquals(16, boardValues.size(), "Board should have 16 values.");
        assertEquals(2, boardValues.get(0), "Tile (0,0) should have value 2.");
        assertEquals(4, boardValues.get(5), "Tile (1,1) should have value 4.");
        for (int i = 0; i < 16; i++) {
            if (i != 0 && i != 5) {
                assertEquals(0, boardValues.get(i), "All other tiles should have value 0.");
            }
        }
    }

    @Test
    void testGetRow() {
        Board board = new Board(new Random(42));

        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(4);

        board.setTile(0, 0, tile1);
        board.setTile(0, 3, tile2);

        List<Tile> row = board.getRow(0);

        assertEquals(4, row.size(), "Row should have 4 tiles.");
        assertEquals(2, row.get(0).getValue(), "First tile in row should have value 2.");
        assertEquals(4, row.get(3).getValue(), "Last tile in row should have value 4.");
        for (int i = 1; i < 3; i++) {
            assertEquals(0, row.get(i).getValue(), "Middle tiles should have value 0.");
        }
    }

    @Test
    void testGetColumn() {
        Board board = new Board(new Random(42));

        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(4);

        board.setTile(0, 0, tile1);
        board.setTile(3, 0, tile2);

        List<Tile> column = board.getColumn(0);

        assertEquals(4, column.size(), "Column should have 4 tiles.");
        assertEquals(2, column.get(0).getValue(), "First tile in column should have value 2.");
        assertEquals(4, column.get(3).getValue(), "Last tile in column should have value 4.");
        for (int i = 1; i < 3; i++) {
            assertEquals(0, column.get(i).getValue(), "Middle tiles should have value 0.");
        }
    }
    
    @Test
    void testMoveReturnValue() {
        // Set up initial board state with no possible moves to the left
        Tile tile1 = new Tile();
        tile1.setValue(2);
        Tile tile2 = new Tile();
        tile2.setValue(4);

        board.setTile(0, 0, tile1);
        board.setTile(0, 1, tile2);

        // Try moving left (shouldn't change the state)
        assertFalse(board.move(Directions.LEFT));

        // Set up a new board state where a move will change the state
        Tile tile3 = new Tile();
        tile3.setValue(2);
        board.setTile(1, 0, tile3);
        board.setTile(1, 1, tile3);

        // Try moving left (should merge the tiles and change the state)
        assertTrue(board.move(Directions.LEFT));
    }

    
    @Test
    void testGameIsOverFullBoardWithMoves() {
        int[][] values = {
            {2, 4, 8, 16},
            {32, 64, 128, 256},
            {512, 1024, 2048, 4096},
            {8192, 16384, 16384, 65536}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile tile = new Tile();
                tile.setValue(values[i][j]);
                board.setTile(i, j, tile);
            }
        }

        assertFalse(board.isGameOver(), "Game should not be over if a merge is possible.");
    }
}
