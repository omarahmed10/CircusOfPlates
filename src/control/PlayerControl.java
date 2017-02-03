package control;

import java.awt.Point;
import java.util.Iterator;
import java.util.Stack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import models.Plate;
import models.PlateCaptured;
import models.Player;
import view.Board;

public class PlayerControl implements Runnable {
	private final Logger logger;
	private models.Player player;
	private Board board;
	private volatile boolean suspended, inGame;
	private Thread t;

	public PlayerControl(Board board, models.Player player) {
		logger = LogManager.getLogger();
		this.player = player;
		this.board = board;
		inGame = true;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (inGame) {
			Iterator<Plate> t = board.getPlateFactory().createIterator();
			while (t.hasNext()) {
				if (t != null) {
					Plate p = t.next();
					if (p != null) {
						collide(p);
					}
				}
			}
			synchronized (this) {
				while (suspended) {
					try {
						System.out.println("Stoped");
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public synchronized void makePlateReusable(Plate p) {
		board.getPlateFactory().removePlate(p);
		board.getPlateFactory().addReusablePlate(p);
	}

	public void collide(Plate plate) {
		// left stack
		if (isLeftCollided(plate)) {
			logger.debug("Plate " + plate
					+ " collisioned with left stack of player " + this);
			player.leftStackHeight += plate.getHeight();
			// the left stack start is the leftmost edge of palyerImage or a
			// on it plate
			player.leftStackStart = Integer.min(player.getPosition(),
					plate.getCurrentPosition().x);
			plate.setState(new PlateCaptured(plate));
			player.leftStack.push(plate);
			if (isThreeSameColor(player.leftStack)) {
				Plate p = player.leftStack.pop();
				player.leftStackHeight = player.leftStackHeight - p.getHeight();
				makePlateReusable(p);
				p = player.leftStack.pop();
				player.leftStackHeight = player.leftStackHeight - p.getHeight();
				makePlateReusable(p);
				p = player.leftStack.pop();
				player.leftStackHeight = player.leftStackHeight - p.getHeight();
				makePlateReusable(p);

				player.score++;
				logger.debug("Three successive " + plate.getColor()
						+ "plates ! , " + "player " + this
						+ " player.score is now " + player.score);
			}
		}

		// right stack
		else if (isRightCollided(plate)) {
			logger.debug("Plate " + plate
					+ " collisioned with right stack of player " + this);
			player.rightStackHeight += plate.getHeight();
			// the right stack end is the rightmost edge of palyerImage or a
			// plate on it
			player.rightStackEnd = Integer.max(
					player.getPosition() + player.getPlayerImage().getIconWidth(),
					plate.getCurrentPosition().x + plate.getWidth());

			plate.setState(new PlateCaptured(plate));
			player.rightStack.push(plate);

			if (isThreeSameColor(player.rightStack)) {

				Plate p = player.rightStack.pop();
				player.rightStackHeight = player.rightStackHeight
						- p.getHeight();
				makePlateReusable(p);
				p = player.rightStack.pop();
				player.rightStackHeight = player.rightStackHeight
						- p.getHeight();
				makePlateReusable(p);
				p = player.rightStack.pop();
				player.rightStackHeight = player.rightStackHeight
						- p.getHeight();
				makePlateReusable(p);

				player.score++;
				logger.debug("Three successive " + plate.getColor()
						+ "plates ! , " + "player " + this
						+ " player.score is now " + player.score);
			}
		}
	}

	private boolean isThreeSameColor(Stack<Plate> PlateStake) {
		if (PlateStake.size() >= 3) {
			Stack<Plate> similarStack = new Stack<Plate>();
			for (int i = PlateStake.size() - 1; i >= (PlateStake.size()
					- 3); --i) {
				similarStack.push(PlateStake.get(i));
			}
			Plate lastPlate = similarStack.pop();
			return (lastPlate.getColor() == similarStack.pop().getColor()
					&& lastPlate.getColor() == similarStack.pop().getColor());
		}

		return false;
	}

	private boolean isLeftCollided(Plate plate) {
		Point plateBottomCentre = new Point(
				plate.getCurrentPosition().x + plate.getWidth() / 2,
				plate.getCurrentPosition().y + plate.getHeight());

		return plateBottomCentre.x <= (player.getPosition()
				+ player.HOLDER_WIDTH)
				&& plateBottomCentre.x >= player.getPosition()
				&& matchHeight(plateBottomCentre.y, player.leftStackHeight);
	}

	private boolean isRightCollided(Plate plate) {
		Point plateBottomCentre = new Point(
				plate.getCurrentPosition().x + plate.getWidth() / 2,
				plate.getCurrentPosition().y + plate.getHeight());

		return plateBottomCentre.x <= (player.getPosition()
				+ player.getPlayerImage().getIconWidth())
				&& plateBottomCentre.x >= ((player.getPosition()
						+ player.getPlayerImage().getIconWidth())
						- player.HOLDER_WIDTH)
				&& matchHeight(plateBottomCentre.y, player.rightStackHeight);
	}

	private boolean matchHeight(int y, int stackH) {
		return (y + 4) > (board.getHeight() - stackH)
				&& (y - 4) < (board.getHeight() - stackH);
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "playerController");
			t.start();
		}
	}

	public void suspend() {
		suspended = true;
	}

	public void stop() {
		inGame = false;
	}

	public synchronized void resume() {
		suspended = false;
		this.notify();
	}
}
