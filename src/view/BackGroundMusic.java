package view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class BackGroundMusic implements Runnable {

	private volatile boolean suspended;
	private Thread t;
	private javazoom.jl.player.Player musicPlayer;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			startSong();
			synchronized (this) {
				while (suspended) {
					System.out.println("Trying");
					this.wait();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void startSong() {
		try {
			String workingDir = System.getProperty("user.dir");
			String path = new File(
					workingDir + File.separator + "src" + File.separator
							+ "resources" + File.separator + "FallingUp.mp3")
									.getCanonicalPath();
			File music = new File(path);
			FileInputStream fI;
			fI = new FileInputStream(music);
			BufferedInputStream bI = new BufferedInputStream(fI);
			musicPlayer = new javazoom.jl.player.Player(bI);
			musicPlayer.play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void start() {
		if (t == null) {
			t = new Thread(this, "music");
			t.start();
		}
	}

	void suspend() {
		suspended = true;
	}

	void stop() {
		if (musicPlayer != null)
			musicPlayer.close();
	}

	synchronized void resume() {
		suspended = false;
		this.notify();
	}
}
