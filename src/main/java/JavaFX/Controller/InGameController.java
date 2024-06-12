package JavaFX.Controller;

import JavaFX.Model.Board;
import JavaFX.Model.GameState;
import JavaFX.Model.Player;
import JavaFX.Model.Stone;
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
  private GridPane Board;
  @FXML
  private AnchorPane centerAnchor;
  @FXML
  private Shape selectedStone;
  @FXML
  private Text TURN;
  @FXML
  private Text displayTurn;

  private GameState game;

  public void initialize() {
    game = new GameState();
    updateTurnLabel();

    // set eventhandler
    for (Node node : Board.getChildren()) {
      if (node instanceof Rectangle) {
        Rectangle rectangle = (Rectangle) node;
        if (rectangle.getWidth() == 80 && rectangle.getHeight() == 80) {
          rectangle.setOnMouseClicked(event -> handleStoneClick(event));
        }
        else if(rectangle.getWidth() == 104 && rectangle.getHeight() == 104){
          rectangle.setOnMouseClicked(event -> handleCellClick(event));
        }
      }
      else if (node instanceof Circle) {
        Circle circle = (Circle) node;
        if (circle.getRadius() == 40) {
          circle.setOnMouseClicked(event -> handleStoneClick(event));
        }
      }
    }
  }

  @FXML
  public void handleCellClick(MouseEvent event) {
    System.out.println("Cell clicked");
    int nextRow = GridPane.getRowIndex((Node) event.getSource());
    int nextCol = GridPane.getColumnIndex((Node) event.getSource());

    System.out.println(nextRow + " " + nextCol);

    // delete highlight
    for (Node node : Board.getChildren()) {
      if (node instanceof Shape) {
        node.getStyleClass().remove("highlight");
      }
    }

    if (game.getSelectedStone() != null) {
      // selectedStone position
      int currentRow = game.getSelectedStone().getRow();
      int currentCol = game.getSelectedStone().getCol();

      // move selectedStone
      game.moveStone(game.getSelectedStone(), nextRow, nextCol);

      // toggle color
      if (game.getBoard().isToggleCell(nextRow, nextCol)) {
        game.getSelectedStone().toggleColor();
        if (selectedStone instanceof Circle) {
          ((Circle) selectedStone).setFill(game.getSelectedStone().getColor().equals("orange") ? Color.WHITE : Color.web("#ff6f1f"));
        } else if (selectedStone instanceof Rectangle) {
          ((Rectangle) selectedStone).setFill(game.getSelectedStone().getColor().equals("orange") ? Color.WHITE : Color.web("#ff6f1f"));
        }
      }

      // remove node
      Board.getChildren().removeIf(n -> GridPane.getRowIndex(n) == currentRow
          && GridPane.getColumnIndex(n) == currentCol
          && n.equals(selectedStone));

      // add node
      GridPane.setRowIndex(selectedStone, nextRow);
      GridPane.setColumnIndex(selectedStone, nextCol);
      Board.getChildren().add(selectedStone);

      // Clear selected stone
      game.setSelectedStone(null);
      selectedStone = null;
    }

    // turntransition
    updateTurnLabel();

    // Check if the game is over
    if(game.isGameOver()) {
      showGameOverMessage();
      System.exit(0); // terminate
    }
  }

  @FXML
  public void handleStoneClick(MouseEvent event) {
    System.out.println("Stone clicked");

    Integer row = GridPane.getRowIndex((Node) event.getSource());
    Integer col = GridPane.getColumnIndex((Node) event.getSource());

    if (row == null || col == null) {
      return;
    }

    Stone clickedStone = game.getBoard().getStoneAt(row, col);

    if (clickedStone != null) {
      String currentPlayerColor = game.getCurrentPlayer().getColor();
      String clickedStoneColor = clickedStone.getColor();

      if (clickedStoneColor.equals(currentPlayerColor)) {
        // highlight
        selectedStone = (Shape) event.getSource();
        System.out.println("selectedStone: " + selectedStone);
        for (Node node : Board.getChildren()) {
          if (node instanceof Shape) {
            node.getStyleClass().remove("highlight");
          }
        }
        selectedStone.getStyleClass().add("highlight");

        game.selectStone(row, col);
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
    alert.setContentText(game.getCurrentPlayer().getName() + " wins!");
    alert.showAndWait();
  }
}