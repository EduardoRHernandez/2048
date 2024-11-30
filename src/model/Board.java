package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private ArrayList<Tile> aBoard;
    private static final int BOARD_SIZE = 4;

    public Board() {
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

    private void slide(int start, boolean isRow, boolean isLeftOrUp) {
        int[] newline = new int[BOARD_SIZE];
        int newIndex = isLeftOrUp ? 0 : BOARD_SIZE - 1;

        for (int i = 0; i < BOARD_SIZE; i++) {
            int idx = isRow ? start * BOARD_SIZE + i : i * BOARD_SIZE + start;
            Tile tile = getTile(idx / BOARD_SIZE, idx % BOARD_SIZE);

            if (tile.getValue() != 0) {
                newline[newIndex] = tile.getValue();
                tile.setValue(0);
                newIndex = isLeftOrUp ? newIndex + 1 : newIndex - 1;
            }
        }

        // Place the non-zero tiles back into the row or column
        for (int i = 0; i < newline.length; i++) {
            if (newline[i] != 0) {
                Tile tile = getTile(isRow ? start : i, isRow ? i : start);
                tile.setValue(newline[i]);
            }
        }
    }

    // Merge tiles in any direction (left/right/up/down) on a row or column.
    private void merge(int start, boolean isRow, boolean isLeftOrUp) {
        for (int i = isLeftOrUp ? 0 : BOARD_SIZE - 2; isLeftOrUp ? i < BOARD_SIZE - 1
                : i >= 0; i = isLeftOrUp ? i + 1 : i - 1) {
            int idx = isRow ? start * BOARD_SIZE + i : i * BOARD_SIZE + start;
            Tile currentTile = getTile(idx / BOARD_SIZE, idx % BOARD_SIZE);
            Tile nextTile = getTile(idx / BOARD_SIZE, (idx + 1) % BOARD_SIZE);

            if (currentTile.getValue() == nextTile.getValue() && !currentTile.isMerged() && !nextTile.isMerged()) {
                currentTile.setValue(currentTile.getValue() * 2); // Merge the tiles by doubling
                currentTile.setMerged(true);
                nextTile.setValue(0); // Set the next tile to 0 after merging
            }
        }
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
}