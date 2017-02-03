package models;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

public class PolygonPlate extends Plate {
	private Polygon shape;
	private final int width = 80, height = 20;

	public PolygonPlate(int startX, int startY) {
		super(startX, startY);
		shape = new Polygon(
				new int[] { startX, startX + width / 4,
						(startX + width * 3 / 4), startX + width },
				new int[] { startY, startY + height, startY + height, startY },
				4);
		setBound(new Rectangle(width, height));
	}

	@Override
	public void update() {
		int currentX = getCurrentPosition().x;
		int currentY = getCurrentPosition().y;
		shape = new Polygon(
				new int[] { currentX, currentX + width / 4,
						currentX + width * 3 / 4, currentX + width },
				new int[] { currentY, currentY + height, currentY + height,
						currentY },
				4);
	}

	@Override
	public synchronized void draw(Graphics g) {
		if (shape != null) {
			g.setColor(getColor());
			g.fillPolygon(shape);
		}
	}

}
