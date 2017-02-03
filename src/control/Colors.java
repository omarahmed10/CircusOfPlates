package control;

import java.awt.Color;
import java.util.Random;

public class Colors {
	private static final Color[] COLORS = new Color[] { Color.BLUE,
			Color.YELLOW, Color.GREEN, Color.RED, Color.CYAN, Color.MAGENTA,
			Color.PINK };
	private static Random colorGenerator = new Random();

	public final static Color getColor() {
		return COLORS[colorGenerator.nextInt(COLORS.length)];
	}

	private Colors() {
	}
}
