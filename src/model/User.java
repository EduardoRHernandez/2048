package model;

public class User {
    private final int id;
    private final String username;
    private final String hashedPassword;
    private final String email;
    private final String name;
    private int highestScore;

    public User(int id, String username, String password, String email, String name, int highestScore) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashPass(password);
        this.email = email;
        this.name = name;
        this.highestScore = highestScore;
    }

    private String hashPass(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPasswordCorrect(String password) {
        return hashedPassword.equals(hashPass(password));
    }

    public String getPassword() {
        return hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        if (highestScore > this.highestScore) {
            this.highestScore = highestScore;
        }
    }
}
