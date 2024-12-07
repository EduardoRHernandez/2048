package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
 
class UserTest {

    @Test
    void testConstructorWithPlainPassword() {
        User user = new User("testUser", "password123", "test@example.com", "Test User", 100);
        assertEquals("testUser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }

    @Test
    void testConstructorWithHashedPassword() {
        String hashedPassword = Integer.toHexString("password123".hashCode());
        User user = new User("testUser", hashedPassword, "test@example.com", "Test User", 100, true);
        assertEquals("testUser", user.getUsername());
        assertEquals(hashedPassword, user.getPassword());
        assertTrue(user.isPasswordCorrect("password123"));
    }

    @Test
    void testPasswordReset() {
        User user = new User("testUser", "password123", "test@example.com", "Test User", 100);
        assertTrue(user.isPasswordCorrect("password123"));
        user.resetPassword("newPassword456");
        assertFalse(user.isPasswordCorrect("password123"));
        assertTrue(user.isPasswordCorrect("newPassword456"));
    }

    @Test
    void testSetHighestScoreHigher() {
        User user = new User("testUser", "password123", "test@example.com", "Test User", 100);
        user.setHighestScore(150);
        assertEquals(150, user.getHighestScore());
    }

    @Test
    void testSetHighestScoreLower() {
        User user = new User("testUser", "password123", "test@example.com", "Test User", 100);
        user.setHighestScore(50);
        assertEquals(100, user.getHighestScore()); // Score should not decrease
    }

    @Test
    void testToString() {
        User user = new User("testUser", "password123", "test@example.com", "Test User", 100);
        String expected = "User{username='testUser', email='test@example.com', name='Test User', highestScore=100}";
        assertEquals(expected, user.toString());
    }
}
