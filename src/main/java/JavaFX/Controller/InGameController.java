package JavaFX.Controller;

import JavaFX.Model.Board;
import JavaFX.Model.GameState;
import JavaFX.Model.Player;
import JavaFX.Model.Stone;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class InGameController {
  @FXML
  private GridPane gridBoard;
  @FXML
  private AnchorPane centerAnchor;
  @FXML
  private Shape clickedStone;
  @FXML
  private Text TURN;
  @FXML
  private Text displayTurn;

  private GameState game;

  public void initialize() {
    game = new GameState();
    updateTurnLabel();
    setEventHandler();
  }

  public void setEventHandler(){
    // gridBoard
    for (Node node : gridBoard.getChildren()) {
      if (node instanceof Rectangle) {
        Rectangle rectangle = (Rectangle) node;
        if (rectangle.getWidth() == 80 && rectangle.getHeight() == 80) {
          rectangle.setOnMouseClicked(event -> frontEventHandler(event));
          System.out.println("Set EventHandler: " + rectangle.getId());
        }
        else if(rectangle.getWidth() == 104 && rectangle.getHeight() == 104){
          rectangle.setOnMouseClicked(event -> frontEventHandler(event));
        }
      }
      else if (node instanceof Circle) {
        Circle circle = (Circle) node;
        if (circle.getRadius() == 40) {
          circle.setOnMouseClicked(event -> frontEventHandler(event));
          System.out.println("Set EventHandler: " + circle.getId());
        }
      }
    }
  }

  // get all events
  // process corresponding event
  public void frontEventHandler(Event event) {
    if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
      if (event.getSource() instanceof Rectangle
          && ((Rectangle) event.getSource()).getWidth() == 104
          && ((Rectangle) event.getSource()).getHeight() == 104) {
        handleCellClick((MouseEvent) event);
      }
      else handleStoneClick((MouseEvent) event);
    }
  }

  public void handleCellClick(MouseEvent event) {
    int cellRow = GridPane.getRowIndex((Node) event.getSource());
    int cellCol = GridPane.getColumnIndex((Node) event.getSource());

    System.out.println("Cell clicked: " + cellRow + " " + cellCol);

    if(game.getBoard().isPresent(cellRow, cellCol)){
      System.out.println("That cell already contains stone!");
      return;
    }

    deleteHighlight();

    if (game.getSelectedStone() != null) {
      if (!game.isMovedOneCell(cellRow, cellCol)){
        System.out.println("Illegal move!");
        return;
      }
      // selectedStone position
      int stoneRow = game.getSelectedStone().getRow();
      int stoneCol = game.getSelectedStone().getCol();

      toggleNodeColor(cellRow, cellCol);
      moveNode(stoneRow, stoneCol, cellRow, cellCol);
      clickedStone = null;

      // Move selectedStone
      game.moveStone(cellRow, cellCol);
    }

    // Update turn
    updateTurnLabel();

    // Check if the game is over
    if(game.isGameOver()) {
      showGameOverMessage();
      System.exit(0); // terminate
    }
  }

  // Get stone info
  // Compare stone color with player color
  // Enable to click
  public void handleStoneClick(MouseEvent event) {
    // get stone position
    Integer row = GridPane.getRowIndex((Node) event.getSource());
    Integer col = GridPane.getColumnIndex((Node) event.getSource());
    if (row == null || col == null) {
      System.out.println("Null");
      return;
    }
    if (game.isLastMovedStone(row, col)) {
      System.out.println("Illegal selection!");
      return;
    }

    // Get clicked stone data
    Stone selectedStone = game.getBoard().getStoneAt(row, col);

    if (selectedStone != null) {
      int currentPlayerColor = game.getCurrentPlayer().getColor();
      int selectedStoneColor = selectedStone.getColor();

      if (selectedStoneColor == currentPlayerColor) {

        // highlight
        clickedStone = (Shape) event.getSource();
        deleteHighlight();
        clickedStone.getStyleClass().add("highlight");

        System.out.println("Stone clicked!");
        System.out.println("Stone color"  + selectedStoneColor);
        System.out.println("Player color: " + currentPlayerColor);
        System.out.println("Clickedstone: " + clickedStone);

        game.setSelectedStone(row, col);
      }
    }
  }

  public void toggleNodeColor(int row, int col){
    // toggle color
    if (game.getBoard().isToggleCell(row, col)) {
      clickedStone.setFill((game.getSelectedStone().getColor() == 0) ? Color.WHITE : Color.web("#ff6f1f"));
    }
  }

  public void moveNode(int fromRow, int fromCol, int toRow, int toCol){
    // remove node
    gridBoard.getChildren().removeIf(n -> GridPane.getRowIndex(n) == fromRow
        && GridPane.getColumnIndex(n) == fromCol
        && n.equals(clickedStone));

    // add node
    GridPane.setRowIndex(clickedStone, toRow);
    GridPane.setColumnIndex(clickedStone, toCol);
    gridBoard.getChildren().add(clickedStone);
  }

  public void deleteHighlight(){
    for (Node node : gridBoard.getChildren()) {
      if (node instanceof Shape) {
        node.getStyleClass().remove("highlight");
      }
    }
  }

  public void updateTurnLabel() {
    centerAnchor.getChildren().removeAll(TURN, displayTurn);
    displayTurn.setText(game.getCurrentPlayer().getName());
    if(game.getCurrentPlayerIndex() == 0){
      TURN.setStyle("-fx-fill: #ff6f1f;");
      displayTurn.setStyle("-fx-fill: #ff6f1f;");
    }
    else {
      TURN.setStyle("-fx-fill: WHITE;");
      displayTurn.setStyle("-fx-fill: WHITE;");
    }
    centerAnchor.getChildren().addAll(TURN, displayTurn);
  }

  public void showGameOverMessage() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText(null);
    alert.setContentText(game.getWinner().getName() + " wins!");
    alert.showAndWait();

    System.out.println("Game Over!");
    System.out.println(game.getWinner().getName() + "wins!");
  }
}