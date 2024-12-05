package model;

public enum Directions {
    UP(true, false), // Forward (negative index) movement on columns
    DOWN(false, false), // Backward (positive index) movement on columns
    LEFT(true, true), // Forward (negative index) movement on rows
    RIGHT(false, true); // Backward (positive index) movement on rows

    private final boolean isForward; // TRUE for UP/LEFT, FALSE for DOWN/RIGHT
    private final boolean isRow; // TRUE for horizontal movement (LEFT/RIGHT)
    public static final boolean ALWAYS_VALID = true;

    /**
     * Constructs a direction with specified forward and row values.
     *
     * @param isForward TRUE for forward movement (UP/LEFT), FALSE for backward
     *                  (DOWN/RIGHT)
     * @param isRow     TRUE for row movement (LEFT/RIGHT), FALSE for column
     *                  (UP/DOWN)
     * @pre isForward and isRow must be boolean values
     * @post The direction is initialized with the specified forward and row
     *       attributes
     */
    Directions(boolean isForward, boolean isRow) {
        this.isForward = isForward;
        this.isRow = isRow;
    }

    /**
     * Checks if the direction is forward.
     *
     * @return TRUE if the direction is forward (UP/LEFT), FALSE for backward
     *         (DOWN/RIGHT)
     * @pre The direction is one of the four directions
     * @post The result is a boolean indicating if the direction is forward
     */
    public boolean isForward() {
        return isForward;
    }

    /**
     * Checks if the direction is for row movement.
     *
     * @return TRUE if the direction is for row movement (LEFT/RIGHT), FALSE for
     *         column (UP/DOWN)
     * @pre The direction is one of the four directions
     * @post The result is a boolean indicating if the direction is for row movement
     */
    public boolean isRow() {
        return isRow;
    }

    /**
     * Checks if the direction is valid.
     *
     * @return TRUE if the direction is valid, FALSE if not
     * @pre The direction is one of the four directions
     * @post The result is a boolean indicating if the direction is valid
     */
    public boolean isValid() {
        return ALWAYS_VALID;
    }

    /**
     * Converts a string representation of a direction to a Directions enum.
     *
     * @param direction the string representation of the direction
     * @return the corresponding Directions enum
     * @pre direction is a non-null string representing a valid direction
     * @post returns the Directions enum corresponding to the given string
     */
    public static Directions fromString(String direction) {
        return valueOf(direction.toUpperCase());
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
