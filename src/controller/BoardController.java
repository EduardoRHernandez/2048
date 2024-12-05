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

  public boolean moveUp() {
    return board.move(Directions.UP);
  }

  public boolean moveDown() {
    return board.move(Directions.DOWN);
  }

  public boolean moveLeft() {
    return board.move(Directions.LEFT);
  }

  public boolean moveRight() {
    return board.move(Directions.RIGHT);
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

  public boolean isGameOver() {
    return board.isGameOver();
  }
}
