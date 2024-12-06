package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FriendDatabase {

    private static final HashMap<String, ArrayList<User>> friends = new HashMap<>();
    private static final String USERNAME_VALIDATION_MESSAGE = "Username must be non-null and not empty";

    private FriendDatabase() {
    }

    /**
     * Adds a friend to the user's friend list.
     * 
     * @param username the username of the user
     * @param friend   the friend to be added
     * @return true if the friend was successfully added, false otherwise
     * @pre username is non-null and not empty
     * @pre friend is non-null
     * @post if the friend was successfully added, the user's friend list will
     *       contain the friend
     */
    public static boolean addFriend(String username, User friend) {
        assert username != null && !username.isEmpty() : USERNAME_VALIDATION_MESSAGE;
        assert friend != null : "Friend must be non-null";
        if (!friends.containsKey(username)) {
            friends.put(username, new ArrayList<>());
        }
        if (!friends.get(username).contains(friend)) {
            friends.get(username).add(friend);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the list of friends for the given user.
     * 
     * @param username the username of the user
     * @return a list of friends for the given user
     * @pre username is non-null and not empty
     * @post the list of friends is not null and is not empty if the user has
     *       friends
     */
    public static List<User> getFriends(String username) {
        assert username != null && !username.isEmpty() : USERNAME_VALIDATION_MESSAGE;
        if (friends.containsKey(username)) {
            List<User> friendList = friends.get(username).stream()
                    .map(user -> new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getName(),
                            user.getHighestScore(), true))
                    .toList();
            assert friendList != null && !friendList.isEmpty()
                    : "List of friends must not be null or empty if the user has friends";
            return Collections.unmodifiableList(friendList);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Removes a friend from the user's friend list.
     * 
     * @param username the username of the user
     * @param friend   the friend to be removed
     * @return true if the friend was successfully removed, false otherwise
     * @pre username is non-null and not empty
     * @pre friend is non-null
     * @post if the friend was successfully removed, the user's friend list will no
     *       longer contain the friend
     */
    public static boolean removeFriend(String username, User friend) {
        assert username != null && !username.isEmpty() : USERNAME_VALIDATION_MESSAGE;
        assert friend != null : "Friend must be non-null";

        if (friends.containsKey(username)) {
            return friends.get(username).removeIf(f -> f.getUsername().equals(friend.getUsername()));
        } else {
            return false;
        }
    }

    /**
     * Clears all friends from the database.
     * 
     * @pre The friend database is initialized
     * @post The friend database is empty
     */
    public static void clear() {
        assert friends != null : "Friend database must be initialized";
        friends.clear();
        assert friends.isEmpty() : "Friend database should be empty after clear";
    }

    /**
     * @param fileName the name of the file to save friends to
     * @pre fileName is non-null and not empty
     * @post Friends have been saved to the specified file
     * @throws IOException if an I/O error occurs
     */
    public static void saveFriends(String fileName) throws IOException {
        assert fileName != null && !fileName.isEmpty();
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        for (Map.Entry<String, ArrayList<User>> entry : friends.entrySet()) {
            String line = entry.getKey() + ": "
                    + entry.getValue().stream().map(User::getUsername).collect(Collectors.joining(", "));
            writer.println(line); // Write each entry to the file
        }
        writer.close();
    }

    /**
     * Loads friends from the specified files.
     * 
     * @param userFile   the file containing user data
     * @param friendFile the file containing friend relationships
     * @pre userFile and friendFile are non-null and not empty
     * @post Friends have been loaded into the database from the specified files
     */
    public static void loadFriends(String userFile, String friendFile) throws IOException {
        assert userFile != null && !userFile.isEmpty() : "User file must be non-null and not empty";
        assert friendFile != null && !friendFile.isEmpty() : "Friend file must be non-null and not empty";

        BufferedReader reader = new BufferedReader(new FileReader(friendFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ", -1); // Split by colon with all fields
            assert parts.length == 2 : "Each line must contain a username and a list of friends";
            String username = parts[0];
            String friendsString = parts[1];
            String[] friendsArray = friendsString.split(", ");
            ArrayList<User> friends = new ArrayList<>();
            for (String friend : friendsArray) {
                User friendUser = UserFileHandler.getUser(friend, userFile);
                assert friendUser != null : "Friend user must exist in user file";
                friends.add(friendUser);
            }
            FriendDatabase.friends.put(username, friends);
        }
        reader.close();
    }

    public static void print() {
        friends.forEach((username, friends) -> {
            System.out.println(username + " has " + friends.size() + " friends:");
            friends.forEach(friend -> System.out.println(friend.getUsername()));
        });
    }
}
