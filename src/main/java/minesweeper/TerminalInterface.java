package minesweeper;

public class TerminalInterface {

  private Grid grid;
  private CommandFetcher fetcher;
  private boolean flagMode;

  public static void main(String[] args) {
    TerminalInterface terminal = new TerminalInterface();
    terminal.run();
  }

  public TerminalInterface() {
    grid = new Grid(8, 8, 9);
    fetcher = new CommandFetcher();
    flagMode = false;
  }

  public void run() {
    grid.printForUser(System.out);
    while (true) {
      Command command = fetcher.nextCommand();
      System.out.println(command);
      executeCommand(command);
      grid.printForUser(System.out);
      if (grid.hasBombExploded()) {
        System.out.println("Game Over, You Hit A Bomb!");
        break;
      }
      if (grid.hasWon()) {
        System.out.println("Congratulations, You Swept The Mines!");
        break;
      }
    }
  }

  private void executeCommand(Command command) {
    switch (command.getType()) {
      case TOGGLE_FLAG_MODE:
        toggleFlagMode();
        break;
      case TOUCH_CELL:
        touchCell(command.getX(), command.getY());
        break;
      default:
        System.out.println("Unimplemented Command");
    }
  }

  private void toggleFlagMode() {
    flagMode = !flagMode;
    System.out.println("Flag Mode " + flagMode);
  }

  private void touchCell(int x, int y) {
    try {
      if (!flagMode) {
        grid.openCell(x, y);
      } else {
        grid.flagCell(x, y);
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage() + "\nPlease Try Again");
    }
  }
}
