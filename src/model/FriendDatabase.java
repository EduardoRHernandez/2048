package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendDatabase {

    private static final HashMap<String, ArrayList<User>> friends = new HashMap<>();

    private FriendDatabase() {
    }

    public static boolean addFriend(String username, User friend) {
        if (!friends.containsKey(username)) {
            friends.put(username, new ArrayList<>());
        }
        if (!friends.get(username).contains(friend)) {
            friends.get(username).add(friend);
            return true;
        } else
            return false;
    }

    public static List<User> getFriends(String username) {
        return friends.getOrDefault(username, new ArrayList<>());
    }

    public static boolean removeFriend(String username, User friend) {
        if (friends.containsKey(username)) {
            friends.get(username).remove(friend);
            return true;
        } else
            return false;
    }

    public static void clear() {
        friends.clear();
    }

    public static void print() {
        friends.forEach((username, friends) -> {
            System.out.println(username + " has " + friends.size() + " friends:");
            friends.forEach(friend -> System.out.println(friend.getUsername()));
        });
    }
}
