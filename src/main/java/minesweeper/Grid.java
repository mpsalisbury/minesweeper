package minesweeper;

import java.io.PrintStream;
import java.util.Random;

public class Grid {
  private int height;
  private int width;
  private int bombCount;
  private boolean bombExploded;
  private int goodCoversRemaining;
  private Cell[][] cell;

  public enum UserCellState {
    COVERED,
    FLAGGED,
    BOMB_0,
    BOMB_1,
    BOMB_2,
    BOMB_3,
    BOMB_4,
    BOMB_5,
    BOMB_6,
    BOMB_7,
    BOMB_8,
    HAS_BOMB;
  }

  private static class Cell {
    enum CoverState {
      OPEN,
      COVERED,
      FLAGGED;

      public String toString() {
        switch (this) {
          case OPEN:
            return "O ";
          case COVERED:
            return "C ";
          case FLAGGED:
            return "F ";
          default:
            return "xx";
        }
      }

      public UserCellState getUserState() {
        switch (this) {
          case COVERED:
            return UserCellState.COVERED;
          case FLAGGED:
            return UserCellState.FLAGGED;
          default:
            throw new IllegalStateException();
        }
      }
    }

    enum CellState {
      BOMB_0,
      BOMB_1,
      BOMB_2,
      BOMB_3,
      BOMB_4,
      BOMB_5,
      BOMB_6,
      BOMB_7,
      BOMB_8,
      HAS_BOMB;

      public static CellState getBombCountState(int bombCount) {
        switch (bombCount) {
          case 0:
            return BOMB_0;
          case 1:
            return BOMB_1;
          case 2:
            return BOMB_2;
          case 3:
            return BOMB_3;
          case 4:
            return BOMB_4;
          case 5:
            return BOMB_5;
          case 6:
            return BOMB_6;
          case 7:
            return BOMB_7;
          case 8:
            return BOMB_8;
          default:
            throw new IllegalArgumentException("Bad Bombcount " + bombCount);
        }
      }

      public String toString() {
        switch (this) {
          case BOMB_0:
            return "/ ";
          case BOMB_1:
            return "1 ";
          case BOMB_2:
            return "2 ";
          case BOMB_3:
            return "3 ";
          case BOMB_4:
            return "4 ";
          case BOMB_5:
            return "5 ";
          case BOMB_6:
            return "6 ";
          case BOMB_7:
            return "7 ";
          case BOMB_8:
            return "8 ";
          case HAS_BOMB:
            return "X ";
          default:
            return "xx";
        }
      }

      public UserCellState getUserState() {
        switch (this) {
          case BOMB_0:
            return UserCellState.BOMB_0;
          case BOMB_1:
            return UserCellState.BOMB_1;
          case BOMB_2:
            return UserCellState.BOMB_2;
          case BOMB_3:
            return UserCellState.BOMB_3;
          case BOMB_4:
            return UserCellState.BOMB_4;
          case BOMB_5:
            return UserCellState.BOMB_5;
          case BOMB_6:
            return UserCellState.BOMB_6;
          case BOMB_7:
            return UserCellState.BOMB_7;
          case BOMB_8:
            return UserCellState.BOMB_8;
          case HAS_BOMB:
            return UserCellState.HAS_BOMB;
          default:
            throw new IllegalStateException();
        }
      }
    }

    private CellState cellState;
    private CoverState coverState;

    public Cell() {
      cellState = CellState.BOMB_0;
      coverState = CoverState.COVERED;
    }

    public boolean hasBomb() {
      return cellState == CellState.HAS_BOMB;
    }

    public boolean hasZero() {
      return cellState == CellState.BOMB_0;
    }

    public void placeBomb() {
      cellState = CellState.HAS_BOMB;
    }

    public void setAdjacentBombCount(int bombCount) {
      cellState = CellState.getBombCountState(bombCount);
    }

    public boolean isCovered() {
      return coverState == CoverState.COVERED;
    }

    public UserCellState getUserState() {
      if (coverState == CoverState.OPEN) {
        return cellState.getUserState();
      } else {
        return coverState.getUserState();
      }
    }

    public String toUserString() {
      if (coverState == CoverState.OPEN) {
        return cellState.toString();
      } else {
        return coverState.toString();
      }
    }

    public String toBombLayerString() {
      return cellState.toString();
    }

    public void open() {
      if (coverState == CoverState.COVERED) {
        coverState = CoverState.OPEN;
      }
    }

    public void toggleFlag() {
      switch (coverState) {
        case FLAGGED:
          coverState = CoverState.COVERED;
          break;
        case COVERED:
          coverState = CoverState.FLAGGED;
          break;
        case OPEN:
          //TODO: Add Warning Message
          break;
      }
    }
  }

  public Grid(int height, int width, int bombCount) {
    this.height = height;
    this.width = width;
    this.bombCount = bombCount;
    goodCoversRemaining = (height * width) - bombCount;
    bombExploded = false;
    cell = new Cell[width][height];
    initializeCells();
    initializeBombs();
    intializeCounts();
  }

  private void initializeCells() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        cell[x][y] = new Cell();
      }
    }
  }

  private void initializeBombs() {
    for (int b = 0; b < bombCount; b++) {
      placeBomb();
    }
  }

  private void placeBomb() {
    Random random = new Random();
    int x;
    int y;
    do {
      x = random.nextInt(width);
      y = random.nextInt(height);
    } while (cell[x][y].hasBomb());
    cell[x][y].placeBomb();
  }

  private void intializeCounts() {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (!cell[x][y].hasBomb()) {
          cell[x][y].setAdjacentBombCount(getCount(x, y));
        }
      }
    }
  }

  private int getCount(int x, int y) {
    int count = 0;
    for (int cx = x - 1; cx <= x + 1; cx++) {
      for (int cy = y - 1; cy <= y + 1; cy++) {
        if (hasBomb(cx, cy)) {
          count++;
        }
      }
    }
    return count;
  }

  private boolean hasBomb(int x, int y) {
    if (coordinatesInRange(x, y)) {
      return cell[x][y].hasBomb();
    }
    return false;
  }

  private void checkCoordinates(int x, int y) {
    if (!coordinatesInRange(x, y)) {
      throw new IllegalArgumentException("Coordinates Out Of Range");
    }
  }

  private boolean coordinatesInRange(int x, int y) {
    if (x < 0 || x > width - 1) {
      return false;
    }
    if (y < 0 || y > height - 1) {
      return false;
    }
    return true;
  }

  public void openCell(int x, int y) {
    checkCoordinates(x, y);
    if (cell[x][y].isCovered()) {
      cell[x][y].open();
      if (cell[x][y].hasBomb()) {
        bombExploded = true;
      } else {
        goodCoversRemaining--;
      }
      if (cell[x][y].hasZero()) {
        clearSquare(x, y);
      }
    }
  }

  public void clearSquare(int centerX, int centerY) {
    for (int x = centerX - 1; x <= centerX + 1; x++) {
      for (int y = centerY - 1; y <= centerY + 1; y++) {
        if (coordinatesInRange(x, y)) {
          openCell(x, y);
        }
      }
    }
  }

  public void flagCell(int x, int y) {
    checkCoordinates(x, y);
    cell[x][y].toggleFlag();
  }

  public boolean hasBombExploded() {
    return bombExploded;
  }

  public boolean hasWon() {
    return goodCoversRemaining < 1;
  }

  public UserCellState getUserCellState(int x, int y) {
    return cell[x][y].getUserState();
  }

  public void printForUser(PrintStream out) {
    for (int y = height - 1; y >= 0; y--) {
      for (int x = 0; x < width; x++) {
        out.print(cell[x][y].toUserString());
      }
      out.println();
    }
  }

  public void printBombLayer(PrintStream out) {
    for (int y = height - 1; y >= 0; y--) {
      for (int x = 0; x < width; x++) {
        out.print(cell[x][y].toBombLayerString());
      }
      out.println();
    }
  }
}
