package controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.User;
import model.UserFileHandler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class FriendDatabaseControllerTest {

    private static final String TEST_USER_FILE = "UserDatabase.txt";
    private static final String TEST_USER1 = "testUser1";
    private static final String TEST_USER2 = "testUser2";
    private static final String TEST_USER3 = "testUser3";
    
    private FriendDatabaseController friendDatabaseController;
    
    @BeforeEach
    public void setUp() {
        // Set up necessary state before each test
        friendDatabaseController = new FriendDatabaseController();
        
        // Create test users and add them to the user file
        try {
            User user1 = new User(TEST_USER1, "password1", "email1@test.com", "User One", 100);
            User user2 = new User(TEST_USER2, "password2", "email2@test.com", "User Two", 150);
            User user3 = new User(TEST_USER3, "password3", "email3@test.com", "User Three", 200);
            
            UserFileHandler.saveUsersToFile(user1, TEST_USER_FILE);
            UserFileHandler.saveUsersToFile(user2, TEST_USER_FILE);
            UserFileHandler.saveUsersToFile(user3, TEST_USER_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddFriend() {
        // Test adding a friend
        boolean result = FriendDatabaseController.addFriend(TEST_USER1, TEST_USER2, TEST_USER_FILE);
        assertTrue(result);
        
        // Check if the friend is in the database
        List<User> friends = FriendDatabaseController.getFriends(TEST_USER1);
        assertEquals(1, friends.size());
        assertEquals(TEST_USER2, friends.get(0).getUsername());
    }

    @Test
    public void testRemoveFriend() {
        // Test removing a friend
        FriendDatabaseController.addFriend(TEST_USER1, TEST_USER2, TEST_USER_FILE);
        boolean result = friendDatabaseController.removeFriend(TEST_USER1, TEST_USER2, TEST_USER_FILE);
        assertTrue(result);

        // Check if the friend is removed
        List<User> friends = FriendDatabaseController.getFriends(TEST_USER1);
        assertEquals(0, friends.size(), "User should have no friends after removal");
    }

    @Test
    public void testGetFriends() {
        // Test retrieving the list of friends
        FriendDatabaseController.addFriend(TEST_USER1, TEST_USER2, TEST_USER_FILE);
        FriendDatabaseController.addFriend(TEST_USER1, TEST_USER3, TEST_USER_FILE);

        List<User> friends = FriendDatabaseController.getFriends(TEST_USER1);
        assertEquals(2, friends.size());
    }

    @Test
    public void testClear() {
        // Test clearing the friends
        FriendDatabaseController.addFriend(TEST_USER1, TEST_USER2, TEST_USER_FILE);
        FriendDatabaseController.clear();

        List<User> friends = FriendDatabaseController.getFriends(TEST_USER1);
        assertTrue(friends.isEmpty());
    }
    
    @Test
    public void testGetUserNotFound() {
        // Test if getUser returns null for a non-existent user
        try {
            User nonExistentUser = UserFileHandler.getUser("nonExistentUser", TEST_USER_FILE);
            assertNull(nonExistentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddFriendIOException() {
        // Test handling IOException in addFriend (e.g., file not found)
        boolean result = FriendDatabaseController.addFriend(TEST_USER1, TEST_USER2, "non_existent_file.txt");
        assertFalse(result);
    }

    @Test
    public void testRemoveFriendIOException() {
        // Test handling IOException in removeFriend (e.g., file not found)
        boolean result = friendDatabaseController.removeFriend(TEST_USER1, TEST_USER2, "non_existent_file.txt");
        assertFalse(result);
    }

}
