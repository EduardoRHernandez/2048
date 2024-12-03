package controller;

import java.util.List;
import model.*;

public class BoardController {

    Board board;

    public BoardController() {
        board = new Board();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void moveUp() {
        board.move(Directions.UP);
    }

    /************* ✨ Codeium Command ⭐ *************/
    /**
     * Moves the tiles down (positive y direction) on the board, combining tiles
     * of the same value.
     */
    /****** 6c842576-a108-4727-971f-4d85626c41b9 *******/
    public void moveDown() {
        board.move(Directions.DOWN);
    }

    public void moveLeft() {
        board.move(Directions.LEFT);
    }

    public void moveRight() {
        board.move(Directions.RIGHT);
    }

    public List<Integer> getBoard() {
        return board.getBoardValues();
    }

    public void addRandomTile() {
        board.addRandomTile();
    }

    public int getCurrentScore() {
        return board.getCurrentScore();
    }

    public String toString() {
        return board.toString();
    }

    public void setTile(int x, int y, Tile tile) {
        board.setTile(x, y, tile);
    }

    public Tile getTile(int x, int y) {
        return board.getTile(x, y);
    }
}
