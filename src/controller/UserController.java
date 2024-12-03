package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class UserController {
    File usersFile;

    public UserController(File usersFile) {
        this.usersFile = usersFile;
    }

    public boolean addUser(String username, String password, String email, String name, int highestScore) {
        User newUser = new User(username, password, email, name, highestScore);
        try {
        	// Load all users from the file
            List<User> existingUsers = UserFileHandler.loadUsersFromFile(usersFile.getPath());
            
            // Check for duplicates in username or email
            for (User user : existingUsers) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    return false;
                }
                if (user.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
        	
            UserFileHandler.saveUsersToFile(newUser, usersFile.getPath());return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUser(String username, String password) {
        try {
            return UserFileHandler.getUser(username, password, usersFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateUser(String username, String password, int highestScore) {
        User user = getUser(username, password);
        if (user != null) {
            user.setHighestScore(highestScore);
            user.resetPassword(password);
            try {
                UserFileHandler.deleteUser(username, usersFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                UserFileHandler.saveUsersToFile(user, usersFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetPassword(String username, String name, String email, String newPassword) {
        User user = null;
        try {
            user = UserFileHandler.getUser(username, name, email, usersFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user != null) {
            user.resetPassword(newPassword);
            try {
                UserFileHandler.deleteUser(username, usersFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                UserFileHandler.saveUsersToFile(user, usersFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
