package JavaFX.Model;

public class Stone {
  // shape: 1 bit
  // color: 1 bit
  // row: 3 bit
  // col: 3 bit

  // shape/color/row/col
  // 0/0/000/000
  private int info;

  public Stone(int shape, int color, int row, int col) {
    info = (shape << 7) + (color << 6) + (row << 3) + col;
  }

  public int getShape(){
    return info >> 7;
  }

  public int getColor() { return (info >> 6) & 1; }

  public void setColor(int color) {
    info = (getShape() << 7) + (color << 6) + (getRow() << 3) + getCol();
  }

  public int getRow() {
    return (info >> 3) & 7;
  }

  public void setRow(int row) {
    info = (getShape() << 7) + (getColor() << 6) + (row << 3) + getCol();
  }

  public int getCol() {
    return info & 7;
  }

  public void setCol(int col) {
    info = (getShape() << 7) + (getColor() << 6) + (getRow() << 3) + col;
  }

  public void toggleColor() {
    if(getColor() == 0) setColor(1);
    else setColor(0);
  }
}
