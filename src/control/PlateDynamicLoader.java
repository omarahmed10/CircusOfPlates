package control;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import models.Plate;

public class PlateDynamicLoader {

	private ArrayList<Class<Plate>> loadedPlateClasses;

	private static PlateDynamicLoader instance = null;

	private PlateDynamicLoader() {
		loadedPlateClasses = new ArrayList<Class<Plate>>();
	}// constructor

	public static PlateDynamicLoader getInstance() {
		if (instance == null) {
			instance = new PlateDynamicLoader();
		}
		return instance;
	}// method

	public void loadPlateClass(String pathToJar)
			throws IOException, ClassNotFoundException {

		@SuppressWarnings("resource")
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			if (je.isDirectory() || !je.getName().endsWith(".class")) {
				continue;
			}
			// -6 because of .class
			String className = je.getName().substring(0,
					je.getName().length() - 6);
			className = className.replace('/', '.');
			@SuppressWarnings("unchecked")
			Class<Plate> c = (Class<Plate>) cl.loadClass(className);
			loadedPlateClasses.add(c); // Adding the LoadedClass to the
										// Arraylist to be sent to the factory
		}
	}// loadPlateClass method

	public ArrayList<Class<Plate>> getLoadedClasses() {
		return loadedPlateClasses;
	}// method

}// class
