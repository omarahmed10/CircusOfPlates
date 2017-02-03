package control;

public class PlateConsumer implements Runnable {
	private Processor processor;
	private volatile boolean suspended,inGame;
	private Thread t;

	public PlateConsumer(Processor processor) {
		this.processor = processor;
		inGame = true;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (inGame) {
			try {
				processor.project();
				synchronized (this) {
					while (suspended) {
						this.wait();
					}
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "plateConsumer");
			t.start();
		}
	}

	public void suspend() {
		suspended = true;
		processor.stopProjecting();
	}

	public void stop() {
		inGame = false;
	}

	public synchronized void resume() {
		suspended = false;
		processor.resumeProjecting();
		this.notify();
	}
}
