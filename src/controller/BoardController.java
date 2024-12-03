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

  /*************  ✨ Codeium Command ⭐  *************/
  /**
   * Moves the tiles down.
   * @return true if the board state changed (i.e. a tile was moved or merged), false otherwise
   */
  /******  d974140c-7252-4e2d-a650-ce56d20c31b4  *******/
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
