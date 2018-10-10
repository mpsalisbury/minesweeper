package minesweeper;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandFetcher {
  public Command nextCommand() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Command: ");
    String input = scanner.nextLine();
    if (input.equals("F")) {
      return Command.newToggleFlagMode();
    }
    Pattern cellPattern = Pattern.compile("(\\d+),(\\d+)");
    Matcher cellMatcher = cellPattern.matcher(input);

    if (cellMatcher.matches()) {
      int x = Integer.parseInt(cellMatcher.group(1));
      int y = Integer.parseInt(cellMatcher.group(2));
      return Command.newTouchCell(x - 1, y - 1);
    }
    return Command.newError();
  }
}
