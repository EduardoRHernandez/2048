package model;

public enum Directions {
    UP(true, false), // Forward (negative index) movement on columns
    DOWN(false, false), // Backward (positive index) movement on columns
    LEFT(true, true), // Forward (negative index) movement on rows
    RIGHT(false, true); // Backward (positive index) movement on rows

    private final boolean isForward; // TRUE for UP/LEFT, FALSE for DOWN/RIGHT
    private final boolean isRow; // TRUE for horizontal movement (LEFT/RIGHT)
    public static final boolean ALWAYS_VALID = true;

    Directions(boolean isForward, boolean isRow) {
        this.isForward = isForward;
        this.isRow = isRow;
    }

    public boolean isForward() {
        return isForward;
    }

    public boolean isRow() {
        return isRow;
    }

    public boolean isValid() {
        return ALWAYS_VALID;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static Directions fromString(String direction) {
        return valueOf(direction.toUpperCase());
    }
}
