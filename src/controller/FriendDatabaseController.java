package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.FriendDatabase;
import model.User;

public class FriendDatabaseController {
    private UserController userController;

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public boolean addFriend(String username, String friendUsername) {
        User friendUser;
        User adderUser;
        friendUser = this.userController.getUser(friendUsername);
        adderUser = this.userController.getUser(username);
        if (friendUser == null) {
            return false;
        }
        return FriendDatabase.addFriend(username, friendUser)
                && FriendDatabase.addFriend(friendUser.getUsername(), adderUser);
    }

    public boolean removeFriend(String username, String friendUsername) {
        User friendUser;
        User removeUser;
        friendUser = userController.getUser(friendUsername);
        removeUser = userController.getUser(username);
        return FriendDatabase.removeFriend(username, friendUser)
                && FriendDatabase.removeFriend(removeUser.getUsername(), friendUser);
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

    public static void saveFriends() {
        FriendDatabase.saveFriends();
    }

    public static void loadFriends(String userFile, String friendFile) {
        FriendDatabase.loadFriends(userFile, friendFile);
    }

    public static void print() {
        FriendDatabase.print();
    }

    public static void clear() {
        FriendDatabase.clear();
    }

}
