package model;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UserFileHandler {
    private static final Logger LOGGER = Logger.getLogger(UserFileHandler.class.getName());

    // Private constructor to prevent instantiation
    private UserFileHandler() {
    }

    // Save users to file
    public static void saveUsersToFile(List<User> users, String filename) throws IOException {
        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Iterate over each user and serialize their data into a single line
            for (User user : users) {
                String userLine = serializeUser(user);
                writer.write(userLine);
                writer.newLine();
            }
        }
    }

    public static void saveUsersToFile(User user, String filename) throws IOException {
        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // If the file is not empty, write a new line before adding the next user
            if (new File(filename).length() > 0) {
                writer.newLine();
            }

            // Serialize the single user and write to file
            String userLine = serializeUser(user);
            writer.write(userLine);
        }
    }

    // Helper method to serialize a user into a CSV-friendly format
    private static String serializeUser(User user) {
        // Escape special characters like commas in user attributes
        return user.getUsername() + "," +
                escapeComma(user.getEmail()) + "," +
                escapeComma(user.getName()) + "," +
                user.getHighestScore() + "," +
                user.getPassword(); // Save hashed password
    }

    public static List<User> loadUsersFromFile(String filename) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split by comma with all fields
                if (parts.length == 5) {
                    try {
                        String username = unescapeComma(parts[0]);
                        String email = unescapeComma(parts[1]);
                        String name = unescapeComma(parts[2]);
                        int highestScore = Integer.parseInt(parts[3]);
                        String passwordHash = parts[4]; // hashed password

                        // Recreate the user (pass password hash directly)
                        User user = new User(username, passwordHash, email, name, highestScore, true);
                        users.add(user);
                    } catch (NumberFormatException e) {
                        LOGGER.warning("Malformed data: " + line);
                    }
                }
            }
        }
        return users;
    }

    public static void printUsers(List<User> users) {
        for (User user : users) {
            if (user != null) {
                LOGGER.info(user.toString());
            }
        }
    }

    public static void printUser(User user) {
        if (user != null) {
            LOGGER.info(user.toString());
        }
    }

    public static User getUser(String username, String password, String filename) throws IOException {
        List<User> users = loadUsersFromFile(filename);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.isPasswordCorrect(password)) {
                return user;
            }
        }
        return null;
    }

    public static User getUser(String username, String name, String email, String filename) throws IOException {
        List<User> users = loadUsersFromFile(filename);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getName().equals(name) && user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static void deleteUser(String username, String filename) throws IOException {
        List<User> users = loadUsersFromFile(filename);
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsersToFile(users, filename);
    }

    // Utility methods to handle special characters in data
    private static String escapeComma(String value) {
        return value.replace(",", "\\,"); // Escape commas in user input
    }

    private static String unescapeComma(String value) {
        return value.replace("\\,", ","); // Unescape commas
    }
}
