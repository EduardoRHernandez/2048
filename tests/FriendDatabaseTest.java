package model;
import model.FriendDatabase;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FriendDatabaseTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        FriendDatabase.clear();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        FriendDatabase.clear();
        System.setOut(originalOut);
    }

    @Test
    void testAddFriendSuccess() {
        User user1 = new User("user1", "password1", "user1@example.com", "User One", 100);
        User friend = new User("friend1", "password2", "friend1@example.com", "Friend One", 200);

        assertTrue(FriendDatabase.addFriend("user1", friend));
        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(1, friends.size());
        assertEquals("friend1", friends.get(0).getUsername());
    }

    @Test
    void testAddDuplicateFriend() {
        User friend = new User("friend1", "password2", "friend1@example.com", "Friend One", 200);

        FriendDatabase.addFriend("user1", friend);
        assertFalse(FriendDatabase.addFriend("user1", friend));
        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(1, friends.size());
    }

    @Test
    void testGetFriendsEmptyList() {
        List<User> friends = FriendDatabase.getFriends("nonexistentUser");
        assertTrue(friends.isEmpty());
    }

    @Test
    void testRemoveFriendSuccess() {
    	User user1 = new User("user1", "password1", "user1@example.com", "User One", 100);
    	User user2 = new User("user2", "password12", "user1@example.com", "User Two", 100);
        User friend = new User("friend1", "password2", "friend1@example.com", "Friend One", 200);

        FriendDatabase.addFriend("friend1", user1);
        assertTrue(FriendDatabase.removeFriend("friend1", user1));
        
        FriendDatabase.addFriend("friend1", user1);
        FriendDatabase.addFriend("friend1", user2);
        List<User> friends = FriendDatabase.getFriends("friend1"); 
        assertEquals(2, friends.size());
        FriendDatabase.removeFriend("friend1", user2);
        List<User> friendsAgain = FriendDatabase.getFriends("friend1");
        assertEquals(1, friendsAgain.size());
        assertEquals("user1", friends.get(0).getUsername());
    }  
   
    @Test
    void testRemoveNonexistentFriend() {
        User friend = new User("friend1", "password2", "friend1@example.com", "Friend One", 200);

        assertFalse(FriendDatabase.removeFriend("user1", friend));
    }

    @Test
    void testClearDatabase() {
        User friend1 = new User("friend1", "password1", "friend1@example.com", "Friend One", 100);
        User friend2 = new User("friend2", "password2", "friend2@example.com", "Friend Two", 200);

        FriendDatabase.addFriend("user1", friend1);
        FriendDatabase.addFriend("user1", friend2);

        FriendDatabase.clear();
        assertTrue(FriendDatabase.getFriends("user1").isEmpty());
    }

    @Test
    void testSaveAndLoadFriends() throws IOException {
        User user1 = new User("user1", "password1", "user1@example.com", "User One", 100);
        User friend1 = new User("friend1", "password2", "friend1@example.com", "Friend One", 200);
        User friend2 = new User("friend2", "password3", "friend2@example.com", "Friend Two", 300);

        FriendDatabase.addFriend("user1", friend1);
        FriendDatabase.addFriend("user1", friend2);

        String friendFile = "Friends.txt";
        String userFile = "UserDatabase.csv";

        UserFileHandler.saveUsersToFile(List.of(user1, friend1, friend2), userFile);
        FriendDatabase.saveFriends(friendFile);

        FriendDatabase.clear();
        FriendDatabase.loadFriends(userFile, friendFile);

        List<User> friends = FriendDatabase.getFriends("user1");
        assertEquals(2, friends.size());
        assertEquals("friend1", friends.get(0).getUsername());
        assertEquals("friend2", friends.get(1).getUsername());

        // Cleanup
        new File(friendFile).delete();
        new File(userFile).delete();
    }

    @Test
    void testPrint() {
        User friend1 = new User("friend1", "password1", "friend1@example.com", "Friend One", 100);
        User friend2 = new User("friend2", "password2", "friend2@example.com", "Friend Two", 200);

        FriendDatabase.addFriend("user1", friend1);
        FriendDatabase.addFriend("user1", friend2);

        FriendDatabase.print();

        String expectedOutput = "user1 has 2 friends:\nfriend1\nfriend2";
        assertEquals(expectedOutput, outContent.toString().trim());
    }
}
