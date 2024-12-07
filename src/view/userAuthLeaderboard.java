package view;

import controller.FriendDatabaseController;
import controller.UserController;
import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import model.User;

public class userAuthLeaderboard {

  private UserController userController;
  private FriendDatabaseController friendController;
  private User loggedInUser;
  private String password;

  private File usersFile;

  public userAuthLeaderboard(FriendDatabaseController friendController) {
    // Initialize the file path
    this.usersFile = new File("src/files/UserDatabase.csv");
    this.friendController = friendController;
    this.userController = new UserController(usersFile);
    this.password = "\0";
  }

  public boolean authenticate(int score) {
    while (loggedInUser == null) {
      Alert choiceAlert = new Alert(Alert.AlertType.CONFIRMATION);
      choiceAlert.setTitle("Authenticate");
      choiceAlert.setHeaderText("Welcome to 2048!");
      choiceAlert.setContentText("Do you want to Sign In or Sign Up?");
      ButtonType signInButton = new ButtonType("Sign In");
      ButtonType signUpButton = new ButtonType("Sign Up");
      ButtonType cancelButton = new ButtonType("Cancel");
      choiceAlert
        .getButtonTypes()
        .setAll(signInButton, signUpButton, cancelButton);

      Optional<ButtonType> result = choiceAlert.showAndWait();

      if (result.isPresent() && result.get() == signInButton) {
        loggedInUser = signIn(score);
      } else if (result.isPresent() && result.get() == signUpButton) {
        loggedInUser = signUp(score);
      } else {
        showError("You must sign in or sign up to continue.");
      }
    }
    return true;
  }

  private User signIn(int score) {
    TextInputDialog usernameDialog = new TextInputDialog();
    usernameDialog.setTitle("Sign In");
    usernameDialog.setHeaderText("Sign In");
    usernameDialog.setContentText("Enter your username:");
    Optional<String> usernameInput = usernameDialog.showAndWait();

    if (usernameInput.isPresent()) {
      TextInputDialog passwordDialog = new TextInputDialog();
      passwordDialog.setTitle("Sign In");
      passwordDialog.setHeaderText("Sign In");
      passwordDialog.setContentText("Enter your password:");
      Optional<String> passwordInput = passwordDialog.showAndWait();

      if (passwordInput.isPresent()) {
        String username = usernameInput.get();
        this.password = passwordInput.get();
        User currentUser = userController.getUser(username, password);

        if (currentUser == null) {
          showError("Invalid username or password. Please try again.");
        } else {
          currentUser.setHighestScore(score);
          userController.updateUser(username, password, score);
          showMessage("Welcome back, " + currentUser.getUsername() + "!");
          return currentUser;
        }
      }
    }
    return null;
  }

  private User signUp(int score) {
    TextInputDialog usernameDialog = new TextInputDialog();
    usernameDialog.setTitle("Sign Up");
    usernameDialog.setHeaderText("Sign Up");
    usernameDialog.setContentText("Enter your username:");
    Optional<String> usernameInput = usernameDialog.showAndWait();

    if (usernameInput.isPresent()) {
      TextInputDialog passwordDialog = new TextInputDialog();
      passwordDialog.setTitle("Sign Up");
      passwordDialog.setHeaderText("Sign Up");
      passwordDialog.setContentText("Enter your password:");
      Optional<String> passwordInput = passwordDialog.showAndWait();

      if (passwordInput.isPresent()) {
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Sign Up");
        emailDialog.setHeaderText("Sign Up");
        emailDialog.setContentText("Enter your email:");
        Optional<String> emailInput = emailDialog.showAndWait();

        if (emailInput.isPresent()) {
          TextInputDialog nameDialog = new TextInputDialog();
          nameDialog.setTitle("Sign Up");
          nameDialog.setHeaderText("Sign Up");
          nameDialog.setContentText("Enter your name:");
          Optional<String> nameInput = nameDialog.showAndWait();

          if (nameInput.isPresent()) {
            String username = usernameInput.get();
            this.password = passwordInput.get();
            String email = emailInput.get();
            String name = nameInput.get();

            boolean success = userController.addUser(
              username,
              password,
              email,
              name,
              score
            );
            if (!success) {
              showError("Username or email already exists. Please try again.");
            } else {
              showMessage("Account created successfully!");
              return userController.getUser(username, password);
            }
          }
        }
      }
    }
    return null;
  }

  public String getLeaderboard() {
    if (loggedInUser == null) return "No friends to display.";

    var friends = friendController.getFriends(loggedInUser.getUsername());
    if (friends.isEmpty()) return "No friends to display.";

    StringBuilder leaderboard = new StringBuilder("Top Scores:\n");
    friends.sort((a, b) ->
      Integer.compare(b.getHighestScore(), a.getHighestScore())
    );
    for (User friend : friends) {
      leaderboard
        .append(friend.getUsername())
        .append(": ")
        .append(friend.getHighestScore())
        .append("\n");
    }
    return leaderboard.toString();
  }

  // Getter for the logged-in user
  public User getLoggedInUser() {
    return loggedInUser;
  }

  public void showMessage(String message) {
    Alert alert = new Alert(
      Alert.AlertType.INFORMATION,
      message,
      ButtonType.OK
    );
    alert.showAndWait();
  }

  public void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
    alert.showAndWait();
  }

  public String sendPassword() {
    return password;
  }
}
