package tests;

public package model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void testDefaultConstructor() {
        Tile tile = new Tile();

        assertEquals(0, tile.getValue());
        assertFalse(tile.isMerged());
    }

    @Test
    void testCopyConstructor() {
        Tile original = new Tile();
        original.setValue(4);
        original.setMerged(true);

        Tile copy = new Tile(original);

        assertEquals(4, copy.getValue());
        assertTrue(copy.isMerged());

        copy.setValue(8);
        copy.setMerged(false);
        assertEquals(4, original.getValue());
        assertTrue(original.isMerged());
    }

    @Test
    void testSetAndGetValue() {
        Tile tile = new Tile();
        tile.setValue(16);

        assertEquals(16, tile.getValue());
    }

    @Test
    void testSetAndGetMerged() {
        Tile tile = new Tile();
        tile.setMerged(true);

        assertTrue(tile.isMerged());
        tile.setMerged(false);
        assertFalse(tile.isMerged());
    }
}
 {
    
}

import model.Tile;
