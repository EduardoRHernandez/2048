package view;

import java.io.File;
import java.util.Scanner;
import controller.BoardController;
import controller.UserController;
import model.User;

public class TextUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File usersFile = new File("users.txt");
        BoardController boardController = new BoardController();
        UserController userController = new UserController(usersFile);

        System.out.println("Welcome to 2048!");
        User currentUser = null;

        // Start the game loop
        boardController.addRandomTile();
        boardController.addRandomTile();
        boolean playing = true;

        while (playing) {
            System.out.println("Current Board:");
            System.out.println(boardController);

            System.out.println("Enter a move (w: up, s: down, a: left, d: right, q: quit): ");
            String move = scanner.nextLine();

            switch (move.toLowerCase()) {
                case "w":
                    boardController.moveUp();
                    break;
                case "s":
                    boardController.moveDown();
                    break;
                case "a":
                    boardController.moveLeft();
                    break;
                case "d":
                    boardController.moveRight();
                    break;
                case "q":
                    playing = false;
                    break;
                default:
                    System.out.println("Invalid move. Please try again.");
            }

            if (boardController.isGameOver()) {
                System.out.println("Game over! Your final score is: " + currentUser.getHighestScore());
                playing = false;
            } else {
                boardController.addRandomTile();
            }
        }

        // Save user's high score if it's a new record
        System.out.print("Enter your score: ");
        int score = Integer.parseInt(scanner.nextLine());
        if (score > currentUser.getHighestScore()) {
            userController.updateUser(currentUser.getUsername(), currentUser.getPassword(), score);
            System.out.println("New high score saved!");
        }

        System.out.println("Thanks for playing 2048!");
        scanner.close();
    }
}
