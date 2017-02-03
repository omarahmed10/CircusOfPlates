package control;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import models.Plate;
import models.PlateIterator;
import models.PolygonPlate;
import view.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlateFactory {
	private static PlateFactory instance = null;
	private Plate currentPlate;
	private volatile List<Plate> newPlates;
	private Random plateGenerator;
	private Observer observer;
	private PlatePool platePool;
	private ArrayList<Class<Plate>> LoadedPlateClasses;
	private static Object lock = new Object();
	private PlateDynamicLoader plateDynamicLoader;
	private final Logger logger;

	/**
	 * 
	 * @param speed
	 *            the speed here describe the difficulty of the game
	 */
	private PlateFactory() {
		plateGenerator = new Random();
		newPlates = new LinkedList<Plate>();
		platePool = PlatePool.getInstance();
		plateDynamicLoader = PlateDynamicLoader.getInstance();
		LoadedPlateClasses = plateDynamicLoader.getLoadedClasses();
		logger = LogManager.getLogger();
	}

	public static PlateFactory getInstance() {
		synchronized (lock) {

			if (instance == null) {
				instance = new PlateFactory();
			}
			return instance;
		}
	}

	public void addObserver(Observer observer) {
		this.observer = observer;
	}

	public Plate createPlate(int x, int y) {
		synchronized (lock) {
			currentPlate = platePool.getReusablePlate(x, y);
			if (currentPlate == null) {
				try {
					currentPlate = createNewPlate(x, y);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			newPlates.add(currentPlate);

			return currentPlate;
		}
	}

	private Plate createNewPlate(int x, int y) throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		synchronized (lock) {
			Plate newPlate = null;
			if (LoadedPlateClasses == null || LoadedPlateClasses.size() == 0) {
				newPlate = new PolygonPlate(x, y);
				newPlate.addObserver(observer);
				newPlate.setColor(Colors.getColor());
			} else {
				int randomNumber = plateGenerator
						.nextInt(LoadedPlateClasses.size()) + 1;
				if (randomNumber < LoadedPlateClasses.size()) {
					Constructor<Plate> constructor = LoadedPlateClasses
							.get(randomNumber).getConstructor(
									new Class[] { int.class, int.class });
					newPlate = (Plate) constructor.newInstance(x, y);
					// obj.printHere("HelloWorld");
				} else {
					newPlate = new PolygonPlate(x, y);
					newPlate.addObserver(observer);
					newPlate.setColor(Colors.getColor());
				}
			}
			logger.info("New plate is created in position : " + "( " + x + " , " + y + " )");
			return newPlate;
		}
	}

	public Iterator<Plate> createIterator() {
		synchronized (lock) {
			return new PlateIterator(newPlates);
		}
	}

	public void removePlate(Plate p) {
		synchronized (lock) {
			logger.debug("Removing plate : " + p );
			newPlates.remove(p);
		}
	}

	public void addReusablePlate(Plate reusablePlate) {
		synchronized (lock) {
			platePool.addReusablePlate(reusablePlate);
		}
	}

	public List<Plate> getArray() {
		return newPlates;
	}

	public void loadClass(String classPath) {
		try {
			logger.debug("Loading a shape class dynamically from class path : " + classPath);

			plateDynamicLoader.loadPlateClass(classPath);
		} catch (ClassNotFoundException e) {
			logger.error("Dynamic loading failed, class not found in class path : " + classPath);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Dynamic loading failed, error eccored");
			e.printStackTrace();
		}
	}

	public void setLoadedPlates(List<Plate> createdList,
			List<Plate> reusableList) {
		newPlates = createdList;
		platePool.setUsablePlates(reusableList);
	}

	public List<Plate> getUsablePlate() {
		return platePool.getUsablePlates();
	}

	public String toString() {

		return newPlates.toString();

	}
}
