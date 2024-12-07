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

        // Main menu loop
        while (currentUser == null) {
            System.out.println("1. Sign In\n2. Register\n3. Play as Guest\n4. Exit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": // Sign in
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    currentUser = userController.getUser(loginUsername, loginPassword);
                    if (currentUser == null) {
                        System.out.println("Invalid credentials. Please try again.\n");
                    } else {
                        System.out.println("Login successful. Welcome, " + currentUser.getName() + "!\n");
                    }
                    break;
                case "2": // Register
                    System.out.print("Enter username: ");
                    String registerUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String registerPassword = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    userController.addUser(registerUsername, registerPassword, email, name, 0);
                    System.out.println("\nRegistration successful! You can now log in.\n");
                    break;
                case "3": // Play as guest
                    currentUser = new User("Guest", "", "", "Guest", 0);
                    System.out.println("\nPlaying as Guest. Scores won't be saved.\n");
                    break;
                case "4": // Exit
                    System.out.println("\nGoodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nInvalid option. Please try again.\n");
            }
        }

        // Start the game loop
        boardController.addRandomTile();
        boardController.addRandomTile();
        boolean playing = true;

        while (playing) {
            System.out.println("\n\nCurrent Score: " + boardController.getCurrentScore() + "\n");
            System.out.println("Current Board:");
            System.out.println(boardController);

            System.out.println("Enter a move (w: up, s: down, a: left, d: right, q: quit): ");
            String move = scanner.nextLine();

            boolean validMove = false;
            switch (move.toLowerCase()) {
                case "w":
                    validMove = boardController.moveUp();
                    break;
                case "s":
                    validMove = boardController.moveDown();
                    break;
                case "a":
                    validMove = boardController.moveLeft();
                    break;
                case "d":
                    validMove = boardController.moveRight();
                    break;
                case "q":
                    playing = false;
                    break;
                default:
                    System.out.println("\nInvalid move. Please try again.\n");
            }

            if (validMove) {
                boardController.addRandomTile();
            } else if (!move.equalsIgnoreCase("q")) {
                System.out.println("\nMove was not valid. Try a different direction.\n");
            }

            if (boardController.isGameOver()) {
                System.out.println("\nGame over " + currentUser.getName() + "! Your final score is: " + boardController.getCurrentScore());
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