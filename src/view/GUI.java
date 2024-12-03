package view;

import controller.BoardController;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

  private BoardController controller;
  private GridPane grid;
  private Label scoreLabel;
  private int score;

  @Override
  public void start(Stage primaryStage) {
    // Initialize the game controller
    controller = new BoardController();
    controller.addRandomTile(); // Add the first random tile
    controller.addRandomTile(); // Add the second random tile

    // Initialize the score using controller
    score = controller.getCurrentScore();

    // Set up the score label
    scoreLabel = new Label("Score: " + score);
    scoreLabel.setStyle(
      "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f9f6f2;"
    );

    // Set up the game grid
    grid = new GridPane();
    grid.setHgap(10); // Horizontal gap between tiles
    grid.setVgap(10); // Vertical gap between tiles
    grid.setStyle("-fx-background-color: #bbada0; -fx-padding: 10;");

    // Display the initial board
    updateBoard();

    // Combine the score label and game grid into a VBox layout
    VBox root = new VBox(10, scoreLabel, grid);
    root.setStyle("-fx-background-color: #faf8ef; -fx-padding: 20;");
    root.setAlignment(Pos.TOP_CENTER); // Align everything at the top center

    // Create the scene and add a key press event handler
    Scene scene = new Scene(root, 500, 600);
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP:
          controller.moveUp();
          updateScore(); // Update the score after moving
          break;
        case DOWN:
          controller.moveDown();
          updateScore();
          break;
        case LEFT:
          controller.moveLeft();
          updateScore();
          break;
        case RIGHT:
          controller.moveRight();
          updateScore();
          break;
        default:
          return; // Ignore non-arrow keys
      }

      controller.addRandomTile(); // Add a random tile after each move
      animateTiles(); // Animate the tile movement
      updateBoard(); // Refresh the UI

      // Check if the game is over
      if (controller.isGameOver()) {
        showGameOver();
      }
    });

    // Set up the primary stage
    primaryStage.setTitle("2048 Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void updateBoard() {
    grid.getChildren().clear(); // Clear the grid for updates
    var boardValues = controller.getBoard(); // Fetch board values

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        int value = boardValues.get(i * 4 + j);

        // Create a label to represent each tile
        Label tile = new Label(value == 0 ? "" : String.valueOf(value));
        tile.setMinSize(100, 100); // Set tile size
        tile.setAlignment(Pos.CENTER); // Center the text
        tile.setStyle(getTileStyle(value)); // Style the tile based on its value

        grid.add(tile, j, i); // Add the tile to the grid at (column, row)
      }
    }
  }

  private String getTileStyle(int value) {
    String backgroundColor;
    switch (value) {
      case 2:
        backgroundColor = "#eee4da";
        break;
      case 4:
        backgroundColor = "#ede0c8";
        break;
      case 8:
        backgroundColor = "#f2b179";
        break;
      case 16:
        backgroundColor = "#f59563";
        break;
      case 32:
        backgroundColor = "#f67c5f";
        break;
      case 64:
        backgroundColor = "#f65e3b";
        break;
      case 128:
        backgroundColor = "#edcf72";
        break;
      case 256:
        backgroundColor = "#edcc61";
        break;
      case 512:
        backgroundColor = "#edc850";
        break;
      case 1024:
        backgroundColor = "#edc53f";
        break;
      case 2048:
        backgroundColor = "#edc22e";
        break;
      default:
        backgroundColor = "#cdc1b4"; // Default for empty or unknown tiles
    }

    String fontColor = (value == 2 || value == 4) ? "#776e65" : "#f9f6f2"; // Text color
    return String.format(
      "-fx-background-color: %s; -fx-border-color: #bbada0; -fx-border-width: 2px; " +
      "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: %s; -fx-alignment: center;",
      backgroundColor,
      fontColor
    );
  }

  private void updateScore() {
    score += 10; // Increment score for demonstration; adjust based on your logic
    scoreLabel.setText("Score: " + score);
  }

  private void animateTiles() {
    for (var child : grid.getChildren()) {
      if (child instanceof Label) {
        Label tile = (Label) child;

        // Animate each tile with TranslateTransition
        TranslateTransition transition = new TranslateTransition(
          Duration.millis(200),
          tile
        );
        transition.setByX(0); // Adjust X movement if needed
        transition.setByY(0); // Adjust Y movement if needed
        transition.play();
      }
    }
  }

  private void showGameOver() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("Game Over! Final Score: " + score);
    alert.setContentText("Would you like to start a new game?");

    // Add "Yes" and "No" options
    alert
      .getButtonTypes()
      .setAll(
        javafx.scene.control.ButtonType.YES,
        javafx.scene.control.ButtonType.NO
      );

    var result = alert.showAndWait();
    if (
      result.isPresent() && result.get() == javafx.scene.control.ButtonType.YES
    ) {
      restartGame(); // Restart the game
    } else {
      System.exit(0); // Exit the program
    }
  }

  private void restartGame() {
    controller = new BoardController(); // Reset the board controller
    score = 0; // Reset the score
    scoreLabel.setText("Score: " + score); // Update the score label
    controller.addRandomTile(); // Add initial tiles
    controller.addRandomTile();
    updateBoard(); // Refresh the board
  }

  public static void main(String[] args) {
    launch(args);
  }
}
