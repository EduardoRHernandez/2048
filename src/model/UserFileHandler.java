package model;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UserFileHandler {
    private static final Logger LOGGER = Logger.getLogger(UserFileHandler.class.getName());

    // Private constructor to prevent instantiation
    private UserFileHandler() {
    }

    /**
     * Saves a list of users to the specified file in CSV format.
     * 
     * @param users    the list of users to be saved
     * @param filename the name of the file to save the users to
     * @throws IOException if an I/O error occurs
     * 
     * @pre users is non-null and all fields of the user are valid
     * @pre filename is non-null and not empty
     * @post the users are appended to the file in CSV format
     */
    public static void saveUsersToFile(List<User> users, String filename) {
        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Iterate over each user and serialize their data into a single line
            for (User user : users) {
                String userLine = serializeUser(user);
                writer.write(userLine);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.severe("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Saves a single user to the specified file in CSV format.
     * 
     * @param user     the user to be saved
     * @param filename the name of the file to save the user to
     * @throws IOException if an I/O error occurs
     * @pre user != null and all fields of the user are valid
     * @pre filename is non-null and not empty
     * @post the user is appended to the file in CSV format
     */
    public static void saveUsersToFile(User user, String filename) {
        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // If the file is not empty, write a new line before adding the next user
            if (new File(filename).length() > 0) {
                writer.newLine();
            }

            // Serialize the single user and write to file
            String userLine = serializeUser(user);
            writer.write(userLine);
        } catch (IOException e) {
            LOGGER.severe("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Serialize a user into a CSV-friendly format.
     * 
     * @param user the user to be serialized
     * @return the serialized user as a string
     * 
     * @pre user != null and all fields are not null and not empty
     * @post the returned string is not null and not empty, and each value is
     *       comma-separated and escaped according to CSV rules
     */
    private static String serializeUser(User user) {
        // Escape special characters like commas in user attributes
        return user.getUsername() + "," +
                escapeComma(user.getEmail()) + "," +
                escapeComma(user.getName()) + "," +
                user.getHighestScore() + "," +
                user.getPassword(); // Save hashed password
    }

    private static User parseUser(String[] parts) {
        try {
            String username = unescapeComma(parts[0]);
            String email = unescapeComma(parts[1]);
            String name = unescapeComma(parts[2]);
            int highestScore = Integer.parseInt(parts[3]);
            String passwordHash = parts[4]; // hashed password
            return new User(username, passwordHash, email, name, highestScore, true);
        } catch (NumberFormatException e) {
            LOGGER.warning("Malformed data: " + Arrays.toString(parts));
            return null;
        }
    }

    /**
     * Load users from a CSV file.
     * 
     * @param filename the path to the file to read from
     * @return a list of users loaded from the file
     * 
     * @pre filename is non-null and not empty
     * @post if the file is empty or does not exist, the returned list is empty
     * @post if the file contains malformed data, a warning will be logged and the
     *       returned list will not contain the malformed user(s)
     * @post the returned list is not null and not empty if the file contains valid
     *       user data
     */
    public static List<User> loadUsersFromFile(String filename) {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split by comma with all fields
                if (parts.length == 5) {
                    User user = parseUser(parts);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error reading from file: " + e.getMessage());
        }
        return users;
    }

    /**
     * Prints all users in the given list to the console.
     * 
     * @param users the list of users to be printed
     * 
     * @pre users is non-null
     * @post if users is empty, the console will not be modified
     * @post if users contains users, the console will have been printed to with the
     *       string representation of each user
     */
    public static void printUsers(List<User> users) {
        for (User user : users) {
            if (user != null) {
                LOGGER.info(user.toString());
            }
        }
    }

    /**
     * Prints the string representation of the specified user.
     * 
     * @param user the user to be printed
     * @pre user is non-null
     * @post the user information is logged if user is non-null
     */
    public static void printUser(User user) {
        LOGGER.info(user.toString());
    }

    /**
     * Retrieves the user with the specified username and password from the file.
     * 
     * @param username the username of the user to be retrieved
     * @param password the password of the user to be retrieved
     * @param filename the name of the file where the user is stored
     * @return the user with the specified username and password if it exists in the
     *         file,
     *         null otherwise
     * @throws IOException if an I/O error occurs
     * @pre username and password are non-null and not empty
     * @pre filename is non-null and not empty
     * @post the returned user is not null if the user exists in the file, and null
     *       otherwise
     */
    public static User getUser(String username, String password, String filename) {
        List<User> users = loadUsersFromFile(filename);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.isPasswordCorrect(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves the user with the specified username, name, and email from the
     * file.
     * 
     * @param username the username of the user to be retrieved
     * @param name     the name of the user to be retrieved
     * @param email    the email of the user to be retrieved
     * @param filename the name of the file where the user is stored
     * @return the user with the specified username, name, and email if it exists in
     *         the file,
     *         null otherwise
     * @throws IOException if an I/O error occurs
     * @pre username, name, and email are non-null and not empty
     * @pre filename is non-null and not empty
     * @post the returned user is not null if the user exists in the file, and null
     *       otherwise
     */
    public static User getUser(String username, String name, String email, String filename) {
        List<User> users = loadUsersFromFile(filename);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getName().equals(name) && user.getEmail().equals(email)) {
                return new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getName(),
                        user.getHighestScore(), true);
            }
        }
        return null;
    }

    /**
     * Retrieves the user with the specified username from the file.
     * 
     * @param username the username of the user to be retrieved
     * @param filename the name of the file where the user is stored
     * @return the user with the specified username if it exists in the file, null
     *         otherwise
     * @throws IOException if an I/O error occurs
     * @pre username is non-null and not empty
     * @pre filename is non-null and not empty
     * @post the returned user is not null if the user exists in the file, and null
     *       otherwise
     */
    public static User getUser(String username, String filename) {
        List<User> users = loadUsersFromFile(filename);
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getName(),
                        user.getHighestScore(), true);
            }
        }
        return null;
    }

    /**
     * Deletes the user with the specified username from the file.
     *
     * @param username the username of the user to be deleted
     * @param filename the name of the file from which the user is to be deleted
     * @throws IOException if an I/O error occurs
     * @pre username is non-null and not empty
     * @pre filename is non-null and not empty
     * @post the user with the specified username is no longer present in the file
     */
    public static void deleteUser(String username, String filename) {
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
