package controller;

import model.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    private static final String TEST_FILE = "UserDatabase.csv";
    private File testFile;
    private UserController userController;
 
    @BeforeAll
    void setup() {
        testFile = new File(TEST_FILE);
        userController = new UserController(testFile);
    }
 
    @BeforeEach
    void cleanFile() throws IOException {
        if (testFile.exists()) {
            testFile.delete();
        }
        testFile.createNewFile();
    }

    @AfterAll
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }
  
    @Test
    void testAddUser() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        
        assertEquals(1, users.size());
        User user = users.get(0);

        assertEquals("testuser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }

    @Test
    void testGetUser() {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);

        User user = userController.getUser("testuser", "password123");
        assertNotNull(user);

        assertEquals("testuser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }

    @Test
    void testGetUserInvalidCredentials() {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);

        User user = userController.getUser("testuser", "wrongpassword");
        assertNull(user);
    }

    @Test
    void testUpdateUser() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        userController.updateUser("testuser", "password123", 200);

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, users.size());
        User user = users.get(0);

        assertEquals(200, user.getHighestScore());
        assertTrue(user.isPasswordCorrect("password123"));
    }

    @Test
    void testResetPassword() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        userController.resetPassword("testuser", "Test User", "test@example.com", "newpassword");

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, users.size());
        User user = users.get(0);

        assertTrue(user.isPasswordCorrect("newpassword"));
        assertFalse(user.isPasswordCorrect("password123"));
    }

    @Test
    void testAddDuplicateUser() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, users.size()); 
    }

    @Test
    void testDeleteUser() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        UserFileHandler.deleteUser("testuser", TEST_FILE);

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(0, users.size());
    }
     
    @Test
    void testAddUserIOException() {
        // Simulate an IOException during addUser
        File invalidFile = new File("invalid:/path/UserDatabase.csv");
        UserController faultyController = new UserController(invalidFile);

        assertDoesNotThrow(() -> {
            faultyController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        }, "addUser should handle IOException gracefully.");
    }

    @Test
    void testGetUserIOException() {
        // Simulate an IOException during getUser
        File invalidFile = new File("invalid:/path/UserDatabase.csv"); 
        UserController faultyController = new UserController(invalidFile);

        User user = faultyController.getUser("testuser", "password123");
        assertNull(user, "getUser should return null if IOException occurs.");
    }
    
    @Test
    void testAddUserDuplicatePrevention() throws IOException {
        // Add the first user
        boolean isAdded = userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        assertTrue(isAdded);

        // Attempt to add a user with the same username
        isAdded = userController.addUser("testuser", "newpassword", "newemail@example.com", "Duplicate Username", 200);
        assertFalse(isAdded);

        // Attempt to add a user with the same email
        isAdded = userController.addUser("newuser", "newpassword", "test@example.com", "Duplicate Email", 200);
        assertFalse(isAdded);

        // Verify that only the original user exists in the file
        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, users.size());
        User user = users.get(0);

        // Validate original user details
        assertEquals("testuser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }
    
    @Test
    void testAddUserWithUserObject() throws IOException {
        // Create a new User object
        User newUser = new User("testuser", "password123", "test@example.com", "Test User", 100);

        // Add the user
        boolean isAdded = userController.addUser(newUser);
        assertTrue(isAdded);

        // Attempt to add a duplicate user by username
        User duplicateUsernameUser = new User("testuser", "newpassword", "newemail@example.com", "Duplicate Username", 200);
        isAdded = userController.addUser(duplicateUsernameUser);
        assertFalse(isAdded);

        // Attempt to add a duplicate user by email
        User duplicateEmailUser = new User("newuser", "newpassword", "test@example.com", "Duplicate Email", 200);
        isAdded = userController.addUser(duplicateEmailUser);
        assertFalse(isAdded);

        // Verify the file still contains only the original user
        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, users.size());
        User user = users.get(0);

        // Validate original user details
        assertEquals("testuser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }
    
    @Test
    void testGetUserByUsername() throws IOException {
        // Add a user
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);

        // Retrieve the user by username
        User user = userController.getUser("testuser");
        assertNotNull(user);

        // Validate user details
        assertEquals("testuser", user.getUsername());
        assertTrue(user.isPasswordCorrect("password123"));
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(100, user.getHighestScore());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        // Attempt to retrieve a user that doesn't exist
        User user = userController.getUser("nonexistentuser");
        assertNull(user);
    }



}
