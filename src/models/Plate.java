package models;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import view.Observer;

/**
 * this class must have a factory.
 * 
 * @author omar
 *
 */
public abstract class Plate implements models.Observable {

	private Point originPoint, currentPoint;
	private Rectangle plateBound;
	private java.awt.Color myColor;
	private boolean captured;
	private Observer myObserver;
	private int initialV, direction;
	private State stateIF;

	public Plate(int x, int y) {
		originPoint = new Point(x, y);
		currentPoint = new Point(x, y);
		captured = false;
	}

	public void setState(State stateIF) {
		this.stateIF = stateIF;
	}

	public Rectangle getBound() {
		return plateBound;
	}

	public int getHeight() {
		return getBound().height;
	}

	public int getWidth() {
		return getBound().width;
	}

	public void setBound(Rectangle bound) {
		plateBound = bound;
	}

	public Point getCurrentPosition() {
		return currentPoint;
	}

	public void setCurrentPosition(int x, int y) {
		currentPoint.setLocation(x, y);
	}

	public void setOriginalPosition(int x, int y) {
		originPoint.setLocation(x, y);
	}

	public Point getOriginPosition() {
		return originPoint;
	}

	public int getInitialVelocity() {
		return initialV;
	}

	public void setInitialVelocity(int v) {
		this.initialV = v;
	}

	public int getDirection() {
		return direction;
	}

	public void setColor(Color newColor) {
		myColor = newColor;
	}

	public Color getColor() {
		return myColor;
	}

	public State getState() {
		return stateIF;
	}

	public abstract void draw(Graphics g);

	public abstract void update();

	public void reset(int startX, int startY) {
		setCurrentPosition(startX, startY);
		update();
	}

	public void stop() {
		captured = true;
	}

	public boolean isCaptured() {
		return captured;
	}

	void project(int vi, int direction) {
		// this.projected = flag;
		// this.captured = false;
		this.initialV = vi;
		this.direction = direction;
	}

	public void displace(int offset) {
		currentPoint.x += getBound().width * offset;
		originPoint.x = currentPoint.x;
		update();
	}

	@Override
	public void move(int offset) {
		if (stateIF != null) {
			stateIF.move(offset);
			update();
			notifyObserver();
		} else {
			// state is not settled no action should be performed.
		}
	}

	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		myObserver = obs;
	}

	@Override
	public void notifyObserver() {
		if (myObserver != null) {
			myObserver.update();
		} else {
			// No observer is setted.
		}

	}

}
