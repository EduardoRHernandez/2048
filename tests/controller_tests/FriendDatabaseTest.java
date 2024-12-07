package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FriendDatabaseTest {

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() throws IOException {
        // 
        user1 = new User("user1", "password123", "user1@email.com", "User One", 100);
        user2 = new User("user2", "password456", "user2@email.com", "User Two", 150);
        user3 = new User("user3", "password789", "user3@email.com", "User Three", 200);
        
        ArrayList<User> Users = new ArrayList();
        
        Users.add(user1);
        Users.add(user2);
        Users.add(user3);
        
        
        UserFileHandler.saveUsersToFile(Users, "UserDatabase.csv");

        FriendDatabase.clear();
    }

    @Test
    void testAddFriend() {
        // Add a friend to user1's list
        assertTrue(FriendDatabase.addFriend("user1", user2));

        // Verify that the friend is added
        assertTrue(FriendDatabase.getFriends("user1").contains(user2));

        // Try adding the same friend again (should not be added again)
        assertFalse(FriendDatabase.addFriend("user1", user2));

        // Add another friend to user1's list
        assertTrue(FriendDatabase.addFriend("user1", user3));

        // Verify that both user2 and user3 are friends of user1
        assertTrue(FriendDatabase.getFriends("user1").contains(user2));
        assertTrue(FriendDatabase.getFriends("user1").contains(user3));
    }

    @Test
    void testGetFriends() {
        // Add some friends for user1
        FriendDatabase.addFriend("user1", user2);
        FriendDatabase.addFriend("user1", user3);

        // Verify that the list of friends for user1 is correct
        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(2, friends.size());
        assertTrue(friends.contains(user2));
        assertTrue(friends.contains(user3));
    }
 
    @Test
    void testRemoveFriend() {
        // Add a friend and then remove it
        FriendDatabase.addFriend("user1", user2);
        assertTrue(FriendDatabase.removeFriend("user1", user2));

        // Verify that the friend is removed
        assertFalse(FriendDatabase.getFriends("user1").contains(user2));

        // Try removing a friend who is not in the list
        assertFalse(FriendDatabase.removeFriend("user1", user2));
        assertFalse(FriendDatabase.removeFriend("user4", user2));
    }

    @Test
    void testClear() {
        // Add some friends for user1
        FriendDatabase.addFriend("user1", user2);
        FriendDatabase.addFriend("user1", user3);

        // Clear the friend database
        FriendDatabase.clear();

        // Verify that all friends are removed
        assertTrue(FriendDatabase.getFriends("user1").isEmpty());
    }

    @Test
    void testPrint() {
        // Add a friend for user1
        FriendDatabase.addFriend("user1", user2);
        assertDoesNotThrow(() -> FriendDatabase.print());
    }
    
    @Test
    void testSaveFriends() throws IOException {
        // Add friends to the database
        FriendDatabase.addFriend("user1", user2);
        FriendDatabase.addFriend("user1", user3);

        // Redirect the save function to the temporary file
        FriendDatabase.saveFriends();

        // Verify that the file exists
        assertTrue(Files.exists(Paths.get("Friends.txt")));

        // Verify the file content (basic checks)
        List<String> lines = Files.readAllLines(Paths.get("Friends.txt"));
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("user1"));
        assertTrue(lines.get(0).contains("user2"));
        assertTrue(lines.get(0).contains("user3"));
    }

    @Test
    void testLoadFriends() {
        // Save the friends to the file
        FriendDatabase.addFriend("user1", user2);
        FriendDatabase.addFriend("user1", user3);
        FriendDatabase.saveFriends();

        // Clear the database to ensure loading is effective
        FriendDatabase.clear();
        assertTrue(FriendDatabase.getFriends("user1").isEmpty());

        // Load the friends from the file
        FriendDatabase.loadFriends("UserDatabase.csv", "Friends.txt");

        // Verify the friends are loaded correctly
        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(2, friends.size());
        assertEquals("user2",friends.get(0).getUsername());
        assertEquals("user3",friends.get(1).getUsername());
    }
}
