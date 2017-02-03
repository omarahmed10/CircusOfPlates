package models;

import view.Observer;

public interface Observable {
	void addObserver(Observer obs);

	void notifyObserver();

	void move(int offset);
}
