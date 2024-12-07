package view;

import controller.BoardController;
import controller.FriendDatabaseController;
import controller.UserController;
import java.io.File;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

public class GUI extends Application {

  private BoardController controller;
  private User loggedInUser;
  private GridPane grid;
  private Label scoreLabel;
  private Label bestScoreLabel;
  VBox leaderboardBox;
  private int score;
  private int bestScore;
  private themeGUI themeManager;
  private userAuthLeaderboard authHelper;

  @Override
  public void start(Stage primaryStage) {
    // Initialize file paths and controllers
    File userFile = new File("src/files/UserDatabase.csv");
    if (!userFile.exists()) {
      System.err.println("Error: UserDatabase.csv file not found.");
      return;
    }

    // Controllers
    FriendDatabaseController friendController = new FriendDatabaseController(
      new UserController(userFile)
    );
    authHelper = new userAuthLeaderboard(friendController);

    // Authenticate user
    if (!authHelper.authenticate(0)) {
      System.out.println("Authentication failed or canceled. Exiting...");
      return;
    }

    // Fetch the logged-in user
    loggedInUser = authHelper.getLoggedInUser();

    // Initialize game controller and theme manager
    controller = new BoardController();
    themeManager = new themeGUI();

    primaryStage.setOnCloseRequest(event -> {
      saveUserScore();
      Platform.exit(); // Ensure the platform exits after saving
    });

    // Set up the game grid
    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPrefSize(400, 400);
    grid.setAlignment(Pos.CENTER);

    // Add two random tiles at the start
    controller.addRandomTile();
    controller.addRandomTile();
    updateBoard();

    // Initialize scores
    score = controller.getCurrentScore();
    bestScore = 0;

    // Score label with styled box
    StackPane scoreBox = new StackPane();
    scoreBox.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    scoreLabel = new Label("Score: " + score);
    scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    scoreLabel.setStyle("-fx-text-fill: #f9f6f2;");
    scoreBox.getChildren().add(scoreLabel);

    // Best score label with styled box
    StackPane bestScoreBox = new StackPane();
    bestScoreBox.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    bestScoreLabel = new Label("Best: " + bestScore);
    bestScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    bestScoreLabel.setStyle("-fx-text-fill: #f9f6f2;");
    bestScoreBox.getChildren().add(bestScoreLabel);

    // "New Game" button
    Button newGameButton = new Button("New Game");
    newGameButton.setStyle(
      "-fx-font-size: 16px; -fx-background-color: #8f7a66; -fx-text-fill: white;"
    );
    newGameButton.setFocusTraversable(false);
    newGameButton.setOnAction(e -> {
      restartGame();
      resetFocusToGrid(); // Reset focus to the grid
    });

    // "Add Friend" button
    Button addFriendButton = new Button("Add Friend");
    addFriendButton.setStyle(
      "-fx-font-size: 16px; -fx-background-color: #8f7a66; -fx-text-fill: white;"
    );
    addFriendButton.setFocusTraversable(false);
    VBox leaderboardContainer = createLeaderboardPanel(friendController);
    addFriendButton.setOnAction(event -> {
      // Handle adding a friend
      handleAddFriend(authHelper, friendController);

      // Refresh the leaderboard
      updateLeaderboard(friendController, leaderboardContainer);
    });

    // Theme selector dropdown
    ComboBox<String> themeSelector = new ComboBox<>();
    themeSelector.getItems().addAll(themeManager.getThemes().keySet());
    themeSelector.setValue("Classic"); // Default theme
    themeSelector.setFocusTraversable(false);
    themeSelector.setOnAction(event -> {
      themeManager.setTheme(themeSelector.getValue());
      applyTheme(); // Apply the selected theme
      grid.requestFocus(); // Reset focus after changing the theme
    });

    // Arrange score, best score, buttons, and theme selector
    HBox scoreBoxContainer = new HBox(
      20,
      themeSelector,
      scoreBox,
      bestScoreBox,
      newGameButton,
      addFriendButton
    );
    scoreBoxContainer.setAlignment(Pos.CENTER);

    // Game grid wrapper
    StackPane gridWrapper = new StackPane();
    gridWrapper.setStyle(
      "-fx-background-color: #bbada0; -fx-border-radius: 10px;"
    );
    gridWrapper.setMaxSize(440, 440);
    gridWrapper.setPadding(new Insets(10));
    gridWrapper.getChildren().add(grid);

    VBox scoreAndGrid = new VBox(20, scoreBoxContainer, gridWrapper);
    scoreAndGrid.setAlignment(Pos.CENTER);

    // Leaderboard panel
    leaderboardBox = createLeaderboardPanel(friendController);

    // Combine components into the root layout
    HBox root = new HBox(20, scoreAndGrid, leaderboardBox);
    root.setStyle("-fx-background-color: #faf8ef; -fx-padding: 20;");
    root.setAlignment(Pos.TOP_CENTER);

    // Key press event handler for tile movement
    Scene scene = new Scene(root, 900, 750);
    scene.setOnKeyPressed(event -> {
      boolean validMove = false;

      switch (event.getCode()) {
        case UP -> validMove = controller.moveUp();
        case DOWN -> validMove = controller.moveDown();
        case LEFT -> validMove = controller.moveLeft();
        case RIGHT -> validMove = controller.moveRight();
        default -> {
          return; // Ignore other keys
        }
      }

      if (validMove) {
        updateScore();
        updateBoard();

        if (controller.isGameOver()) {
          showGameOver();
        }
      }
    });

    // Set up stage
    primaryStage.setTitle("2048 Game");
    primaryStage.setScene(scene);
    primaryStage.show();

    root.requestFocus(); // Ensure focus starts on the grid
  }

  private void updateLeaderboard(
    FriendDatabaseController friendController,
    VBox leaderboardContainer
  ) {
    leaderboardContainer.getChildren().clear(); // Clear the existing leaderboard

    Label header = new Label("Leaderboard");
    header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    header.setStyle("-fx-text-fill: #f9f6f2;");
    leaderboardContainer.getChildren().add(header);

    List<User> friends = friendController.getFriends(
      loggedInUser.getUsername()
    );
    if (friends.isEmpty()) {
      Label noFriendsLabel = new Label("No friends or scores to display.");
      noFriendsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
      noFriendsLabel.setStyle("-fx-text-fill: #f9f6f2;");
      leaderboardContainer.getChildren().add(noFriendsLabel);
    } else {
      for (User friend : friends) {
        Label friendLabel = new Label(
          friend.getUsername() + ": " + friend.getHighestScore()
        );
        friendLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        friendLabel.setStyle("-fx-text-fill: #f9f6f2;");
        leaderboardContainer.getChildren().add(friendLabel);
      }
    }

    // Dynamically adjust the height of the leaderboard based on the number of items
    int itemCount = leaderboardContainer.getChildren().size(); // Count children (header + items)
    leaderboardContainer.setPrefHeight(itemCount * 30); // Adjust height dynamically (30px per item)
  }

  /*************  ✨ Codeium Command ⭐  *************/
  /**
   * Applies the current theme to the game UI.
   *
   * Calls the applyTheme method from themeGUI to set the theme for the root and grid
   * nodes, and then calls updateBoard to ensure the board reflects the updated theme.
   */
  /******  86c98ed6-36ba-4bdb-81c3-1201a00955b3  *******/
  private void applyTheme() {
    // Call applyTheme from themeGUI
    themeManager.applyTheme(grid.getScene().getRoot(), grid);
    updateBoard(); // Ensure the board reflects the updated theme
  }

  private VBox createLeaderboardPanel(
    FriendDatabaseController friendController
  ) {
    Label leaderboardLabel = new Label("Leaderboards");
    leaderboardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    leaderboardLabel.setStyle("-fx-text-fill: #f9f6f2;");

    VBox leaderboardContainer = new VBox(10, leaderboardLabel);
    leaderboardContainer.setPadding(new Insets(10));
    leaderboardContainer.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    leaderboardContainer.setAlignment(Pos.TOP_LEFT);

    // Fetch the logged-in user's friends
    List<User> friends = friendController.getFriends(
      loggedInUser.getUsername()
    );

    if (friends.isEmpty()) {
      Label noFriendsLabel = new Label("No friends or scores to display.");
      noFriendsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
      noFriendsLabel.setStyle("-fx-text-fill: #f9f6f2;");
      leaderboardContainer.getChildren().add(noFriendsLabel);
    } else {
      for (User friend : friends) {
        Label friendLabel = new Label(
          friend.getUsername() + ": " + friend.getHighestScore()
        );
        friendLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        friendLabel.setStyle("-fx-text-fill: #f9f6f2;");
        leaderboardContainer.getChildren().add(friendLabel);
      }
    }

    return leaderboardContainer;
  }

  private void handleAddFriend(
    userAuthLeaderboard authHelper,
    FriendDatabaseController friendController
  ) {
    // Create a dialog for adding a friend
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Add Friend");
    dialog.setHeaderText("Enter the username of the friend you want to add:");
    dialog.setContentText("Friend's Username:");

    // Show the dialog and get the user input
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent() && !result.get().trim().isEmpty()) {
      String friendUsername = result.get().trim();

      // Check if the user is already a friend
      List<User> currentFriends = friendController.getFriends(
        loggedInUser.getUsername()
      );
      boolean alreadyFriend = currentFriends
        .stream()
        .anyMatch(friend ->
          friend.getUsername().equalsIgnoreCase(friendUsername)
        );

      if (alreadyFriend) {
        authHelper.showError("This user is already your friend.");
        return;
      }

      // Attempt to add the friend
      boolean success = friendController.addFriend(
        loggedInUser.getUsername(),
        friendUsername
      );
      if (success) {
        authHelper.showMessage("Friend added successfully!");
        updateLeaderboard(friendController, leaderboardBox); // Update the leaderboard
      } else {
        authHelper.showError("Failed to add friend. Username may not exist.");
      }
    } else {
      authHelper.showError("No username entered. Please try again.");
    }
  }

  private void resetFocusToGrid() {
    Platform.runLater(() -> grid.requestFocus());
  }

  private void updateBoard() {
    grid.getChildren().clear();
    var boardValues = controller.getBoard();

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        int value = boardValues.get(i * 4 + j);

        // Create a label for each tile
        Label tile = new Label(value == 0 ? "" : String.valueOf(value));
        tile.setMinSize(100, 100);
        tile.setAlignment(Pos.CENTER);
        tile.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        tile.setStyle(getTileStyle(value));

        grid.add(tile, j, i);
        GridPane.setHalignment(tile, HPos.CENTER);
      }
    }
  }

  private String getTileStyle(int value) {
    String backgroundColor = themeManager.getCurrentTheme().getTileColor(value);
    String fontColor = (value == 2 || value == 4) ? "#776e65" : "#f9f6f2";
    return String.format(
      "-fx-background-color: %s; -fx-border-radius: 5px; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: %s;",
      backgroundColor,
      fontColor
    );
  }

  private void updateScore() {
    score = controller.getCurrentScore();
    if (score > bestScore) {
      bestScore = score;
    }
    scoreLabel.setText("Score: " + score);
    bestScoreLabel.setText("Best: " + bestScore);
  }

  private void showGameOver() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("Game Over! Final Score: " + score);
    alert.setContentText("Click OK to start a new game.");
    alert.showAndWait();
    restartGame();
  }

  private void restartGame() {
    controller = new BoardController(); // Reset the controller
    score = 0; // Reset the score
    updateScore(); // Update the score display
    controller.addRandomTile(); // Add two new random tiles
    controller.addRandomTile();
    updateBoard(); // Refresh the board
    grid.getScene().getRoot().requestFocus(); // Ensure the root has focus for key handling
  }

  private void saveUserScore() {
    if (loggedInUser != null && controller != null) {
      int finalScore = controller.getCurrentScore();

      // Update the highest score if the final score is greater
      if (finalScore > loggedInUser.getHighestScore()) {
        loggedInUser.setHighestScore(finalScore);

        // Use the UserController to update the database
        UserController userController = new UserController(
          new File("src/files/UserDatabase.csv")
        );
        userController.updateUser(
          loggedInUser.getUsername(),
          authHelper.sendPassword(),
          finalScore
        );
        System.out.println("Score saved successfully!");
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
