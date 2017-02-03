package models;

import java.awt.Graphics2D;
import java.util.Stack;
import javax.swing.ImageIcon;
import control.MotionHandler;
import control.PlayerControl;
import view.Board;
import view.ImageLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player implements Runnable {

	private ImageIcon playerImage;
	private int horizontalPosition;
	private Board gameBoard;
	public final int HOLDER_WIDTH = 64;
	private MotionHandler motionHandler;
	private int commandLeftKey;
	private int commandRightKey;
	public int leftStackHeight, leftStackStart, rightStackHeight, rightStackEnd,
			initialPosition, score;
	public Stack<Plate> leftStack, rightStack;
	private final Logger logger;
	private volatile boolean suspended, inGame;
	private Thread t;
	private PlayerControl pC;

	public Player(Board board, int commandLeftKey, int commandRightKey,
			int startPoint) {
		logger = LogManager.getLogger();
		this.gameBoard = board;
		score = 0;
		initialPosition = startPoint;
		this.commandLeftKey = commandLeftKey;
		this.commandRightKey = commandRightKey;
		leftStack = new Stack<Plate>();
		rightStack = new Stack<Plate>();
		playerImage = ImageLoader.loadImage("player.png");
		horizontalPosition = initialPosition;
		leftStackHeight = playerImage.getIconHeight();
		leftStackStart = initialPosition;
		rightStackHeight = playerImage.getIconHeight();
		rightStackEnd = initialPosition + playerImage.getIconWidth();
		// player collision thread.
		inGame = true;
		pC = new PlayerControl(board, this);
		pC.start();
	}

	public void addMotionHandler(MotionHandler mH) {
		this.motionHandler = mH;
	}

	public int getLeftKey() {
		return commandLeftKey;
	}

	public int getRightKey() {
		return commandRightKey;
	}

	public ImageIcon getPlayerImage() {
		return playerImage;
	}

	private void setPosition(int horizontalPosition) {
		this.horizontalPosition = horizontalPosition;
	}

	public int getPosition() {
		return horizontalPosition;
	}

	public int getScore() {
		return score;
	}

	@Override
	public void run() {
		logger.debug("Thread of " + this + " is run");
		synchronized (this) {
			while (inGame) {
				try {
					while (suspended) {
						this.wait();
					}
					this.wait();
					move();
					gameBoard.repaint();
				} catch (InterruptedException e1) {
					logger.fatal("Thread of " + this + " is interrupted");
					e1.printStackTrace();
				}
			}
		}
	}

	private void move() {
		int screenLeftmost = 0;
		int screenRightmost = gameBoard.getWidth();
		// The player & the plates he holds are limited by the left end of
		// screen
		if (leftStackStart + motionHandler.getDx() < screenLeftmost) {
			setPosition(leftStackStart);
			rightStackEnd -= leftStackStart;
			leftStackStart = 0;
		}
		// player 1 is limited by middle of screen
		else if (initialPosition == 0 && this.getPosition()
				+ motionHandler.getDx() > gameBoard.getWidth() / 2
						- this.getPlayerImage().getIconWidth() - gameBoard.sW) {
			this.setPosition(gameBoard.getWidth() / 2 - gameBoard.sW
					- this.getPlayerImage().getIconWidth());
		}
		// player 2 is limited by middle of screen
		else if (initialPosition == gameBoard.getWidth() / 2 + gameBoard.sW
				&& this.getPosition()
						+ motionHandler.getDx() < gameBoard.getWidth() / 2
								+ 40) {
			this.setPosition(initialPosition);
		}
		// The player & the plates he holds are limited by the right end of
		// screen
		else if (rightStackEnd + motionHandler.getDx() > screenRightmost) {
			setPosition(rightStackEnd - playerImage.getIconWidth());
			leftStackStart += screenRightmost - rightStackEnd;
			rightStackEnd = screenRightmost;
		}

		else {
			setPosition(getPosition() + motionHandler.getDx());
			leftStackStart += motionHandler.getDx();
			rightStackEnd += motionHandler.getDx();
			movePlates(leftStack, motionHandler.getDx());
			movePlates(rightStack, motionHandler.getDx());
		}
		logger.debug("Player " + this + " moved");
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(this.getPlayerImage().getImage(), this.getPosition(),
				gameBoard.getHeight() - this.getPlayerImage().getIconHeight(),
				gameBoard);
	}

	private void movePlates(Stack<Plate> plateStack, int horizontalDistance) {
		for (Plate plate : plateStack) {
			plate.move(horizontalDistance);
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "music");
			t.start();
		}
	}

	public void suspend() {
		suspended = true;
		pC.suspend();
	}

	public void stop() {
		inGame = false;
	}

	public synchronized void resume() {
		suspended = false;
		pC.resume();
		this.notify();
	}
}