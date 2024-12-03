package controller;

import java.io.File;
import java.io.IOException;

import model.*;

public class UserController {
    File usersFile;

    public UserController(File usersFile) {
        this.usersFile = usersFile;
    }

    public void addUser(String username, String password, String email, String name, int highestScore) {
        User newUser = new User(username, password, email, name, highestScore);
        try {
            UserFileHandler.saveUsersToFile(newUser, usersFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
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
