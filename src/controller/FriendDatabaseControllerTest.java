package controller;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.User;

import java.io.File;
import java.util.List;

class FriendDatabaseControllerTest {

    private FriendDatabaseController friendDatabaseController;
    private UserController userController;
    private String username = "user1";
    private String friendUsername = "friend1";
    private static final String TEST_FILE = "UserDatabase.csv";
    private File testFile;

    @BeforeAll
    void setup() {
        testFile = new File(TEST_FILE);
        userController = new UserController(testFile);
        // Initialize FriendDatabaseController
    }
    
    @BeforeEach
    void setUp() {
        User user1 = new User(username, "password", "user@example.com", "User One", 100);
        User user2 = new User(friendUsername, "password", "friend@example.com", "Friend One", 200);
        
        userController.addUser(user1);
        userController.addUser(user2);
    }

    @Test
    void testAddFriend_Success() {
        // Add friend to the database
        boolean result = FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);

        // Assert the friend was added successfully
        assertTrue(result);
    }

    @Test
    void testAddFriend_FriendAlreadyExists() {
        // Add the friend for the first time
        FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);
        
        // Try adding the same friend again
        boolean result = FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);

        // Assert that the friend cannot be added again
        assertFalse(result);
    }

    @Test
    void testRemoveFriend_Success() {
        // Add the friend first
        FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);
        
        // Now remove the friend
        boolean result = friendDatabaseController.removeFriend(username, friendUsername, TEST_FILE);

        // Assert the friend was removed successfully
        assertTrue(result);
    }

    @Test
    void testRemoveFriend_FriendDoesNotExist() {
        // Try removing a friend who hasn't been added
        boolean result = friendDatabaseController.removeFriend(username, friendUsername, TEST_FILE);

        // Assert that the friend cannot be removed
        assertFalse(result);
    }

    @Test
    void testGetFriends() {
        // Add friend to the database
        FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);

        // Get the list of friends
        List<User> friendsList = FriendDatabaseController.getFriends(username);

        // Assert the friend is in the list
        assertNotNull(friendsList, "The returned list should not be null.");
        assertEquals(1, friendsList.size(), "The list should contain one friend.");
        assertEquals(friendUsername, friendsList.get(0).getUsername(), "The friend's username should match.");
    }

    @Test
    void testPrint() {
        // Adding friends and checking the output
        FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);

        // Check the print functionality (manual observation or checking console output)
        // Here we are assuming this method doesn't return a value, so we cannot assert directly.
        assertDoesNotThrow(() -> FriendDatabaseController.print());
    }

    @Test
    void testClear() {
        // Add a friend
        FriendDatabaseController.addFriend(username, friendUsername, TEST_FILE);
        
        // Clear the database
        FriendDatabaseController.clear();
        
        // Verify the database is cleared by checking if there are no friends
        List<User> friendsList = FriendDatabaseController.getFriends(username);
        assertTrue(friendsList.isEmpty());
    }
}
