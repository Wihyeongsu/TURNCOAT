package JavaFX.Model;

public class GameState {
  private final Board board;
  private final Player[] players = new Player[2];
  private int currentPlayerIndex = 0; // player turn
  private boolean isGameOver;
  private Player winner;
  private int lastMovedRow;
  private int lastMovedCol;
  private Stone selectedStone;


  public GameState() {
    this.board = new Board();
    this.currentPlayerIndex = 0;
    this.isGameOver = false;
    this.winner = null;
    players[0] = new Player("Player1", 0);
    players[1] = new Player("Player2", 1);
    board.showBoard();
    System.out.println("Turn: " + players[currentPlayerIndex].getName());
  }

  public Player getWinner() {
    return winner;
  }

  public Board getBoard() {
    return board;
  }

  public Player getCurrentPlayer() {
    return players[currentPlayerIndex];
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public void switchTurn() {
    selectedStone = null;
    currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    System.out.println("Turn:" + players[currentPlayerIndex].getName());
    board.showBoard();
  }

  public void setSelectedStone(int row, int col){
    Stone stone = board.getStoneAt(row, col);

    if (stone != null && stone.getColor() == players[currentPlayerIndex].getColor()) {
      selectedStone = stone;
    }
  }

  public Stone getSelectedStone() {
    return selectedStone;
  }

  public void moveStone(int toRow, int toCol) {
    Player currentPlayer = players[currentPlayerIndex];
    int currentRow = selectedStone.getRow();
    int currentCol = selectedStone.getCol();

    if (selectedStone.getColor() == currentPlayer.getColor()) {
      board.moveStone(currentRow, currentCol, toRow, toCol);
      lastMovedRow = toRow;
      lastMovedCol = toCol;
    }

    // check
    if (board.isConnected4(toRow, toCol)) {
      isGameOver = true;
      winner = players[board.getStoneAt(toRow, toCol).getColor()];
    } else {
      switchTurn();
    }
  }

  public boolean isMovedOneCell(int toRow, int toCol){
    int fromRow = selectedStone.getRow();
    int fromCol = selectedStone.getCol();
    return board.isMovedOneCell(fromRow, fromCol, toRow, toCol);
  }

  public boolean isLastMovedStone(int row, int col){
    return (lastMovedRow == row && lastMovedCol == col);
  }
}