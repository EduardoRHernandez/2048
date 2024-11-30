package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {
    private ArrayList<Tile> aBoard;
    private static final int BOARD_SIZE = 4;
    private Random random;

    public Board() {
        this.random = new Random();
        aBoard = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                aBoard.add(new Tile());
            }
        }
    }

    public Board(Random random) {
        this.random = random;
        initializeBoard();
    }

    private void initializeBoard() {
        aBoard = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                aBoard.add(new Tile());
            }
        }
    }

    public List<Integer> getBoardValues() {
        List<Integer> boardValues = new ArrayList<>();
        for (Tile tile : aBoard) {
            boardValues.add(tile.getValue());
        }
        return Collections.unmodifiableList(boardValues);
    }

    public Tile getTile(int x, int y) {
        return aBoard.get(x * BOARD_SIZE + y);
    }

    public void setTile(int x, int y, Tile tile) {
        aBoard.set(x * BOARD_SIZE + y, tile);
    }

    public void resetMergedState() {
        for (Tile tile : aBoard) {
            tile.setMerged(false);
        }
    }

    public List<Tile> getRow(int rowIndex) {
        return aBoard.subList(rowIndex * BOARD_SIZE, (rowIndex + 1) * BOARD_SIZE);
    }

    public List<Tile> getColumn(int colIndex) {
        List<Tile> column = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            column.add(aBoard.get(i * BOARD_SIZE + colIndex));
        }
        return column;
    }

    public void moveLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, true, true);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            merge(i, true, true);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, true, true);
        }

        resetMergedState();
    }

    public void moveRight() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, true, false);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            merge(i, true, false);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, true, false);
        }

        resetMergedState();
    }

    public void moveUp() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, false, true);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            merge(i, false, true);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, false, true);
        }

        resetMergedState();
    }

    public void moveDown() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, false, false);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            merge(i, false, false);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            slide(i, false, false);
        }

        resetMergedState();
    }

    // Merge tiles in any direction (left/right/up/down) on a row or column.
    private void merge(int start, boolean isRow, boolean isLeftOrUp) {
        int increment = isLeftOrUp ? 1 : -1;
        int startIdx = isLeftOrUp ? 0 : BOARD_SIZE - 2;
        int endIdx = isLeftOrUp ? BOARD_SIZE - 1 : -1;

        for (int i = startIdx; i != endIdx; i += increment) {
            Tile currentTile = getTileByIndex(start, i, isRow);
            Tile nextTile = getTileByIndex(start, i + increment, isRow);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
            }
        }
    }

    // Extracted helper method to calculate tile indices
    private Tile getTileByIndex(int start, int index, boolean isRow) {
        return isRow ? getTile(start, index) : getTile(index, start);
    }

    // Extracted helper method to check merge conditions
    private boolean shouldMerge(Tile currentTile, Tile nextTile) {
        return currentTile.getValue() == nextTile.getValue()
                && !currentTile.isMerged()
                && !nextTile.isMerged();
    }

    // Extracted helper method to perform the merge
    private void performMerge(Tile currentTile, Tile nextTile) {
        currentTile.setValue(currentTile.getValue() * 2);
        currentTile.setMerged(true);
        nextTile.setValue(0);
    }

    private void slide(int start, boolean isRow, boolean isLeftOrUp) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = isLeftOrUp ? 0 : BOARD_SIZE - 1;

        // Extracted method to calculate index
        for (int i = 0; i < BOARD_SIZE; i++) {
            int idx = calculateIndex(start, i, isRow);
            Tile tile = getTile(idx / BOARD_SIZE, idx % BOARD_SIZE);

            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex = updateNewIndex(newIndex, isLeftOrUp);
            }
        }

        // Extracted method to update board with new values
        updateBoardWithNewValues(newline, start, isRow);
    }

    // Helper method to calculate index
    private int calculateIndex(int start, int i, boolean isRow) {
        return isRow ? start * BOARD_SIZE + i : i * BOARD_SIZE + start;
    }

    // Helper method to update new index
    private int updateNewIndex(int newIndex, boolean isLeftOrUp) {
        return isLeftOrUp ? newIndex + 1 : newIndex - 1;
    }

    // Helper method to update board with new values
    private void updateBoardWithNewValues(int[] newline, int start, boolean isRow) {
        for (int i = 0; i < newline.length; i++) {
            if (newline[i] != 0) {
                Tile tile = getTile(isRow ? start : i, isRow ? i : start);
                tile.setValue(newline[i]);
            }
        }
    }

    public void addRandomTile() {
        ArrayList<Integer> emptyTileIndices = new ArrayList<>();

        // Find all empty tiles
        for (int i = 0; i < aBoard.size(); i++) {
            if (aBoard.get(i).getValue() == 0) {
                emptyTileIndices.add(i);
            }
        }

        // If there are no empty tiles, do nothing
        if (emptyTileIndices.isEmpty()) {
            return;
        }

        // Choose a random index from the list of empty tiles
        int randomIndex = emptyTileIndices.get(random.nextInt(emptyTileIndices.size()));

        // Set the random tile to 2 (90% chance) or 4 (10% chance)
        int newValue = random.nextInt(10) == 0 ? 4 : 2;
        aBoard.get(randomIndex).setValue(newValue);
    }

    public boolean isGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile tile = getTile(i, j);
                if (tile.getValue() == 0)
                    return false; // Empty tile exists
                if (j < BOARD_SIZE - 1 && tile.getValue() == getTile(i, j + 1).getValue())
                    return false; // Merge possible horizontally
                if (i < BOARD_SIZE - 1 && tile.getValue() == getTile(i + 1, j).getValue())
                    return false; // Merge possible vertically
            }
        }
        return true; // No moves available
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(getTile(i, j).getValue()).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}