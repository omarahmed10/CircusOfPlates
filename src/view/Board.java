package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import control.KeyHandler;
import control.MouseHandler;
import control.PlateConsumer;
import control.PlateFactory;
import control.PlateProducer;
import control.Processor;
import control.Strategy;
import models.Plate;
import models.Player;

public class Board extends JPanel implements Serializable, view.Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Graphics2D g2d;
	protected ImageIcon largeShelf1, largeShelf2, smallShelf2, smallShelf1;
	private static PlateFactory plateFactory;
	private Player player1, player2;
	private Processor player1Processor, player2Processor;
	private Image BackGround, separator;
	private Strategy strategyIF;
	private PlateProducer Player2Producer, Player1Producer;
	private PlateConsumer Player1Consumer, Player2Consumer;
	private BackGroundMusic bGM;
	private boolean inGame = true;
	private int maxPlateHeight;
	/**
	 * separator width.
	 */
	public final int sW = 40;

	private JLabel player1Label, player2Label, player1Score, player2Score,
			maxScore;

	protected Board(int width, int height, Strategy IStrategy) {
		BackGround = ImageLoader.loadImage("BackGround.png").getImage();
		this.strategyIF = IStrategy;
		setSize(width, height);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		loadShelfs();
		loadScores();
		plateFactory = PlateFactory.getInstance();
		plateFactory.addObserver(this);
		loadPlayers();
		initializeThreads();
	}

	private void loadPlayers() {
		player1 = new Player(this, KeyEvent.VK_A, KeyEvent.VK_D, 0);
		MouseHandler mH = new MouseHandler(player1);
		player1.addMotionHandler(mH);
		addMouseMotionListener(mH);
		player2 = new Player(this, KeyEvent.VK_A, KeyEvent.VK_D,
				getWidth() / 2 + sW);
		KeyHandler kH = new KeyHandler(player2, this);
		player2.addMotionHandler(kH);
		addKeyListener(kH);
	}

	private void loadScores() {
		player1Label = new JLabel("Player 1");
		player1Label.setOpaque(false);
		player1Label.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 30));
		player1Label.setForeground(Color.ORANGE);

		player2Label = new JLabel("Player 2");
		player2Label.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 30));
		player2Label.setForeground(Color.ORANGE);
		player2Label.setOpaque(false);

		player1Score = new JLabel();
		player1Score.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 30));
		player1Score.setForeground(Color.ORANGE);
		player1Score.setOpaque(false);

		player2Score = new JLabel();
		player2Score.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 30));
		player2Score.setForeground(Color.ORANGE);
		player2Score.setOpaque(false);

		maxScore = new JLabel("Max Score " + strategyIF.getMaxScore());
		maxScore.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 30));
		maxScore.setForeground(Color.ORANGE);
		maxScore.setOpaque(false);
		add(player1Label);
		add(player2Label);
		add(player1Score);
		add(player2Score);
		add(maxScore);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (inGame) {
			g.drawImage(BackGround, 0, 0, getWidth(), getHeight(), this);
			g2d = (Graphics2D) g;
			drawShelfs(g);
			player1.draw(g2d);
			player2.draw(g2d);
			drawPlates(g);
			drawScore();
			checkScore();
			checkPlateHeight();
		}
		Toolkit.getDefaultToolkit().sync();
	}

	public void initializeThreads() {
		player1Processor = new Processor(plateFactory, this, 1, strategyIF);
		player2Processor = new Processor(plateFactory, this, -1, strategyIF);
		Player1Producer = new PlateProducer(player1Processor, 0, 32, 0, 96);
		Player1Consumer = new PlateConsumer(player1Processor);
		Player2Consumer = new PlateConsumer(player2Processor);
		Player2Producer = new PlateProducer(player2Processor, getWidth() - 98,
				32, getWidth() - 85, 96);
		Player1Consumer.start();
		Player2Consumer.start();
		Player1Producer.start();
		Player2Producer.start();
		player1.start();
		player2.start();
		bGM = new BackGroundMusic();
		bGM.start();
	}

	private void drawScore() {
		player1Label.setBounds(20, 140, 150, 30);
		player1Score.setText(player1.getScore() + "");
		player1Score.setBounds(70, 170, 70, 30);
		player2Label.setBounds(getWidth() - 150, 140, 150, 30);
		player2Score.setText(player2.getScore() + "");
		player2Score.setBounds(getWidth() - 70, 170, 70, 30);
		maxScore.setBounds(getWidth() / 2 - 70, 0, 300, 30);
	}

	private void checkScore() {
		if (player1.getScore() >= strategyIF.getMaxScore()) {
			// player1 wins
			inGame = false;
			stopGame();
			showResult("We Have A Winner", "Player One Wins");
		} else if (player2.getScore() >= strategyIF.getMaxScore()) {
			// player2 wins
			inGame = false;
			stopGame();
			showResult("We Have A Winner", "Player Two Wins");
		}
	}

	private void checkPlateHeight() {
		if (player1.leftStackHeight >= maxPlateHeight
				&& player1.rightStackHeight >= maxPlateHeight) {
			// player1 loose
			inGame = false;
			stopGame();
			showResult("Game Over", "Player Two Wins");
		} else if (player2.leftStackHeight >= maxPlateHeight
				&& player2.rightStackHeight >= maxPlateHeight) {
			// player2 loose
			inGame = false;
			stopGame();
			showResult("Game Over", "Player One Wins");
		}
	}

	public void pauseGame() {
		bGM.suspend();
		Player1Producer.suspend();
		Player2Producer.suspend();
		Player1Consumer.suspend();
		Player2Consumer.suspend();
		player1.suspend();
		player2.suspend();
	}

	public void stopGame() {
		removeAll();
		bGM.stop();
		Player1Producer.stop();
		Player2Producer.stop();
		Player1Consumer.stop();
		Player2Consumer.stop();
		player1.stop();
		player2.stop();
	}

	public void resumeGame() {
		bGM.resume();
		Player1Producer.resume();
		Player2Producer.resume();
		Player1Consumer.resume();
		Player2Consumer.resume();
		player1.resume();
		player2.resume();
	}

	public void loadShelfs() {
		largeShelf1 = ImageLoader.loadImage("largeShelf.png");
		largeShelf2 = ImageLoader.loadImage("largeShelf.png");
		smallShelf1 = ImageLoader.loadImage("smallShelf.png");
		smallShelf2 = ImageLoader.loadImage("smallShelf.png");
		maxPlateHeight = getHeight() - 70;
		separator = ImageLoader.loadImage("separator.png").getImage();
	}

	private void drawShelfs(Graphics g) {
		g2d.drawImage(largeShelf1.getImage(), -18, 40, this);
		g2d.drawImage(largeShelf2.getImage(),
				getWidth() - largeShelf2.getIconWidth(), 40, this);
		g2d.drawImage(smallShelf1.getImage(), -5, 110, this);
		g2d.drawImage(smallShelf2.getImage(),
				getWidth() - smallShelf2.getIconWidth(), 110, this);
	}

	public synchronized void drawPlates(Graphics g) {
		try {
			Iterator<Plate> t = plateFactory.createIterator();
			while (t.hasNext()) {
				t.next().draw(g);
			}
			g.drawImage(separator, getWidth() / 2 - sW, 0, 80, getHeight(),
					this);
		} catch (Exception e) {
			System.out.println(plateFactory);
			e.printStackTrace();
			System.exit(0);
		}
	}

	public PlateFactory getPlateFactory() {
		return plateFactory;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		repaint();
	}

	private void showResult(String ad, String result) {

		setBackground(Color.ORANGE);
		JLabel adLabel = new JLabel(ad);
		adLabel.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 50));
		adLabel.setForeground(Color.BLACK);
		adLabel.setBounds(0, 100, 500, 100);
		add(adLabel);
		JLabel resultLabel = new JLabel(result);
		resultLabel.setFont(
				new Font("DejaVu Sans Condensed", Font.BOLD | Font.ITALIC, 50));
		resultLabel.setForeground(Color.BLACK);
		resultLabel.setBounds(100, 200, 500, 100);
		add(resultLabel);
	}
}
