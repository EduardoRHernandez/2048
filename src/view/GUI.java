package view;

import controller.BoardController;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GUI extends Application {

  private BoardController controller;
  private GridPane grid;
  private Label scoreLabel;
  private Label bestScoreLabel;
  private int score;
  private int bestScore;

  @Override
  public void start(Stage primaryStage) {
    // Initialize the game controller
    controller = new BoardController();
    controller.addRandomTile();
    controller.addRandomTile();

    // Initialize scores
    score = controller.getCurrentScore();
    bestScore = 0;

    // Set up the score label
    scoreLabel = new Label("Score: " + score);
    scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    scoreLabel.setStyle("-fx-text-fill: #f9f6f2;");

    // Set up the best score label
    bestScoreLabel = new Label("Best: " + bestScore);
    bestScoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    bestScoreLabel.setStyle("-fx-text-fill: #f9f6f2;");

    // Wrap score labels in StackPanes for background styling
    StackPane scoreBox = new StackPane();
    scoreBox.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    scoreBox.getChildren().add(scoreLabel);
    scoreBox.setPrefSize(150, 50); // Fix the size of the background box

    StackPane bestScoreBox = new StackPane();
    bestScoreBox.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    bestScoreBox.getChildren().add(bestScoreLabel);
    bestScoreBox.setPrefSize(150, 50); // Fix the size of the background box

    // Set up the "New Game" button
    Button newGameButton = new Button("New Game");
    newGameButton.setStyle(
      "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-padding: 10 20;"
    );
    newGameButton.setOnAction(e -> restartGame());

    // Arrange score, best score, and button in a horizontal box
    HBox scoreBoxContainer = new HBox(
      20,
      scoreBox,
      bestScoreBox,
      newGameButton
    );
    scoreBoxContainer.setAlignment(Pos.CENTER);

    // Set up the game grid
    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPrefSize(400, 400); // Fix the size of the grid
    grid.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 20; -fx-border-radius: 10px;"
    );
    grid.setAlignment(Pos.CENTER); // Center-align the grid in its container

    // Display the initial board
    updateBoard();

    // Combine scoreBoxContainer and grid into a VBox layout
    VBox root = new VBox(20, scoreBoxContainer, grid);
    root.setStyle("-fx-background-color: #faf8ef; -fx-padding: 20;");
    root.setAlignment(Pos.TOP_CENTER);

    // Prevent grid stretching when fullscreen
    VBox.setVgrow(grid, null);

    // Create the scene
    Scene scene = new Scene(root, 500, 600);
    root.requestFocus(); // Ensure the root has focus for key handling

    // Add key press event handler
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP:
          controller.moveUp();
          break;
        case DOWN:
          controller.moveDown();
          break;
        case LEFT:
          controller.moveLeft();
          break;
        case RIGHT:
          controller.moveRight();
          break;
        default:
          return; // Ignore other keys
      }

      // Update the game state
      controller.addRandomTile();
      updateScore();
      updateBoard();

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
        backgroundColor = "#cdc1b4";
    }

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

  public static void main(String[] args) {
    launch(args);
  }
}
