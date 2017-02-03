package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import models.Player;
import view.Board;

public class KeyHandler implements KeyListener, MotionHandler {

	private Player player;
	private int dx;
	private volatile boolean paused;
	private Board board;

	@Override
	public int getDx() {
		return dx;
	}

	public KeyHandler(Player player, Board board) {
		this.player = player;
		dx = 0;
		this.board = board;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		synchronized (player) {
			if (e.getKeyCode() == player.getRightKey()) {
				player.notify();
				dx = 30;
			}
			if (e.getKeyCode() == player.getLeftKey()) {
				player.notify();
				dx = -30;
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dx = 0;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dx = 0;
		}
		if (!paused && e.getKeyCode() == KeyEvent.VK_SPACE) {
			// make the thread to wait.
			System.out.println("Paused");
			board.pauseGame();
			paused = true;
		} else if (paused && e.getKeyCode() == KeyEvent.VK_SPACE) {
			// notify all threads.
			System.out.println("Resumed");
			board.resumeGame();
			paused = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		synchronized (player) {
			if (arg0.getKeyCode() == player.getRightKey()) {
				player.notify();
				dx = 20;
			}
			if (arg0.getKeyCode() == player.getLeftKey()) {
				player.notify();
				dx = -20;
			}

		}
	}
}
