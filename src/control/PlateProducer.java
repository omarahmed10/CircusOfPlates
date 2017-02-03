package control;

public class PlateProducer implements Runnable {
	private Processor processor;
	private int lSHeight, sSHeight, lSStartPoint, sSStartPoint;
	private volatile boolean suspended, inGame;
	private Thread t;

	public PlateProducer(Processor processor, int largeShelfStartPoint,
			int largeShelfHeight, int smallShelfStartPoint,
			int smallShelfHeight) {
		this.processor = processor;
		lSHeight = largeShelfHeight;
		sSHeight = smallShelfHeight;
		lSStartPoint = largeShelfStartPoint;
		sSStartPoint = smallShelfStartPoint;
		inGame = true;
	}

	@Override
	public void run() {
		while (inGame) {
			try {
				processor.produce(lSStartPoint, lSHeight, sSStartPoint,
						sSHeight);
				synchronized (this) {
					while (suspended) {
						this.wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "plateProducer");
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
		notify();
	}

}
