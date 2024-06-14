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

    // set eventhandler
    for (Node node : gridBoard.getChildren()) {
      if (node instanceof Rectangle) {
        Rectangle rectangle = (Rectangle) node;
        if (rectangle.getWidth() == 80 && rectangle.getHeight() == 80) {
          rectangle.setOnMouseClicked(event -> handleStoneClick(event));
          System.out.println("Set EventHandler: " + rectangle.getId());
        }
        else if(rectangle.getWidth() == 104 && rectangle.getHeight() == 104){
          rectangle.setOnMouseClicked(event -> handleCellClick(event));
        }
      }
      else if (node instanceof Circle) {
        Circle circle = (Circle) node;
        if (circle.getRadius() == 40) {
          circle.setOnMouseClicked(event -> handleStoneClick(event));
          System.out.println("Set EventHandler: " + circle.getId());
        }
      }
    }
  }

  public void handleCellClick(MouseEvent event) {
    int nextRow = GridPane.getRowIndex((Node) event.getSource());
    int nextCol = GridPane.getColumnIndex((Node) event.getSource());

    if(game.getBoard().isPresent(nextRow, nextCol)){
      System.out.println("The cell already contains stone!");
      return;
    }

    System.out.println("Cell clicked: " + nextRow + " " + nextCol);

    // delete highlight
    for (Node node : gridBoard.getChildren()) {
      if (node instanceof Shape) {
        node.getStyleClass().remove("highlight");
      }
    }

    if (game.getSelectedStone() != null) {
      // selectedStone position
      int currentRow = game.getSelectedStone().getRow();
      int currentCol = game.getSelectedStone().getCol();

      // toggle color
      if (game.getBoard().isToggleCell(nextRow, nextCol)) {
        clickedStone.setFill((game.getSelectedStone().getColor() == 0) ? Color.WHITE : Color.web("#ff6f1f"));
      }

      // remove node
      gridBoard.getChildren().removeIf(n -> GridPane.getRowIndex(n) == currentRow
          && GridPane.getColumnIndex(n) == currentCol
          && n.equals(clickedStone));

      // add node
      GridPane.setRowIndex(clickedStone, nextRow);
      GridPane.setColumnIndex(clickedStone, nextCol);
      gridBoard.getChildren().add(clickedStone);

      // Clear selected stone
      clickedStone = null;

      // move selectedStone
      game.moveStone(nextRow, nextCol);
    }

    // turntransition
    updateTurnLabel();

    // Check if the game is over
    if(game.isGameOver()) {
      showGameOverMessage();
      System.exit(0); // terminate
    }
  }


  // 돌 클릭시 돌에 대한 정보를 가져옴
  // 돌의 색과 플레이어의 색을 비교
  // 동일할 경우에만 돌 클릭 가능
  public void handleStoneClick(MouseEvent event) {

    // get stone position
    Integer row = GridPane.getRowIndex((Node) event.getSource());
    Integer col = GridPane.getColumnIndex((Node) event.getSource());

    if (row == null || col == null) {
      return;
    }

    // get stone data
    Stone selectedStone = game.getBoard().getStoneAt(row, col);

    if (selectedStone != null) {
      int currentPlayerColor = game.getCurrentPlayer().getColor();
      int selectedStoneColor = selectedStone.getColor();

      if (selectedStoneColor == currentPlayerColor) {
        System.out.println("Stone clicked! Color: " + selectedStoneColor);
        System.out.println("Player Color: " + currentPlayerColor);

        // highlight
        clickedStone = (Shape) event.getSource();
        System.out.println("clickedStone: " + clickedStone);
        for (Node node : gridBoard.getChildren()) {
          if (node instanceof Shape) {
            node.getStyleClass().remove("highlight");
          }
        }
        clickedStone.getStyleClass().add("highlight");

        game.setSelectedStone(row, col);
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