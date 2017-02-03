package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws InterruptedException
	 */
	public MainFrame() throws InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws InterruptedException
	 */
	private void initialize() throws InterruptedException {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		/////
//		JFileChooser fileChooser = new JFileChooser();
//		fileChooser.addChoosableFileFilter(new CirFileFilter());
//		fileChooser.setAcceptAllFileFilterUsed(false);
//		MenuBar menu = new MenuBar(fileChooser);
//		setJMenuBar(menu);
//		//
		Intro intro = new Intro(this);
		intro.setSize(getWidth(), getHeight());
		add(intro);
		Thread introThread = new Thread(intro);
		introThread.start();
	}

}
