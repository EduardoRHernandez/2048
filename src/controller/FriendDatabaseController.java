package controller;

import java.io.IOException;
import java.util.List;
import model.FriendDatabase;
import model.User;

public class FriendDatabaseController {

  private UserController userController;

  /**
   * @param userController the user controller to use when adding and removing
   *                       friends
   * @pre userController != null
   */
  public FriendDatabaseController(UserController userController) {
    this.userController = userController;
  }

  /**
   * @param username       the username of the user who is adding the friend
   * @param friendUsername the username of the friend being added
   * @return true if the friend was added successfully, false if the friend could
   *         not
   *         be added
   * @pre userController != null
   * @pre username != null and not empty
   * @pre friendUsername != null and not empty
   */
  public boolean addFriend(String username, String friendUsername) {
    User friendUser;
    User adderUser;
    friendUser = this.userController.getUser(friendUsername);
    adderUser = this.userController.getUser(username);
    if (friendUser == null) {
      return false;
    }
    return (
      FriendDatabase.addFriend(username, friendUser) &&
      FriendDatabase.addFriend(friendUser.getUsername(), adderUser)
    );
  }

  /**
   * @param username       the username of the user who is removing the friend
   * @param friendUsername the username of the friend being removed
   * @return true if the friend was removed successfully, false if the friend
   *         could
   *         not be removed
   * @pre userController != null
   * @pre username != null and not empty
   * @pre friendUsername != null and not empty
   */
  public boolean removeFriend(String username, String friendUsername) {
    User friendUser;
    User removeUser;
    friendUser = this.userController.getUser(friendUsername);
    removeUser = this.userController.getUser(username);
    if (friendUser == null || removeUser == null) {
      return false;
    }
    return (
      FriendDatabase.removeFriend(username, friendUser) &&
      FriendDatabase.removeFriend(friendUser.getUsername(), removeUser)
    );
  }

  /**
   * @param username the username of the user whose friends are to be retrieved
   * @return the list of friends for the given user
   * @pre username is non-null and not empty
   * @post the list of friends is not null and is not empty if the user has
   *       friends
   */
  public List<User> getFriends(String username) {
    return FriendDatabase.getFriends(username);
  }

  /**
   * Saves the friends database to the specified file.
   *
   * @param friendFile the file to save the friends database to
   * @pre friendFile is non-null and not empty
   * @post the friends database has been saved to the specified file
   */
  public void saveFriends(String friendFile) {
    try {
      FriendDatabase.saveFriends(friendFile);
    } catch (IOException e) {
      System.err.println("Error saving friends: " + e.getMessage());
    }
  }

  /**
   * Loads the friends database from the specified files.
   *
   * @param userFile   the file containing user data
   * @param friendFile the file containing friend relationships
   * @pre userFile and friendFile are non-null and not empty
   * @post the friends database has been loaded from the specified files
   */
  public void loadFriends(String userFile, String friendFile) {
    try {
      FriendDatabase.loadFriends(userFile, friendFile);
    } catch (IOException e) {
      System.err.println("Error loading friends: " + e.getMessage());
    }
  }

  /**
   * Prints the friends database.
   *
   * @pre The friends database is initialized
   * @post The friends database has been printed to the console
   */
  public void print() {
    FriendDatabase.print();
  }

  /**
   * Clears the friends database.
   *
   * @pre The friends database is initialized
   * @post The friends database is empty
   */
  public void clear() {
    FriendDatabase.clear();
  }
}
