package view;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import controller.BoardController;
import controller.UserController;
import model.User;

public class TextUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File usersFile = new File("src/files/UserDatabase.csv");
        UserController userController = new UserController(usersFile);
        System.out.println("Welcome to 2048!");

        mainMenu(userController, scanner);

        System.out.println("Thanks for playing 2048!");
        scanner.close();
    }

    private static Entry<User, String> signIn(UserController userController, Scanner scanner) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        User currentUser = userController.getUser(loginUsername, loginPassword);
        if (currentUser == null) {
            System.out.println("Invalid credentials. Please try again.\n");
        } else {
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!\n");
        }
        return new AbstractMap.SimpleEntry(currentUser, loginPassword);
    }

    private static void signUp(Scanner scanner, UserController userController) {
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
    }

    private static void playGame(User currentUser, UserController userController, BoardController boardController, Scanner scanner, String password) {
        if (currentUser == null) {
            System.out.println("Please sign in to play the game.\n");
            return;
        }
        
        boardController.addRandomTile();
        boardController.addRandomTile();
        boolean playing = true;

        while (playing) {
            if (boardController.isGameOver()) {
                System.out.println("\nGame over " + currentUser.getName() + "! Your final score is: " + boardController.getCurrentScore());
                if (currentUser.getName() != "Guest") {
                    if (boardController.getCurrentScore() > currentUser.getHighestScore()) {
                        userController.updateUser(currentUser.getUsername(), password, boardController.getCurrentScore());
                    }
                    currentUser = userController.getUser(currentUser.getUsername(), password);
                    System.out.println("Your highest score is: " + currentUser.getHighestScore() + "\n");
                }
                playing = false;
                return;
            }

            System.out.println("Current Score: " + boardController.getCurrentScore() + "\n");
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
                    System.out.println("\nInvalid move. Please try again.\n");
            }
        }
    }

    private static void mainMenu(UserController userController, Scanner scanner) {
        User currentUser = null;
        String password = "";
        while (true) {
            System.out.println("1. Sign In\n2. Register\n3. Play as Guest\n4. Play 2048\n5. Exit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1": // Sign in
                    Map.Entry<User,String> userInfo = signIn(userController, scanner);
                    currentUser = userInfo.getKey();
                    password = userInfo.getValue();
                    break;
                case "2": // Register
                    signUp(scanner, userController);
                    break;
                case "3": // Guest
                    currentUser = new User("Guest", "", "", "Guest", 0);
                    System.out.println("Playing as Guest. Scores won't be saved.\n");
                    playGame(currentUser, userController, new BoardController(), scanner, "");
                    break;
                case "4": // Play
                    playGame(currentUser, userController, new BoardController(), scanner, password);
                    break;
                case "5": // Exit
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
    }
}