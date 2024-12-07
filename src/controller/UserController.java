package controller;

import java.io.File;
import java.util.List;

import model.*;

public class UserController {
    File usersFile;

    public UserController(File usersFile) {
        this.usersFile = usersFile;
    }

    /**
     * Add a user to the user database file.
     * 
     * @param aUser the user to add
     * @return true if the user was added successfully, false if the user could not
     *         be added
     * @pre aUser != null
     */
    public boolean addUser(User aUser) {
        // Load all users from the file
        List<User> existingUsers = UserFileHandler.loadUsersFromFile(usersFile.getPath());
        // Check for duplicates in username or email
        for (User user : existingUsers) {
            if (user.getUsername().equalsIgnoreCase(aUser.getUsername())) {
                return false;
            }
            if (user.getEmail().equalsIgnoreCase(aUser.getEmail())) {
                return false;
            }
        }
        UserFileHandler.saveUsersToFile(aUser, usersFile.getPath());
        return true;
    }

    /**
     * Adds a new user to the user database file.
     * 
     * @param username     the username of the new user
     * @param password     the password of the new user
     * @param email        the email of the new user
     * @param name         the name of the new user
     * @param highestScore the highest score of the new user
     * @return true if the user was added successfully, false if a user with the
     *         same username or email already exists
     * @pre username, password, email, and name are non-null and not empty
     * @post the user is added to the database if they do not already exist
     */
    public boolean addUser(String username, String password, String email, String name, int highestScore) {
        User newUser = new User(username, password, email, name, highestScore);
        List<User> existingUsers = UserFileHandler.loadUsersFromFile(usersFile.getPath());
        for (User user : existingUsers) {
            if (user.getUsername().equalsIgnoreCase(username) || user.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        UserFileHandler.saveUsersToFile(newUser, usersFile.getPath());
        return true;
    }

    /**
     * Retrieves the user with the specified username and password from the
     * file.
     * 
     * @param username the username of the user to be retrieved
     * @param password the password of the user to be retrieved
     * @return the user with the specified username and password if it exists in
     *         the file,
     *         null otherwise
     * @pre username and password are non-null and not empty
     * @post the returned user is not null if the user exists in the file, and null
     *       otherwise
     */
    public User getUser(String username, String password) {
        return UserFileHandler.getUser(username, password, usersFile.getPath());
    }

    /**
     * Retrieves the user with the specified username from the file.
     * 
     * @param username the username of the user to be retrieved
     * @return the user with the specified username if it exists in the file,
     *         null otherwise
     * @pre username is non-null and not empty
     * @post the returned user is not null if the user exists in the file, and null
     *       otherwise
     */
    public User getUser(String username) {
        return UserFileHandler.getUser(username, usersFile.getPath());
    }

    /**
     * Updates the user with the specified username and password.
     * 
     * @param username     the username of the user to be updated
     * @param password     the password of the user to be updated
     * @param highestScore the new highest score to set for the user
     * @pre username and password are non-null and not empty
     * @pre highestScore >= 0
     * @post the user's highest score is updated if the user exists
     * @post the user's password remains unchanged
     */
    public void updateUser(String username, String password, int highestScore) {
        User user = getUser(username, password);
        System.out.println(user.toString());
        if (user != null) {
            user.setHighestScore(highestScore);
            UserFileHandler.deleteUser(username, usersFile.getPath());
            UserFileHandler.saveUsersToFile(user, usersFile.getPath());
        }
    }

    /**
     * Resets the password of the specified user.
     * 
     * @param username    the username of the user
     * @param name        the name of the user
     * @param email       the email of the user
     * @param newPassword the new password to set
     * @pre username, name, email, and newPassword are non-null and not empty
     * @post the user's password is updated to the new password if the user exists
     */
    public void resetPassword(String username, String name, String email, String newPassword) {
        User user = null;
        user = UserFileHandler.getUser(username, name, email, usersFile.getPath());
        if (user != null) {
            user.resetPassword(newPassword);
            UserFileHandler.deleteUser(username, usersFile.getPath());
            UserFileHandler.saveUsersToFile(user, usersFile.getPath());
        }
    }
}
