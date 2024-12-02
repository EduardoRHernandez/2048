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
        initializeBoard();
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
        return new Tile(aBoard.get(x * BOARD_SIZE + y));
    }

    public void setTile(int x, int y, Tile tile) {
        aBoard.set(x * BOARD_SIZE + y, new Tile(tile));
    }

    void resetMergedState() {
        for (Tile tile : aBoard) {
            tile.setMerged(false);
        }
    }

    public List<Tile> getRow(int rowIndex) {
        List<Tile> row = new ArrayList<>();
        for (Tile tile : aBoard.subList(rowIndex * BOARD_SIZE, (rowIndex + 1) * BOARD_SIZE)) {
            row.add(new Tile(tile));
        }
        return row;
    }

    public List<Tile> getColumn(int colIndex) {
        List<Tile> column = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            column.add(new Tile(aBoard.get(i * BOARD_SIZE + colIndex)));
        }
        return column;
    }

    public void move(Directions direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }

    private void moveLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideLeft(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            mergeLeft(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideLeft(i);
        }
        resetMergedState();
    }

    private void moveRight() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideRight(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            mergeRight(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideRight(i);
        }
        resetMergedState();
    }

    private void moveUp() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideUp(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            mergeUp(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideUp(i);
        }
        resetMergedState();
    }

    private void moveDown() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideDown(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            mergeDown(i);
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            slideDown(i);
        }
        resetMergedState();
    }

    // Sliding methods
    private void slideLeft(int row) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            Tile tile = getTile(row, i);
            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex++;
            }
        }
        updateRowWithNewValues(newline, row);
    }

    private void slideRight(int row) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = BOARD_SIZE - 1;

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            Tile tile = getTile(row, i);
            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex--;
            }
        }
        updateRowWithNewValues(newline, row);
    }

    private void slideUp(int col) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            Tile tile = getTile(i, col);
            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex++;
            }
        }
        updateColumnWithNewValues(newline, col);
    }

    private void slideDown(int col) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = BOARD_SIZE - 1;

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            Tile tile = getTile(i, col);
            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex--;
            }
        }
        updateColumnWithNewValues(newline, col);
    }

    // Merging methods
    private void mergeLeft(int row) {
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            Tile currentTile = getTile(row, i);
            Tile nextTile = getTile(row, i + 1);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i++; // Skip next tile after merge
            }
        }
    }

    private void mergeRight(int row) {
        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            Tile currentTile = getTile(row, i);
            Tile nextTile = getTile(row, i - 1);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i--; // Skip next tile after merge
            }
        }
    }

    private void mergeUp(int col) {
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            Tile currentTile = getTile(i, col);
            Tile nextTile = getTile(i + 1, col);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i++; // Skip next tile after merge
            }
        }
    }

    private void mergeDown(int col) {
        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            Tile currentTile = getTile(i, col);
            Tile nextTile = getTile(i - 1, col);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i--; // Skip next tile after merge
            }
        }
    }

    // Helper methods for updating board values
    private void updateRowWithNewValues(int[] newline, int row) {
        for (int i = 0; i < newline.length; i++) {
            if (newline[i] != 0) {
                Tile tile = getTile(row, i);
                tile.setValue(newline[i]);
            }
        }
    }

    private void updateColumnWithNewValues(int[] newline, int col) {
        for (int i = 0; i < newline.length; i++) {
            if (newline[i] != 0) {
                Tile tile = getTile(i, col);
                tile.setValue(newline[i]);
            }
        }
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