package minesweeper;

public class Command {
	enum Type {
		TOGGLE_FLAG_MODE, TOUCH_CELL, ERROR;
	}
	private Type type;
	private int x;
	private int y;

	public String toString() {
		switch (type) {
			case TOGGLE_FLAG_MODE:
        return "Toggle Flag Mode";
			case TOUCH_CELL:
        return "Touch Cell " + x + "," + y;
			case ERROR:
        return "Error";
			default:
        return "Unknown Command";
		}
	}

	public static Command newToggleFlagMode() {
		Command command = new Command();
		command.type = Type.TOGGLE_FLAG_MODE;
		return command;
	}

	public static Command newTouchCell(int x, int y) {
		Command command = new Command();
		command.type = Type.TOUCH_CELL;
		command.x = x;
		command.y = y;
		return command;
	}

	public static Command newError() {
		Command command = new Command();
		command.type = Type.ERROR;
		return command;
	}

	public Type getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
