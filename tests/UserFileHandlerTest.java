package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*; 
import java.lang.reflect.*;

class UserFileHandlerTest {
    private static final String TEST_FILE = "test_users.csv";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";
    private static final String EMAIL = "test@example.com";
    private static final String NAME = "Test User";
    private static final int HIGHEST_SCORE = 100;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() throws IOException {
        // Create test user
        testUser = new User(USERNAME, PASSWORD, EMAIL, NAME, HIGHEST_SCORE);
        testUsers = List.of(
            testUser,
            new User("user2", "pass2", "user2@example.com", "User Two", 200, true)
        );
        
        // Clear test file
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    void testSaveUsersToFile_list() throws IOException {
        UserFileHandler.saveUsersToFile(testUsers, TEST_FILE);

        List<User> loadedUsers = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(testUsers.size(), loadedUsers.size());
        assertEquals(testUser.getUsername(), loadedUsers.get(0).getUsername());
    }

    @Test
    void testSaveUsersToFile_singleUser() throws IOException {
        UserFileHandler.saveUsersToFile(testUser, TEST_FILE);

        List<User> loadedUsers = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, loadedUsers.size());
        assertEquals(testUser.getUsername(), loadedUsers.get(0).getUsername());
    }

    @Test
    void testLoadUsersFromFile() throws IOException {
        // Prepare test data
        Files.writeString(Paths.get(TEST_FILE), USERNAME + "," + EMAIL + "," + NAME + "," + HIGHEST_SCORE + "," + PASSWORD);

        List<User> loadedUsers = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, loadedUsers.size());
        assertEquals(testUser.getUsername(), loadedUsers.get(0).getUsername());
    }

    @Test
    void testPrintUsers() {
        assertDoesNotThrow(() -> UserFileHandler.printUsers(testUsers));
    }

    @Test
    void testPrintUser() {
        assertDoesNotThrow(() -> UserFileHandler.printUser(testUser));
    }

    @Test
    void testGetUser_usernameAndPassword() throws IOException {
        UserFileHandler.saveUsersToFile(testUser, TEST_FILE);

        User foundUser = UserFileHandler.getUser(USERNAME, PASSWORD, TEST_FILE);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
    }

    @Test 
    void testGetUser_usernameNameAndEmail() throws IOException {
        UserFileHandler.saveUsersToFile(testUser, TEST_FILE);

        User foundUser = UserFileHandler.getUser(USERNAME, NAME, EMAIL, TEST_FILE);
        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void testGetUser_usernameOnly() throws IOException {
        UserFileHandler.saveUsersToFile(testUser, TEST_FILE);

        User foundUser = UserFileHandler.getUser(USERNAME, TEST_FILE);
        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void testDeleteUser() throws IOException {
        UserFileHandler.saveUsersToFile(testUsers, TEST_FILE);

        UserFileHandler.deleteUser(USERNAME, TEST_FILE);

        List<User> loadedUsers = UserFileHandler.loadUsersFromFile(TEST_FILE);
        assertEquals(1, loadedUsers.size());
        assertNotEquals(USERNAME, loadedUsers.get(0).getUsername());
    }

}
