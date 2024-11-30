package model;

import java.io.*;
import java.util.*;

public class UserFileHandler {

    // Private constructor to prevent instantiation
    private UserFileHandler() {
    }

    // Save users to file
    public static void saveUsersToFile(List<User> users, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                // Serialize user attributes into a single line
                writer.write(user.getId() + "," +
                        escapeComma(user.getUsername()) + "," +
                        escapeComma(user.getEmail()) + "," +
                        escapeComma(user.getName()) + "," +
                        user.getHighestScore() + "," +
                        user.isPasswordCorrect("")); // Save hashed password
                writer.newLine();
            }
        }
    }

    public static List<User> loadUsersFromFile(String filename) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // Split by comma with all fields
                if (parts.length == 6) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String username = unescapeComma(parts[1]);
                        String email = unescapeComma(parts[2]);
                        String name = unescapeComma(parts[3]);
                        int highestScore = Integer.parseInt(parts[4]);
                        String passwordHash = parts[5]; // Prehashed password

                        // Recreate the user (pass password hash directly)
                        User user = new User(id, username, passwordHash, email, name, highestScore);
                        users.add(user);
                    } catch (NumberFormatException e) {
                        System.err.println("Malformed data: " + line);
                    }
                }
            }
        }
        return users;
    }

    // Utility methods to handle special characters in data
    private static String escapeComma(String value) {
        return value.replace(",", "\\,"); // Escape commas in user input
    }

    private static String unescapeComma(String value) {
        return value.replace("\\,", ","); // Unescape commas
    }
}
