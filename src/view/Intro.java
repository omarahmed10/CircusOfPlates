package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import control.MediumStrategy;
import control.PlateFactory;
import control.Strategy;

public class Intro extends JPanel
		implements Serializable, Runnable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image introBackGround;
	private JButton newGame, option, exit, load;
	private long startTime, currentTime;
	private JFrame parent;
	private Strategy strategyIF;
	private JFileChooser fs;
	private PlateFactory plateFactory;

	public Intro(JFrame parent) {
		try {
			introBackGround = ImageLoader.loadImage("IntroBackGround.png").getImage();
			newGame = new JButton(ImageLoader.loadImage("newGame.png"));
			option = new JButton(ImageLoader.loadImage("option.png"));
			exit = new JButton(ImageLoader.loadImage("exit.png"));
			load = new JButton("Load");
			load.setFont(new Font("DejaVu Sans Condensed",
					Font.BOLD | Font.ITALIC, 30));
			load.setForeground(Color.ORANGE);
			fs = new JFileChooser();
			fs.setMultiSelectionEnabled(true);
			fs.setDialogTitle("Class Loader");
			setLayout(null);
			setButtonProp(newGame, "newGame", 160);
			setButtonProp(option, "option", 240);
			setButtonProp(exit, "exit", 330);
			setButtonProp(load, "load", 420);
		} catch (Exception e) {
			e.printStackTrace();
		}
		startTime = System.currentTimeMillis();
		currentTime = 0;
		this.parent = parent;
		strategyIF = new MediumStrategy();// default Strategy Speed.

		plateFactory = PlateFactory.getInstance();
	}

	private void setButtonProp(JButton button, String actionCommand, int y) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setBounds(500, y, 300, 80);
		button.addActionListener(this);
		button.setActionCommand(actionCommand);
		add(button);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(introBackGround, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void run() {
		currentTime = System.currentTimeMillis();
		setButtonsVisibility(false);
		while (currentTime - startTime < 2000) {
			currentTime = System.currentTimeMillis();
		}
		setButtonsVisibility(true);
		repaint();
	}

	private void setButtonsVisibility(boolean flag) {
		newGame.setVisible(flag);
		option.setVisible(flag);
		exit.setVisible(flag);
		load.setVisible(flag);
	}

	public void setStrategy(Strategy s) {
		strategyIF = s;
	}

	private void startGame() {
		Board gameBoard = new Board(getWidth(), getHeight(), strategyIF);
		parent.add(gameBoard);
		removeAll();
		parent.remove(this);
	}

	private void addLoadedClass(File[] classes) {
		for (File file : classes) {
			plateFactory.loadClass(file.getAbsolutePath());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("newGame")) {
			startGame();
		} else if (e.getActionCommand().equals("option")) {
			new OptionScreen(this);
		} else if (e.getActionCommand().equals("exit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("load")) {
			fs.showOpenDialog(parent);
			File[] classes = fs.getSelectedFiles();
			addLoadedClass(classes);
		}

	}
}
