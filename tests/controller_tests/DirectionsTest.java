package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionsTest {

    @Test
    void testIsForward() {
        // UP should be forward
        assertTrue(Directions.UP.isForward());
        
        // DOWN should not be forward
        assertFalse(Directions.DOWN.isForward());
        
        // LEFT should be forward
        assertTrue(Directions.LEFT.isForward());
        
        // RIGHT should not be forward
        assertFalse(Directions.RIGHT.isForward());
    }

    @Test
    void testIsRow() {
        // LEFT and RIGHT should be row
        assertTrue(Directions.LEFT.isRow());
        assertTrue(Directions.RIGHT.isRow());
        
        // UP and DOWN should not be row 
        assertFalse(Directions.UP.isRow());
        assertFalse(Directions.DOWN.isRow());
    }

    @Test
    void testToString() {
        // Test if toString returns the correct lowercase string
        assertEquals("up", Directions.UP.toString());
        assertEquals("down", Directions.DOWN.toString());
        assertEquals("left", Directions.LEFT.toString());
        assertEquals("right", Directions.RIGHT.toString());
    }

    @Test
    void testFromString() {
        // Test if fromString correctly converts a string to the corresponding enum
        assertEquals(Directions.UP, Directions.fromString("up"));
        assertEquals(Directions.DOWN, Directions.fromString("down"));
        assertEquals(Directions.LEFT, Directions.fromString("left"));
        assertEquals(Directions.RIGHT, Directions.fromString("right"));

        // Test case-insensitive behavior
        assertEquals(Directions.UP, Directions.fromString("UP"));
    }
}
