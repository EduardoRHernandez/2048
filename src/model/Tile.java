package model;

public class Tile {
    private int value;
    private boolean merged;

    public Tile() {
        this.value = 0;
        this.merged = false;
    }

    public Tile(Tile tile) {
        this.value = tile.value;
        this.merged = tile.merged;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }    

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}
