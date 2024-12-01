package model;

public enum Directions {
    UP(true, true),
    DOWN(false, false),
    LEFT(true, true),
    RIGHT(false, false);

    private final boolean isForward; // TRUE for UP/LEFT, FALSE for DOWN/RIGHT
    private final boolean isRow; // TRUE for horizontal movement (LEFT/RIGHT)

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

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static Directions fromString(String direction) {
        return valueOf(direction.toUpperCase());
    }
}
