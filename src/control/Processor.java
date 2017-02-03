package control;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import models.Plate;
import models.PlateProjected;
import view.Board;

public class Processor {
	private BlockingQueue<Plate> largeShelfQueue = new ArrayBlockingQueue<>(3);
	private BlockingQueue<Plate> smallShelfQueue = new ArrayBlockingQueue<>(2);
	private PlateFactory plateFactory;
	private Board mainBoard;
	private Random random;
	private int offsetDirection;
	private Strategy strategyIF;
	private LinkedList<ProjectingPlate> threadsList;

	public Processor(PlateFactory plateFactory, Board mainBoard,
			int offseDirection, Strategy IStrategy) {
		this.plateFactory = plateFactory;
		this.mainBoard = mainBoard;
		random = new Random();
		this.offsetDirection = offseDirection;
		this.strategyIF = IStrategy;
		threadsList = new LinkedList<>();
	}

	public void produce(int x1, int y1, int x2, int y2)
			throws InterruptedException {

		Plate createdPlate1 = null, createdPlate2 = null;
		if (smallShelfQueue.remainingCapacity() > 0) {
			displaceAllPlates(smallShelfQueue);
		}
		if (smallShelfQueue.remainingCapacity() > 0) {
			Thread.sleep(strategyIF.getSpeed());
			createdPlate2 = plateFactory.createPlate(x2, y2);
			smallShelfQueue.put(createdPlate2);
		}
		mainBoard.repaint();
		// give the mainBoard thread time to finish its work.
		Thread.sleep(50);
		if (largeShelfQueue.remainingCapacity() > 0) {
			displaceAllPlates(largeShelfQueue);
		}
		if (largeShelfQueue.remainingCapacity() > 0) {
			Thread.sleep(strategyIF.getSpeed());
			createdPlate1 = plateFactory.createPlate(x1, y1);
			largeShelfQueue.put(createdPlate1);
		}
		mainBoard.repaint();
		// give the mainBoard thread time to finish its work.
		Thread.sleep(50);
	}

	private void displaceAllPlates(BlockingQueue<Plate> queue) {
		Iterator<Plate> it = queue.iterator();
		while (it.hasNext()) {
			Plate p = it.next();
			p.displace(offsetDirection);
		}
	}

	public void project() throws InterruptedException {
		// System.out.println("i am in");
		Plate p = null, p2 = null;
		if (smallShelfQueue.remainingCapacity() == 0) {
			p = smallShelfQueue.take();
			startProjecting(p);
		}
		if (largeShelfQueue.remainingCapacity() == 0) {
			p2 = largeShelfQueue.take();
			startProjecting(p2);
		}
		Thread.sleep(strategyIF.getSpeed());
	}

	public void stopProjecting() {
		for (int i = 0; i < threadsList.size(); i++) {
			threadsList.get(i).suspend();
		}
	}

	public void stopGame() {
		for (int i = 0; i < threadsList.size(); i++) {
			threadsList.get(i).stop();
		}
	}

	public void resumeProjecting() {
		for (int i = 0; i < threadsList.size(); i++) {
			threadsList.get(i).resume();
		}
	}

	private void startProjecting(Plate p) throws InterruptedException {
		int speed = random.nextInt(30) + 20;
		p.setState(new PlateProjected(p, speed, offsetDirection));
		ProjectingPlate pP = new ProjectingPlate(p, mainBoard.getHeight(),
				plateFactory);
		pP.start();
		threadsList.add(pP);

	}

}
