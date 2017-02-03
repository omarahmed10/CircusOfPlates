package control;

import models.Plate;

public class ProjectingPlate implements Runnable {
	private Plate currentPlate;
	private int height;
	private PlateFactory plateFactory;
	private volatile boolean suspended, inGame;
	private Thread t;

	public ProjectingPlate(Plate p, int height, PlateFactory plateFactory) {
		this.currentPlate = p;
		this.height = height;
		this.plateFactory = plateFactory;
		inGame = true;
	}

	@Override
	public void run() {
		projectCurrentPlate();
	}

	private synchronized void projectCurrentPlate() {

		while (inGame && !currentPlate.isCaptured()
				&& currentPlate.getCurrentPosition().y < height) {
			try {
				// give move here any number because offset isn't used in
				// projection.
				currentPlate.move(0);
				Thread.sleep(20);
				synchronized (this) {
					while (suspended) {
						this.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (inGame && !currentPlate.isCaptured()) {
			plateFactory.removePlate(currentPlate);
			plateFactory.addReusablePlate(currentPlate);
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "plateProjector");
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
