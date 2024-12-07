package model;

public class User {
    private final String username;
    private String hashedPassword;
    private final String email;
    private final String name;
    private int highestScore;

    /**
     * Constructor for a User object.
     * 
     * @param username     the username of the User
     * @param password     the plain text password of the User
     * @param email        the email of the User
     * @param name         the name of the User
     * @param highestScore the highest score of the User
     * 
     * @pre username != null and not empty
     * @pre password != null and not empty
     * @pre email != null and not empty
     * @pre name != null and not empty
     * @post getHighestScore() == highestScore
     */
    public User(String username, String password, String email, String name, int highestScore) {
        this.username = username;
        this.hashedPassword = hashPass(password);
        this.email = email;
        this.name = name;
        this.highestScore = highestScore;
    }

    /**
     * Constructor for a User object.
     * 
     * @param username       the username of the User
     * @param hashedPassword the hashed password of the User
     * @param email          the email of the User
     * @param name           the name of the User
     * @param highestScore   the highest score of the User
     * @param isHashed       whether the provided password is hashed
     * 
     * @pre username != null and not empty
     * @pre hashedPassword != null and not empty
     * @pre email != null and not empty
     * @pre name != null and not empty
     * @pre highestScore >= 0
     * @pre isHashed == true
     * @post getHighestScore() == highestScore
     */
    public User(String username, String hashedPassword, String email, String name, int highestScore, boolean isHashed) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.name = name;
        this.highestScore = highestScore;
    }

    /**
     * Hashes the given password.
     * 
     * @param password the password to be hashed
     * @return the hashed password
     * 
     * @pre password != null and not empty
     * @post the returned string is not null and not empty
     */
    private String hashPass(String password) {
        return Integer.toHexString(password.hashCode());
    }

    /**
     * Retrieves the username of the User.
     * 
     * @return the username of the User
     * @pre the User object is initialized
     * @post the returned string is not null and not empty
     */
    public String getUsername() {
        return username;
    }

    /**
     * Checks if the given password matches the password of the User.
     * 
     * @param password the password to be checked
     * @return true if the given password matches the password of the User, false
     *         otherwise
     * @pre password != null and not empty
     * @post the returned boolean is true if the given password is correct, false
     *       otherwise
     */
    public boolean isPasswordCorrect(String password) {
        return hashedPassword.equals(hashPass(password));
    }

    /**
     * Retrieves the email of the User.
     * 
     * @return the email of the User
     * @pre the User object is initialized
     * @post the returned string is not null and not empty
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the name of the User.
     * 
     * @return the name of the User
     * @pre the User object is initialized
     * @post the returned string is not null and not empty
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the highest score of the User.
     * 
     * @return the highest score of the User
     * @pre the User object is initialized
     * @post the returned int is not negative
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Retrieves the hashed password of the User.
     * 
     * @return the hashed password of the User
     * @pre the User object is initialized
     * @post the returned string is not null and not empty
     */
    public String getPassword() {
        return hashedPassword;
    }

    /**
     * Updates the highest score of the User.
     * 
     * @param highestScore the new highest score
     * @pre highestScore >= 0
     * @post the highest score of the User is the maximum of the given highest
     *       score and the current highest score
     */
    public void setHighestScore(int highestScore) {
        if (highestScore > this.highestScore) {
            this.highestScore = highestScore;
        }
    }

    /**
     * Resets the password of the User.
     * 
     * @param newPassword the new password of the User
     * @pre newPassword != null and not empty
     * @post the password of the User is the hash of the given new password
     */
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
