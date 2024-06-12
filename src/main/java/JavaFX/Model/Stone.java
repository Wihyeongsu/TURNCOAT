package JavaFX.Model;

public class Stone {
  private String color;
  private String shape;
  private int row;
  private int col;

  public Stone(String color, String shape, int row, int col) {
    this.color = color;
    this.shape = shape;
    this.row = row;
    this.col = col;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  public void toggleColor() {
    if (color.equals("orange")) setColor("white");
    else if (color.equals("white")) setColor("orange");
  }
}
