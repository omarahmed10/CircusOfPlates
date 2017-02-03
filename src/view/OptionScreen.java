package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import control.DifficultStrategy;
import control.EasyStrategy;
import control.MediumStrategy;
import control.Strategy;

import javax.swing.JButton;

public class OptionScreen implements ActionListener {

	private JFrame frame;
	private boolean selected;
	private Strategy selectedStrategy;
	private Intro parent;

	/**
	 * Create the Screen.
	 */
	public OptionScreen(Intro parent) {
		this.parent = parent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 500, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		JLabel lblNewLabel = new JLabel("Select Level of Difficulty");
		lblNewLabel.setBounds(50, 10, 200, 15);
		frame.getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("Easy");
		btnNewButton.setBounds(10, 30, 117, 25);
		btnNewButton.addActionListener(this);
		frame.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Medium");
		btnNewButton_1.setBounds(140, 30, 117, 25);
		btnNewButton_1.addActionListener(this);
		frame.getContentPane().add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Hard");
		btnNewButton_2.setBounds(270, 30, 117, 25);
		btnNewButton_2.addActionListener(this);
		frame.getContentPane().add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Done");
		btnNewButton_3.setBounds(140, 70, 117, 25);
		btnNewButton_3.addActionListener(this);
		frame.getContentPane().add(btnNewButton_3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton jB;
		if (!selected && e.getActionCommand().equals("Easy")) {
			jB = (JButton) e.getSource();
			jB.setEnabled(false);
			selected = true;
			selectedStrategy = new EasyStrategy();
		} else if (!selected && e.getActionCommand().equals("Medium")) {
			jB = (JButton) e.getSource();
			jB.setEnabled(false);
			selected = true;
			selectedStrategy = new MediumStrategy();
		} else if (!selected && e.getActionCommand().equals("Hard")) {
			jB = (JButton) e.getSource();
			jB.setEnabled(false);
			selected = true;
			selectedStrategy = new DifficultStrategy();
		} else if (selected && e.getActionCommand().equals("Done")) {
			jB = (JButton) e.getSource();
			jB.setEnabled(false);
			parent.setStrategy(selectedStrategy);
			frame.dispose();
		}
	}

}
