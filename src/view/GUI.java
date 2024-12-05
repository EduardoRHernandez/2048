package view;

import controller.BoardController;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
  private themeGUI themeManager; // Theme manager to handle themes

  @Override
  public void start(Stage primaryStage) {
    // Initialize the game controller
    controller = new BoardController();
    themeManager = new themeGUI(); // Initialize the theme manager

    // Set up the game grid
    grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPrefSize(400, 400);
    grid.setAlignment(Pos.CENTER);

    // Add two random tiles at the start
    controller.addRandomTile();
    controller.addRandomTile();

    // Display the initial board
    updateBoard();

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
    scoreBox.setPrefSize(150, 50);

    StackPane bestScoreBox = new StackPane();
    bestScoreBox.setStyle(
      "-fx-background-color: #bbada0; -fx-padding: 10; -fx-border-radius: 5px;"
    );
    bestScoreBox.getChildren().add(bestScoreLabel);
    bestScoreBox.setPrefSize(150, 50);

    // Set up the "New Game" button
    Button newGameButton = new Button("New Game");
    newGameButton.setStyle(
      "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-padding: 10 20;"
    );
    newGameButton.setOnAction(e -> restartGame());

    ComboBox<String> themeSelector = new ComboBox<>();
    themeSelector.getItems().addAll(themeManager.getThemes().keySet());
    themeSelector.setValue("Classic"); // Default theme
    themeSelector.setFocusTraversable(false); // Prevent keyboard focus
    themeSelector.setOnAction(event -> {
      themeManager.setTheme(themeSelector.getValue());
      applyTheme();
    });

    // Arrange score, best score, button, and theme selector in a horizontal box
    HBox scoreBoxContainer = new HBox(
      20,
      themeSelector,
      scoreBox,
      bestScoreBox,
      newGameButton
    );
    scoreBoxContainer.setAlignment(Pos.CENTER);

    // Create a StackPane to wrap the grid and control the background
    StackPane gridWrapper = new StackPane();
    gridWrapper.setStyle(
      "-fx-background-color: #bbada0; -fx-border-radius: 10px;"
    );
    gridWrapper.setMaxSize(440, 440); // Slightly larger than the grid to add padding
    gridWrapper.setPadding(new Insets(10)); // Add padding to spread the background
    gridWrapper.getChildren().add(grid); // Add the grid to the StackPane

    // Combine scoreBoxContainer and gridWrapper into a VBox layout
    VBox root = new VBox(20, scoreBoxContainer, gridWrapper);
    root.setStyle("-fx-background-color: #faf8ef; -fx-padding: 20;");
    root.setAlignment(Pos.TOP_CENTER);

    // Create the scene
    Scene scene = new Scene(root, 700, 750);

    // Assign the scene to the stage
    primaryStage.setScene(scene);

    // Apply the theme after the scene is set
    applyTheme();

    // Add key press event handler
    scene.setOnKeyPressed(event -> {
      boolean validMove = false;

      switch (event.getCode()) {
        case UP:
          if (
            !event.isShiftDown() && !event.isControlDown() && !event.isAltDown()
          ) {
            validMove = controller.moveUp();
          }
          break;
        case DOWN:
          if (
            !event.isShiftDown() && !event.isControlDown() && !event.isAltDown()
          ) {
            validMove = controller.moveDown();
          }
          break;
        case LEFT:
          if (
            !event.isShiftDown() && !event.isControlDown() && !event.isAltDown()
          ) {
            validMove = controller.moveLeft();
          }
          break;
        case RIGHT:
          if (
            !event.isShiftDown() && !event.isControlDown() && !event.isAltDown()
          ) {
            validMove = controller.moveRight();
          }
          break;
        default:
          return; // Ignore other keys
      }

      if (validMove) {
        controller.addRandomTile();
        updateScore();
        updateBoard();

        if (controller.isGameOver()) {
          showGameOver();
        }
      }
    });

    // Set up the primary stage
    primaryStage.setTitle("2048 Game");
    primaryStage.setScene(scene);
    primaryStage.show();

    root.requestFocus();
  }

  private void applyTheme() {
    // Call applyTheme from themeGUI
    themeManager.applyTheme(grid.getScene().getRoot(), grid);
    updateBoard(); // Ensure the board reflects the updated theme
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

  public static void main(String[] args) {
    launch(args);
  }
}
