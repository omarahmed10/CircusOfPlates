package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import control.PlateFactory;
import saving.Json;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private File savedFile, loadedFile;
	private JFileChooser fileChooser;
	private Json json;

	public MenuBar(JFileChooser fileC) {
		fileChooser = fileC;
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem loadItem = new JMenuItem("Load");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(saveItem);
		fileMenu.add(loadItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		add(fileMenu);
		setSaveAct(saveItem);
		setLoadAct(loadItem);
		setExitAct(exitItem);
		json = new Json(PlateFactory.getInstance());
	}

	private void setSaveAct(JMenuItem saveItem) {
		saveItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// try {
				// json.save();
				// System.exit(0);
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
			}
		});
	}

	private void setLoadAct(JMenuItem loadItem) {
		loadItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

		loadItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(fileChooser
						.getParent()) == JFileChooser.APPROVE_OPTION) {
					loadedFile = fileChooser.getSelectedFile();
//					json.load(loadedFile);
				}
			}
		});
	}

	private void setExitAct(JMenuItem exitItem) {
		exitItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
}