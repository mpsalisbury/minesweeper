package minesweeper;

import com.salisburyclan.lpviewport.api.Button0Listener;
import com.salisburyclan.lpviewport.api.Button2Listener;
import com.salisburyclan.lpviewport.api.Color;
import com.salisburyclan.lpviewport.api.LaunchpadApplication;
import com.salisburyclan.lpviewport.api.SubView;
import com.salisburyclan.lpviewport.api.Viewport;
import com.salisburyclan.lpviewport.api.Viewport0;
import com.salisburyclan.lpviewport.api.Viewport1;
import com.salisburyclan.lpviewport.api.WriteLayer;
import com.salisburyclan.lpviewport.geom.Point;
import com.salisburyclan.lpviewport.geom.Range2;
import javafx.application.Application;

public class LaunchpadInterface extends LaunchpadApplication {

  private Grid grid;
  private boolean flagMode;
  private WriteLayer gridLayer;
  private Viewport0 flagViewport;
  private Viewport1 keyViewport;

  public static void main(String[] args) {
    Application.launch(args);
  }

  public LaunchpadInterface() {
    flagMode = false;
  }

  @Override
  public void run() {
    getViewport(this::setupViewport);
  }

  private void setupViewport(Viewport viewport) {
    flagViewport =
        SubView.getSubViewport0(viewport, Point.create(0, viewport.getExtent().yRange().high()));
    flagViewport.addListener(
        new Button0Listener() {
          public void onButtonPressed() {
            toggleFlag();
          }
        });
    Range2 viewportExtent = viewport.getExtent();
    Range2 keyExtent = viewport.getExtent().inset(viewportExtent.getWidth() - 1, 0, 0, 0);
    keyViewport = SubView.getSubViewport1(viewport, keyExtent);
    Range2 gridExtent = viewport.getExtent().inset(0, 0, 1, 1);
    grid =
        new Grid(
            gridExtent.getWidth(),
            gridExtent.getHeight(),
            (gridExtent.getWidth()) * (gridExtent.getHeight()) / 7);
    Viewport gridViewport = SubView.getSubViewport(viewport, gridExtent);
    gridLayer = gridViewport.addLayer();
    gridViewport.addListener(
        new Button2Listener() {
          public void onButtonPressed(Point p) {
            touchCell(p.x(), p.y());
          }
        });
    drawGrid();
    drawFlag();
    drawKey();
  }

  private void touchCell(int x, int y) {
    if (flagMode) {
      grid.flagCell(x, y);
    } else {
      grid.openCell(x, y);
    }
    if (grid.hasBombExploded()) {
      System.out.println("Game Over, You Hit A Bomb!");
      return;
    }
    if (grid.hasWon()) {
      System.out.println("Congratulations, You Swept The Mines!");
      return;
    }
    drawGrid();
  }

  private void toggleFlag() {
    flagMode = !flagMode;
    drawFlag();
  }

  private void drawFlag() {
    if (flagMode) {
      flagViewport.setPixel(Color.YELLOW);
    } else {
      flagViewport.setPixel(Color.BLACK);
    }
  }

  private void drawGrid() {
    gridLayer
        .getExtent()
        .forEach(
            (x, y) -> {
              gridLayer.setPixel(x, y, getGridColor(x, y));
            });
  }

  private void drawKey() {
    for (int x = 0; x <= 7 && x <= keyViewport.getExtent().high(); x++) {
      keyViewport.setPixel(x, getUserStateColor(Grid.UserCellState.getBombState(x + 1)));
    }
  }

  private Color getGridColor(int x, int y) {
    return getUserStateColor(grid.getUserCellState(x, y));
  }

  private Color getUserStateColor(Grid.UserCellState state) {
    switch (state) {
      case COVERED:
        return Color.DARK_GRAY;
      case FLAGGED:
        return Color.YELLOW;
      case BOMB_0:
        return Color.WHITE;
      case BOMB_1:
        return Color.BLUE;
      case BOMB_2:
        return Color.GREEN;
      case BOMB_3:
        return Color.RED;
      case BOMB_4:
        return Color.CYAN;
      case BOMB_5:
        return Color.PURPLE;
      case BOMB_6:
        return Color.PINK;
      case BOMB_7:
        return Color.MAGENTA;
      case BOMB_8:
        return Color.YELLOW_GREEN;
      case HAS_BOMB:
        return Color.ORANGE;
      default:
        throw new IllegalStateException();
    }
  }
}
