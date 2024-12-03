package model;

public class User {
    private final String username;
    private String hashedPassword;
    private final String email;
    private final String name;
    private int highestScore;

    public User(String username, String password, String email, String name, int highestScore) {
        this.username = username;
        this.hashedPassword = hashPass(password);
        this.email = email;
        this.name = name;
        this.highestScore = highestScore;
    }

    public User(String username, String hashedPassword, String email, String name, int highestScore, boolean isHashed) {
        if (!isHashed) {
            throw new IllegalArgumentException("Use the plain password constructor for non-hashed passwords.");
        }
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.name = name;
        this.highestScore = highestScore;
    }

    private String hashPass(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public String getUsername() {
        return username;
    }

    public boolean isPasswordCorrect(String password) {
        return hashedPassword.equals(hashPass(password));
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

    public void resetPassword(String newPassword) {
        this.hashedPassword = hashPass(newPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", highestScore=" + highestScore +
                '}';
    }
}
