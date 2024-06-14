package JavaFX.Model;


import java.util.Random;

import static java.lang.Math.abs;

public class Board {
  private Stone[][] board;
  private boolean[][] toggleCell;

  public Board() {
    board = new Stone[8][8];
    toggleCell = new boolean[8][8];
    initializeBoard();
  }

  private void initializeBoard() {

    for (int i = 1; i < 5; i++) {
      board[i][1] = new Stone(0, 0, i, 1);
      board[i][7] = new Stone(1, 1, i, 7);
      board[8-i][1] = new Stone(0, 0, 8-i, 1);
      board[8-i][7] = new Stone(1, 1, 8-i, 7);
    }

    // toggle 위치 set
    Random random = new Random();
    for (int i = 2; i < 7; i++) {
      for (int j = 2; j < 7; j++) {
        toggleCell[i][j] = random.nextBoolean();
      }
    }

    // 배열 출력
    for (boolean[] row : toggleCell) {
      for (boolean value : row) {
        System.out.print(value ? "T " : "F ");
      }
      System.out.println();
    }
  }

  public Stone getStoneAt(int row, int col) {
    return board[row][col];
  }

  public void moveStone(int fromRow, int fromCol, int toRow, int toCol) {
    if(!isMovedOneCell(fromRow, fromCol, toRow, toCol)) return;
    Stone stone = board[fromRow][fromCol];
    board[fromRow][fromCol] = null;
    if (isToggleCell(toRow, toCol)) {
      stone.toggleColor();
    }
    stone.setRow(toRow);
    stone.setCol(toCol);
    board[toRow][toCol] = stone;
  }

  public boolean isMovedOneCell(int fromRow, int fromCol, int toRow, int toCol) {
    int dy = abs(toRow - fromRow);
    int dx = abs(toCol - fromCol);
    if(dy > 1 || dx > 1) return false;
    return true;
  }

  public boolean isToggleCell(int row, int col) {
    return toggleCell[row][col];
  }

  public boolean isPresent(int row, int col){
    return board[row][col] != null;
  }

  public boolean isConnected4(int row, int col) {
    if (board[row][col] == null) {
      return false;
    }

    int color = board[row][col].getColor();

    // 가로 방향
    int count = 1;
    int left = col - 1;
    int right = col + 1;
    while (left >= 2 && isSameColor(row, left, color)) {
      count++;
      left--;
    }
    while (right < 7 && isSameColor(row, right, color)) {
      count++;
      right++;
    }
    if (count >= 4) {
      return true;
    }

    // 세로 방향
    count = 1;
    int up = row - 1;
    int down = row + 1;
    while (up >= 2 && isSameColor(up, col, color)) {
      count++;
      up--;
    }
    while (down < 7 && isSameColor(down, col, color)) {
      count++;
      down++;
    }
    if (count >= 4) {
      return true;
    }

    // 대각선 우하향
    count = 1;
    int upLeft = row - 1;
    int downRight = row + 1;
    left = col - 1;
    right = col + 1;
    while (upLeft >= 2 && left >= 2 && isSameColor(upLeft, left, color)) {
      count++;
      upLeft--;
      left--;
    }
    while (downRight < 7 && right < 7 && isSameColor(downRight, right, color)) {
      count++;
      downRight++;
      right++;
    }
    if (count >= 4) {
      return true;
    }

    // 대각선 우상향
    count = 1;
    int upRight = row - 1;
    int downLeft = row + 1;
    left = col - 1;
    right = col + 1;
    while (upRight >= 2 && right < 7 && isSameColor(upRight, right, color)) {
      count++;
      upRight--;
      right++;
    }
    while (downLeft < 7 && left >= 2 && isSameColor(downLeft, left, color)) {
      count++;
      downLeft++;
      left--;
    }
    if (count >= 4) {
      return true;
    }

    return false;
  }

  private boolean isSameColor(int row, int col, int color) {
    if (row < 2 || row > 6 || col < 2 || col > 6) {
      return false;
    }
    Stone stone = board[row][col];
    return stone != null && (stone.getColor() == color);
  }
}
