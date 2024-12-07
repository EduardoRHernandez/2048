package model;

public class Tile {
    private int value;
    private boolean merged;

    /**
     * Default constructor for a Tile object.
     * 
     * @pre none
     * @post value == 0 and merged == false
     */
    public Tile() {
        this.value = 0;
        this.merged = false;
    }

    /**
     * Copy constructor for a Tile object.
     * 
     * @param tile the Tile object to be copied
     * @pre tile != null
     * @post this.value == tile.value and this.merged == tile.merged
     */
    public Tile(Tile tile) {
        this.value = tile.value;
        this.merged = tile.merged;
    }

    /**
     * Retrieves the value of the Tile.
     * 
     * @return the value of the Tile
     * @pre none
     * @post the returned value is the value of the Tile
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the Tile.
     * 
     * @param value the value to be set
     * @pre value >= 0
     * @post this.value == value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Retrieves the merged state of the Tile.
     * 
     * @return the merged state of the Tile
     * @pre none
     * @post the returned boolean is the merged state of the Tile
     */
    public boolean isMerged() {
        return merged;
    }

    /**
     * Sets the merged state of the Tile.
     * 
     * @param merged the merged state to be set
     * @pre none
     * @post this.merged == merged
     */
    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}
