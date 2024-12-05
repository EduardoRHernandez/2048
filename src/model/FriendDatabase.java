package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            return friends.get(username).remove(friend);
        } else
            return false;
    }

    public static void clear() {
        friends.clear();
    }

    public static void saveFriends() {
        String fileName = "Friends.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, ArrayList<User>> entry : friends.entrySet()) {
                String line = entry.getKey() + ": "
                        + entry.getValue().stream().map(User::getUsername).collect(Collectors.joining(", "));
                writer.println(line);// Write each entry to the file
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void loadFriends(String userFile, String friendFile) {

        try (BufferedReader reader = new BufferedReader(new FileReader(friendFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ", -1); // Split by colon with all fields
                String username = parts[0];
                String friendsString = parts[1];
                String[] friendsArray = friendsString.split(", ");
                ArrayList<User> friends = new ArrayList<>();
                for (String friend : friendsArray) {
                    friends.add(UserFileHandler.getUser(friend, userFile));
                }
                FriendDatabase.friends.put(username, friends);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void print() {
        friends.forEach((username, friends) -> {
            System.out.println(username + " has " + friends.size() + " friends:");
            friends.forEach(friend -> System.out.println(friend.getUsername()));
        });
    }
}
