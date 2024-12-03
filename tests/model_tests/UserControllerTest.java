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
        assertEquals(2, users.size()); // Assuming duplicate entries are allowed
    }

    @Test
    void testDeleteUser() throws IOException {
        userController.addUser("testuser", "password123", "test@example.com", "Test User", 100);
        UserFileHandler.deleteUser("testuser", TEST_FILE);

        List<User> users = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(0, users.size());
    }
}
