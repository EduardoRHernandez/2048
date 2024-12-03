package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.FriendDatabase;
import model.User;
import model.UserFileHandler;

public class FriendDatabaseController {
    public static boolean addFriend(String username, String friendUsername, String userFile) {
        User friendUser;
        try {
            friendUser = UserFileHandler.getUser(friendUsername, userFile);
            return FriendDatabase.addFriend(username, friendUser);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFriend(String username, String friendUsername, String userFile) {
        User friendUser;
        try {
            friendUser = UserFileHandler.getUser(friendUsername, userFile);
            return FriendDatabase.removeFriend(username, friendUser);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getFriends(String username) {
        List<User> friends = FriendDatabase.getFriends(username);
        List<User> deepCopies = new ArrayList<>();
        for (User friend : friends) {
            deepCopies.add(new User(friend.getUsername(), friend.getPassword(), friend.getEmail(), friend.getName(),
                    friend.getHighestScore(), true));
        }
        return Collections.unmodifiableList(deepCopies);

    }

    public static void print() {
        FriendDatabase.print();
    }

    public static void clear() {
        FriendDatabase.clear();
    }

}
