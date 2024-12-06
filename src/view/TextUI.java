package view;

import java.io.File;
import java.util.Scanner;
import controller.BoardController;
import controller.UserController;
import model.User;

public class TextUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File usersFile = new File("src/files/UserDatabase.csv");
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
                System.out.println("Game over! Your final score is: " + boardController.getCurrentScore());
                playing = false;
            }
        }

        int score = boardController.getCurrentScore();
        System.out.println("Do you have an account? (y/n)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("y")) {
            currentUser = signIn(scanner, userController, score);
        } else if (response.equalsIgnoreCase("n")) {
            currentUser = signUp(scanner, userController, score);
        } else {
            System.out.println("Invalid response. Please try again.");
        }

        System.out.println("Your current score is: " + boardController.getCurrentScore());
        if (currentUser != null) {
            System.out.println("Your highest score is: " + currentUser.getHighestScore());
        }

        System.out.println("Thanks for playing 2048!");
        scanner.close();
    }

    private static User signIn(Scanner scanner, UserController userController, int score) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User currentUser = userController.getUser(username, password);
        if (currentUser == null) {
            System.out.println("Invalid username or password. Please try again.");
        } else {
            currentUser.setHighestScore(score);
            userController.updateUser(username, password, score);
        }
        return currentUser;
    }

    private static User signUp(Scanner scanner, UserController userController, int score) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        boolean success = userController.addUser(username, password, email, name, score);
        if (!success) {
            System.out.println("Username already exists. Please try again.");
        } else {
            System.out.println("Account created successfully!");
        }
        return userController.getUser(username, password);
    }
}