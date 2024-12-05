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

        System.out.println("Do you have an account? (y/n)");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("y")) {
            currentUser = signIn(scanner, userController);
        } else {
            currentUser = signUp(scanner, userController);
        }

        System.out.println("Your current score is: " + boardController.getCurrentScore());
        int score = boardController.getCurrentScore();
        if (score > currentUser.getHighestScore()) {
            userController.updateUser(currentUser.getUsername(), currentUser.getPassword(), score);
            System.out.println("New high score saved!");
        }

        System.out.println("Thanks for playing 2048!");
        scanner.close();
    }

    private static User signIn(Scanner scanner, UserController userController) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        User currentUser = userController.getUser(username, password);
        if (currentUser == null) {
            System.out.println("Invalid username or password. Please try again.");
        }
        return currentUser;
    }

    private static User signUp(Scanner scanner, UserController userController) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        boolean success = userController.addUser(username, password, email, name, 0);
        if (!success) {
            System.out.println("Username already exists. Please try again.");
        } else {
            System.out.println("Account created successfully!");
        }
        return userController.getUser(username, password);
    }
}