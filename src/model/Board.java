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

    public List<Tile> getBoard() {
        ArrayList<Tile> board = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Tile aTile = new Tile(getTile(i, j));
                board.add(aTile);
            }
        }
        return Collections.unmodifiableList(board);
    }

    public Tile getTile(int x, int y) {
        return aBoard.get(x * BOARD_SIZE + y);
    }

    public void setTile(int x, int y, Tile tile) {
        aBoard.set(x * BOARD_SIZE + y, tile);
    }

    public void moveLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 1; j < BOARD_SIZE; j++) {
                if (getTile(i, j).getValue() != 0) {
                    if (getTile(i, j - 1).getValue() == 0) {
                        getTile(i, j - 1).setValue(getTile(i, j).getValue());
                        getTile(i, j).setValue(0);
                    } else if (getTile(i, j - 1).getValue() == getTile(i, j).getValue()) {
                        getTile(i, j - 1).setMerged(true);
                        getTile(i, j).setValue(0);
                    }
                }
            }
        }
    }

    public void moveRight() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = BOARD_SIZE - 2; j >= 0; j--) {
                if (getTile(i, j).getValue() != 0) {
                    if (getTile(i, j + 1).getValue() == 0) {
                        getTile(i, j + 1).setValue(getTile(i, j).getValue());
                        getTile(i, j).setValue(0);
                    } else if (getTile(i, j + 1).getValue() == getTile(i, j).getValue()) {
                        getTile(i, j + 1).setMerged(true);
                        getTile(i, j).setValue(0);
                    }
                }
            }
        }
    }

    public void moveUp() {
        for (int i = 1; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (getTile(i, j).getValue() != 0) {
                    if (getTile(i - 1, j).getValue() == 0) {
                        getTile(i - 1, j).setValue(getTile(i, j).getValue());
                        getTile(i, j).setValue(0);
                    } else if (getTile(i - 1, j).getValue() == getTile(i, j).getValue()) {
                        getTile(i - 1, j).setMerged(true);
                        getTile(i, j).setValue(0);
                    }
                }
            }
        }
    }

    public void moveDown() {
        for (int i = BOARD_SIZE - 2; i >= 0; i--) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (getTile(i, j).getValue() != 0) {
                    if (getTile(i + 1, j).getValue() == 0) {
                        getTile(i + 1, j).setValue(getTile(i, j).getValue());
                        getTile(i, j).setValue(0);
                    } else if (getTile(i + 1, j).getValue() == getTile(i, j).getValue()) {
                        getTile(i + 1, j).setMerged(true);
                        getTile(i, j).setValue(0);
                    }
                }
            }
        }
    }
}