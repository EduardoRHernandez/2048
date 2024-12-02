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
        board.moveUp();
    }

    public void moveDown() {
        board.moveDown();
    }

    public void moveLeft() {
        board.moveLeft();
    }

    public void moveRight() {
        board.moveRight();
    }

    public List<Integer> getBoard() {
        return board.getBoardValues();
    }

    public void addRandomTile() {
        board.addRandomTile();
    }

    public boolean isGameOver() {
        return board.isGameOver();
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
