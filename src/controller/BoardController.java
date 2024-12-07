package controller;

import java.util.List;
import model.*;

public class BoardController {

  private Board board;

  public BoardController() {
    board = new Board();
  }

  /**
   * Sets the board of the controller.
   * 
   * @param board the new board
   * @pre board != null
   * @post this.board == board
   */
  public void setBoard(Board board) {
    this.board = new Board(board);
  }

  /**
   * Moves the board up.
   * 
   * @return true if the board changed, false otherwise
   * @pre none
   * @post if hasBoardChanged(before, after) then addRandomTile() has been called,
   *       else not
   */
  public boolean moveUp() {
    return board.move(Directions.UP);
  }

  /**
   * Moves the board down.
   * 
   * @return true if the board changed, false otherwise
   * @pre none
   * @post if hasBoardChanged(before, after) then addRandomTile() has been
   *       called, else not
   */
  public boolean moveDown() {
    return board.move(Directions.DOWN);
  }

  /**
   * Moves the board to the left.
   *
   * @return true if the board changed, false otherwise
   * @pre none
   * @post if hasBoardChanged(before, after) then addRandomTile() has been
   *       called, else not
   */
  public boolean moveLeft() {
    return board.move(Directions.LEFT);
  }

  /**
   * Moves the board to the right.
   *
   * @return true if the board changed, false otherwise
   * @pre none
   * @post if hasBoardChanged(before, after) then addRandomTile() has been
   *       called, else not
   */
  public boolean moveRight() {
    return board.move(Directions.RIGHT);
  }

  /**
   * Returns the current board as a list of tile values.
   * 
   * @return the current board as a list of tile values
   * @pre none
   * @post the returned list is unmodifiable
   */
  public List<Integer> getBoard() {
    return board.getBoardValues();
  }

  /**
   * Adds a new random tile to the board.
   * 
   * @pre there is at least one empty tile on the board
   * @post there is one more tile on the board with a value of 2 or 4
   */
  public void addRandomTile() {
    board.addRandomTile();
  }

  /**
   * Returns the current score.
   * 
   * @return the current score
   * @pre the board object is initialized
   * @post the returned int is the current score of the board
   */
  public int getCurrentScore() {
    return board.getCurrentScore();
  }

  /**
   * @return a string representation of the board
   * @pre none
   * @post the returned string is a string representation of the board
   */
  public String toString() {
    return board.toString();
  }

  /**
   * Sets the tile at the given position on the board.
   * 
   * @param x    the x-coordinate of the tile
   * @param y    the y-coordinate of the tile
   * @param tile the tile to set at the given position
   * @pre 0 <= x < {@link Board#BOARD_SIZE} and 0 <= y < {@link Board#BOARD_SIZE}
   * @post the tile at the given position is the given tile
   */
  public void setTile(int x, int y, Tile tile) {
    board.setTile(x, y, tile);
  }

  /**
   * Retrieves the tile at the specified position on the board.
   * 
   * @param x the x-coordinate of the tile
   * @param y the y-coordinate of the tile
   * @return the tile at the given position
   * @pre 0 <= x < {@link Board#BOARD_SIZE} and 0 <= y < {@link Board#BOARD_SIZE}
   * @post the returned tile is not null and is a copy of the tile at the given
   *       position
   */
  public Tile getTile(int x, int y) {
    return board.getTile(x, y);
  }

  /**
   * Checks if the game is over.
   * 
   * @return true if there are no more moves available, false otherwise
   * @pre none
   * @post result == true if the game is over, false otherwise
   */
  public boolean isGameOver() {
    return board.isGameOver();
  }
}
