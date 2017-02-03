package control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Plate;
import models.PlateIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlatePool {

	private ArrayList<Plate> reusablePlates;
	private static PlatePool instance = null;
	private final Logger logger;

	private PlatePool() {
		logger = LogManager.getLogger();
		reusablePlates = new ArrayList<Plate>();
	}// constructor

	public synchronized static PlatePool getInstance() {
		if (instance == null) {
			instance = new PlatePool();
		} // if
		return instance;
	}// method

	private synchronized boolean reusablePlatesExist() {
		if (reusablePlates.size() == 0) {
			return false;
		}
		return true;
	}// method

	public synchronized Plate getReusablePlate(int x, int y) {
		if (reusablePlatesExist()) {
			Plate p = reusablePlates.remove(0);
			p.reset(x, y);
			return p;
		}
		return null;
	}// method

	public synchronized void addReusablePlate(Plate reusablePlate) {
		logger.debug("Adding plate : " + reusablePlate + " to the reusable pool");
		reusablePlates.add(reusablePlate);
	}// method

	public List<Plate> getUsablePlates() {
		return reusablePlates;
	}

	public void setUsablePlates(List<Plate> list) {

	}

	public Iterator<Plate> createIterator() {
		return new PlateIterator(reusablePlates);
	}
}// class
