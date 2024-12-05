package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board {
    private ArrayList<Tile> aBoard;
    private static final int BOARD_SIZE = 4;
    private Random random;
    private int currentScore = 0;

    private static final String X_ERROR_MESSAGE = "x-coordinate should be between 0 and " + BOARD_SIZE + ".";
    private static final String Y_ERROR_MESSAGE = "y-coordinate should be between 0 and " + BOARD_SIZE + ".";
    private static final String NON_NEGATIVE_ERROR_MESSAGE = "Tile values should be non-negative.";

    public Board() {
        this.random = new Random();
        initializeBoard();
    }

    public Board(Random random) {
        this.random = random;
        initializeBoard();
    }

    /**
     * Initialize the board with 4x4 empty tiles.
     * 
     * @post aBoard contains 16 tiles with value 0.
     */
    private void initializeBoard() {
        aBoard = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                aBoard.add(new Tile());
            }
        }
        assert aBoard.size() == BOARD_SIZE * BOARD_SIZE : "Board should have 16 tiles.";
        for (Tile tile : aBoard) {
            assert tile.getValue() == 0 : "All tiles should be initialized with value 0.";
            assert !tile.isMerged() : "All tiles should be initialized as not merged.";
        }
    }

    /**
     * @return a list of all tile values in the board.
     * @post The returned list is unmodifiable.
     */
    public List<Integer> getBoardValues() {
        List<Integer> boardValues = new ArrayList<>();
        for (Tile tile : aBoard) {
            boardValues.add(tile.getValue());
        }
        List<Integer> unmodifiableList = Collections.unmodifiableList(boardValues);
        assert unmodifiableList == boardValues : "The returned list should be unmodifiable.";
        return unmodifiableList;
    }

    /**
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the tile at the given position
     * @pre 0 <= x < {@link #BOARD_SIZE} and 0 <= y < {@link #BOARD_SIZE}
     * @post the returned tile is not null
     */
    private Tile getThisTile(int x, int y) {
        assert 0 <= x && x < BOARD_SIZE : X_ERROR_MESSAGE;
        assert 0 <= y && y < BOARD_SIZE : Y_ERROR_MESSAGE;
        return aBoard.get(x * BOARD_SIZE + y);
    }

    /**
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the tile at the given position
     * @pre 0 <= x < {@link #BOARD_SIZE} and 0 <= y < {@link #BOARD_SIZE}
     * @post the returned tile is not null and is a copy of the tile at the given
     *       position
     */
    public Tile getTile(int x, int y) {
        assert 0 <= x && x < BOARD_SIZE : X_ERROR_MESSAGE;
        assert 0 <= y && y < BOARD_SIZE : Y_ERROR_MESSAGE;
        return new Tile(aBoard.get(x * BOARD_SIZE + y));
    }

    /**
     * @param x    the x-coordinate of the tile
     * @param y    the y-coordinate of the tile
     * @param tile the tile to set at the given position
     * @pre 0 <= x < {@link #BOARD_SIZE} and 0 <= y < {@link #BOARD_SIZE}
     * @post the tile at the given position is the given tile
     */
    public void setTile(int x, int y, Tile tile) {
        assert 0 <= x && x < BOARD_SIZE : X_ERROR_MESSAGE;
        assert 0 <= y && y < BOARD_SIZE : Y_ERROR_MESSAGE;
        aBoard.set(x * BOARD_SIZE + y, new Tile(tile));
    }

    /**
     * @return the current score
     * @post the returned score is the sum of all values of all tiles that have been
     *       merged
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Resets the merged state of all tiles on the board.
     * 
     * @post All tiles on the board are set to not merged.
     */
    void resetMergedState() {
        for (Tile tile : aBoard) {
            tile.setMerged(false);
            assert !tile.isMerged() : "Tile should be reset to not merged.";
        }
    }

    /**
     * @param rowIndex the row index of the row to get
     * @return the row at the given index
     * @pre 0 <= rowIndex < {@link #BOARD_SIZE}
     * @post the returned row is a copy of the row at the given index
     */
    public List<Tile> getRow(int rowIndex) {
        assert 0 <= rowIndex && rowIndex < BOARD_SIZE : "Row index should be between 0 and " + BOARD_SIZE + ".";
        List<Tile> row = new ArrayList<>();
        for (Tile tile : aBoard.subList(rowIndex * BOARD_SIZE, (rowIndex + 1) * BOARD_SIZE)) {
            row.add(new Tile(tile));
        }
        assert row.size() == BOARD_SIZE : "Row should have " + BOARD_SIZE + " tiles.";
        return row;
    }

    /**
     * @param colIndex the column index of the column to get
     * @return the column at the given index
     * @pre 0 <= colIndex < {@link #BOARD_SIZE}
     * @post the returned column is a copy of the column at the given index
     */
    public List<Tile> getColumn(int colIndex) {
        assert 0 <= colIndex && colIndex < BOARD_SIZE : "Column index should be between 0 and " + BOARD_SIZE + ".";
        List<Tile> column = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            column.add(new Tile(aBoard.get(i * BOARD_SIZE + colIndex)));
        }
        assert column.size() == BOARD_SIZE : "Column should have " + BOARD_SIZE + " tiles.";
        return column;
    }

    /**
     * @return a snapshot of the current board state
     * @post the returned list is a copy of the current board state
     */
    private List<Integer> snapshotBoard() {
        List<Integer> snapshot = new ArrayList<>();
        for (Tile tile : aBoard) {
            snapshot.add(tile.getValue());
        }
        assert snapshot.size() == aBoard.size() : "Snapshot should have the same number of elements as the board.";
        return snapshot;
    }

    /**
     * Checks if the board state has changed between two snapshots.
     *
     * @param before the list of tile values before changes
     * @param after  the list of tile values after changes
     * @return true if there is a difference between the snapshots, false otherwise
     * @pre before.size() == after.size() : "Both snapshots must have the same
     *      size."
     * @post result == true if there is at least one differing element, false
     *       otherwise
     */
    private boolean hasBoardChanged(List<Integer> before, List<Integer> after) {
        assert before.size() == after.size() : "Both snapshots must have the same size.";

        for (int i = 0; i < before.size(); i++) {
            if (!before.get(i).equals(after.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the board in the specified direction and adds a new random tile if
     * the board changed.
     *
     * @param direction the direction to move the board
     * @return true if the board changed, false otherwise
     * @pre direction must be one of the four directions
     * @post if hasBoardChanged(before, after) then addRandomTile() has been
     *       called, else not
     */
    public boolean move(Directions direction) {
        assert direction != null && direction.isValid()
                : "Direction must be one of the four directions.";

        // Snapshot the board before the move
        List<Integer> beforeMove = snapshotBoard();

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

        // Snapshot the board after the move
        List<Integer> afterMove = snapshotBoard();

        // Add a random tile only if the board has changed
        if (hasBoardChanged(beforeMove, afterMove)) {
            addRandomTile();
            return true;
        } else {
            return false;
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
            Tile tile = getThisTile(i, col);
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
            Tile tile = getThisTile(i, col);
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
            Tile currentTile = getThisTile(row, i);
            Tile nextTile = getThisTile(row, i + 1);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i++; // Skip next tile after merge
            }
        }
    }

    private void mergeRight(int row) {
        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            Tile currentTile = getThisTile(row, i);
            Tile nextTile = getThisTile(row, i - 1);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i--; // Skip next tile after merge
            }
        }
    }

    private void mergeUp(int col) {
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            Tile currentTile = getThisTile(i, col);
            Tile nextTile = getThisTile(i + 1, col);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i++; // Skip next tile after merge
            }
        }
    }

    private void mergeDown(int col) {
        for (int i = BOARD_SIZE - 1; i > 0; i--) {
            Tile currentTile = getThisTile(i, col);
            Tile nextTile = getThisTile(i - 1, col);

            if (shouldMerge(currentTile, nextTile)) {
                performMerge(currentTile, nextTile);
                i--; // Skip next tile after merge
            }
        }
    }

    // Helper methods for updating board values
    /**
     * @param newline the new values to set on the row
     * @param row     the row to update
     * @pre newline.length == {@link #BOARD_SIZE}
     * @post The row at the given index has its values set to the given new values
     */
    private void updateRowWithNewValues(int[] newline, int row) {
        assert newline.length == BOARD_SIZE : "New line should have " + BOARD_SIZE + " elements.";
        for (int i = 0; i < newline.length; i++) {
            Tile tile = getThisTile(row, i);
            tile.setValue(newline[i]);
        }
        for (int i = 0; i < newline.length; i++) {
            Tile tile = getThisTile(row, i);
            assert tile.getValue() == newline[i]
                    : "Tile at row " + row + " and column " + i + " should have value " + newline[i] + ".";
        }
    }

    /**
     * @param newline the new values to set on the column
     * @param col     the column to update
     * @pre newline.length == {@link #BOARD_SIZE}
     * @post The column at the given index has its values set to the given new
     *       values
     */
    private void updateColumnWithNewValues(int[] newline, int col) {
        assert newline.length == BOARD_SIZE : "New line should have " + BOARD_SIZE + " elements.";
        for (int i = 0; i < newline.length; i++) {
            Tile tile = getThisTile(i, col);
            tile.setValue(newline[i]);
        }
        for (int i = 0; i < newline.length; i++) {
            Tile tile = getThisTile(i, col);
            assert tile.getValue() == newline[i]
                    : "Tile at row " + i + " and column " + col + " should have value " + newline[i] + ".";
        }
    }

    /**
     * @param currentTile the current tile to check
     * @param nextTile    the next tile to check
     * @return true if the two tiles should be merged, false otherwise
     * @pre currentTile.getValue() >= 0 && nextTile.getValue() >= 0
     * @post result == true if the two tiles have the same value and are not merged,
     *       false otherwise
     */
    private boolean shouldMerge(Tile currentTile, Tile nextTile) {
        assert currentTile.getValue() >= 0 : NON_NEGATIVE_ERROR_MESSAGE;
        assert nextTile.getValue() >= 0 : NON_NEGATIVE_ERROR_MESSAGE;
        return currentTile.getValue() == nextTile.getValue()
                && !currentTile.isMerged()
                && !nextTile.isMerged();
    }

    // Extracted helper method to perform the merge
    /**
     * @param currentTile the current tile to merge
     * @param nextTile    the next tile to merge
     * @pre currentTile.getValue() >= 0 && nextTile.getValue() >= 0
     * @pre shouldMerge(currentTile, nextTile)
     * @post currentTile.getValue() == 2 * currentTile.getValue() and
     *       currentTile.isMerged() and
     *       nextTile.getValue() == 0 and
     *       this.currentScore == this.currentScore + 2 * currentTile.getValue()
     */
    private void performMerge(Tile currentTile, Tile nextTile) {
        assert currentTile.getValue() >= 0 : NON_NEGATIVE_ERROR_MESSAGE;
        assert nextTile.getValue() >= 0 : NON_NEGATIVE_ERROR_MESSAGE;
        assert shouldMerge(currentTile, nextTile) : "Tiles should be mergeable.";

        int val = currentTile.getValue() * 2;
        currentTile.setValue(val);
        currentTile.setMerged(true);
        nextTile.setValue(0);
        this.currentScore += val;

        assert currentTile.getValue() == val : "Current tile value should be doubled.";
        assert currentTile.isMerged() : "Current tile should be marked as merged.";
        assert nextTile.getValue() == 0 : "Next tile value should be reset to 0.";
        assert this.currentScore == this.currentScore + val : "Score should be incremented.";
    }

    /**
     * @pre There is at least one empty tile on the board
     * @post There is one more tile on the board with a value of 2 or 4
     */
    public void addRandomTile() {
        ArrayList<Integer> emptyTileIndices = new ArrayList<>();

        // Find all empty tiles
        for (int i = 0; i < aBoard.size(); i++) {
            if (aBoard.get(i).getValue() == 0) {
                emptyTileIndices.add(i);
            }
        }

        // If there are no empty tiles, throw an exception
        if (emptyTileIndices.isEmpty()) {
            throw new IllegalStateException("There are no empty tiles on the board");
        }

        // Choose a random index from the list of empty tiles
        int randomIndex = emptyTileIndices.get(random.nextInt(emptyTileIndices.size()));

        // Set the random tile to 2 (90% chance) or 4 (10% chance)
        int newValue = random.nextInt(10) == 0 ? 4 : 2;
        aBoard.get(randomIndex).setValue(newValue);
    }

    /**
     * Checks if the game is over.
     * 
     * @return true if there are no more moves available, false otherwise
     * @post result == true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile tile = getThisTile(i, j);
                if (tile.getValue() == 0)
                    return false; // Empty tile exists
                if (j < BOARD_SIZE - 1 && tile.getValue() == getThisTile(i, j + 1).getValue())
                    return false; // Merge possible horizontally
                if (i < BOARD_SIZE - 1 && tile.getValue() == getThisTile(i + 1, j).getValue())
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
                sb.append(getThisTile(i, j).getValue()).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}