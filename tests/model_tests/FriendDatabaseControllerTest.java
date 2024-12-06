package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.FriendDatabase;
import model.User;
import model.UserFileHandler;

class FriendDatabaseControllerTest {

    private FriendDatabaseController friendDatabaseController;
    private UserController testUserController;

    private File TEST_FILE;
    private String testFileName = "UserDatabase.csv";
    private String testFriendFileName = "Friends.txt";

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        // Clear the database and initialize the test environment
        FriendDatabase.clear();
        TEST_FILE = new File(testFileName);
        TEST_FILE.delete();
        TEST_FILE.createNewFile();
        testUserController = new UserController(TEST_FILE);
        // Add sample users directly to the test controller
        testUserController.addUser(new User("user1", "password1", "user1@example.com", "User One", 100));
        testUserController.addUser(new User("user2", "password2", "user2@example.com", "User Two", 200));
        testUserController.addUser(new User("user3", "password3", "user3@example.com", "User Three", 300));

        friendDatabaseController = new FriendDatabaseController(testUserController);

        // Redirect System.out to capture the output of print
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
        FriendDatabase.clear();
        System.setOut(originalOut);
    }


    @Test
    void testAddFriend() {
        boolean result = friendDatabaseController.addFriend("user1", "user2");
        assertTrue(result);

        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(1, friends.size());
        assertEquals("user2", friends.get(0).getUsername());
    }

    @Test
    void testAddFriendNonexistentUser() {
        boolean result = friendDatabaseController.addFriend("user1", "nonexistentUser");
        assertFalse(result);
    }

    @Test
    void testRemoveFriend() {
        // Add "user1" and "user2" to "friend1"
        User user1 = new User("user1", "password1", "user1@example.com", "User One", 100);
        User user2 = new User("user2", "password12", "user2@example.com", "User Two", 200);
        testUserController.addUser(user1);
        testUserController.addUser(user2);
        testUserController.addUser(new User("friend1", "password123", "friend1@example.com", "Friend1", 300));
        // Ensure users and friends are added correctly
        friendDatabaseController.addFriend("friend1", "user1");
        friendDatabaseController.addFriend("friend1", "user2");
        
        System.out.println(testUserController.getUser("friend1").toString());

        List<User> friends = FriendDatabase.getFriends("friend1");
        assertEquals(2, friends.size());

        // Remove "user2" from "friend1's" friend list
        assertTrue(friendDatabaseController.removeFriend("friend1", "user2"));

        // Verify that "friend1" now has only one friend
        List<User> friendsAfterRemoval = FriendDatabase.getFriends("friend1");
        assertEquals(1, friendsAfterRemoval.size(), "Friend1 should have only one friend after removal");
        assertEquals("user1", friendsAfterRemoval.get(0).getUsername(), "Friend1 should still have 'user1' as a friend");

        // Verify removing a non-existent friend should return false
        assertFalse(friendDatabaseController.removeFriend("friend1", "nonexistentUser"), "Removing a non-existent friend should return false");

        // Verify removing from a non-existent user should return false
        assertFalse(friendDatabaseController.removeFriend("nonexistentUser", "user1"), "Removing from a non-existent user should return false");
    }

    @Test
    void testGetFriends() {
        friendDatabaseController.addFriend("user1", "user2");
        friendDatabaseController.addFriend("user1", "user3");

        List<User> friends = friendDatabaseController.getFriends("user1");
        assertEquals(2, friends.size(), "User1 should have two friends");
        assertEquals("user2", friends.get(0).getUsername());
        assertEquals("user3", friends.get(1).getUsername());
    }

    @Test
    void testSaveFriends() throws IOException {
        friendDatabaseController.addFriend("user1", "user2");
        friendDatabaseController.addFriend("user1", "user3");

        friendDatabaseController.saveFriends(testFriendFileName);

        File file = new File(testFriendFileName);
        assertTrue(file.exists(), "Friend file should be created");

        // Cleanup
        file.delete();
    }

    @Test
    void testLoadFriends() throws IOException {
        // Add friends
        friendDatabaseController.addFriend("user1", "user2");
        friendDatabaseController.addFriend("user1", "user3");

        // Save users and friends using UserFileHandler and FriendDatabase methods
        UserFileHandler.saveUsersToFile(List.of(
            new User("user1", "password1", "user1@example.com", "User One", 100),
            new User("user2", "password2", "user2@example.com", "User Two", 200),
            new User("user3", "password3", "user3@example.com", "User Three", 300)
        ), testFileName);

        friendDatabaseController.saveFriends(testFriendFileName);

        // Clear the database and reload friends from saved files
        FriendDatabase.clear();
        friendDatabaseController.loadFriends(testFileName, testFriendFileName);

        List<User> friends = friendDatabaseController.getFriends("user1");
        assertEquals(2, friends.size(), "User1 should have two friends after loading");
        assertEquals("user2", friends.get(0).getUsername());
        assertEquals("user3", friends.get(1).getUsername());

        // Cleanup
        new File(testFileName).delete();
        new File(testFriendFileName).delete();
    }

    @Test
    void testPrint() {
    	User user1 = new User("user1", "password1", "user1@example.com", "User One", 100);
        User user2 = new User("user2", "password12", "user2@example.com", "User Two", 200);
        testUserController.addUser(new User("friend1", "password123", "friend1@example.com", "Friend1", 300));
        FriendDatabase.addFriend("friend1", user1);
        FriendDatabase.addFriend("friend1", user2);

        friendDatabaseController.print();

        // Verify that the printed output matches expected text
        String expectedOutput = "friend1 has 2 friends:\nuser1\nuser2";
        assertEquals(expectedOutput, outContent.toString().trim());
    }

    @Test
    void testClear() {
        friendDatabaseController.addFriend("user1", "user2");
        friendDatabaseController.addFriend("user1", "user3");

        friendDatabaseController.clear();

        List<User> friends = friendDatabaseController.getFriends("user1");
        assertTrue(friends.isEmpty(), "User1 should have no friends after clearing");
    }
    
    @Test
    void testLoadWrongFile() {
        String invalidFriendFile = ""; 
        String invalidUserFile = "";

   
        // Redirect System.err to capture the error output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            // Attempt to save friends using an invalid file name
            friendDatabaseController.loadFriends(invalidFriendFile, invalidUserFile);

            // Print captured error output for debugging
            String capturedOutput = errContent.toString();

            // Verify that the error message is printed to System.err
            String expectedErrorMessage = "Error";
            assertTrue(capturedOutput.contains(expectedErrorMessage));
        } finally {
            // Restore the original System.err
            System.setErr(originalErr);
        }
    }

    @Test
    void testSaveWrongFile() {
        String invalidFriendFile = ""; 

        // Redirect System.err to capture the error output
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            // Attempt to save friends using an invalid file name
            friendDatabaseController.saveFriends(invalidFriendFile);

            // Print captured error output for debugging
            String capturedOutput = errContent.toString();

            // Verify that the error message is printed to System.err
            String expectedErrorMessage = "Error saving friends";
            assertTrue(capturedOutput.contains(expectedErrorMessage));
        } finally {
            // Restore the original System.err
            System.setErr(originalErr);
        }
    }

}
