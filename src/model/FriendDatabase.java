package model;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendDatabase {
    private static class SingletonHelper {
        private static final FriendDatabase instance = new FriendDatabase();
    }

    private final HashMap<String, ArrayList<User>> friends = new HashMap<>();

    private FriendDatabase() {
    }

    public static FriendDatabase getInstance() {
        return SingletonHelper.instance;
    }

    public void addFriend(String username, User friend) {
        if (!friends.containsKey(username)) {
            friends.put(username, new ArrayList<>());
        }
        friends.get(username).add(friend);
    }

    public ArrayList<User> getFriends(String username) {
        return friends.getOrDefault(username, new ArrayList<>());
    }

    public void removeFriend(String username, User friend) {
        if (friends.containsKey(username)) {
            friends.get(username).remove(friend);
        }
    }

    public void clear() {
        friends.clear();
    }

    public void print() {
        friends.forEach((username, friends) -> {
            System.out.println(username + " has " + friends.size() + " friends:");
            friends.forEach(friend -> System.out.println(friend.getUsername()));
        });
    }
}
