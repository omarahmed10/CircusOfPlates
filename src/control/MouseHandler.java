package control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import models.Player;

public class MouseHandler implements MotionHandler, MouseMotionListener {

	private int oldX = 0, newX;
	private Player player;
	private int dx;

	public MouseHandler(Player player) {
		this.player = player;
		dx = 0;
	}

	@Override
	public int getDx() {
		return dx;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		synchronized (player) {
			newX = e.getX();
			if (newX > oldX) {
				// move right.
				player.notify();
				dx = 8;
			} else if (newX < oldX) {
				// move left.
				player.notify();
				dx = -8;
			} else if (newX == oldX) {
				// stop.
				dx = 0;
			}
			oldX = newX;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
