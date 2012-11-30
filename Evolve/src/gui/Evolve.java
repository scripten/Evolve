package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import gui.exceptions.NoWorldClassSpecified;
import model.World;
import model.WorldParameters;

public class Evolve {
	public static String EVOLVE_FILE_EXTENSION = "EVO";

	private String worldClassName;
	private Class<World> worldClass;
	private EvolveInterface face;
	private World currentWorld;

	@SuppressWarnings("unchecked")
	public Evolve(String worldClassName) throws Exception {
		this.currentWorld = null;
		this.worldClassName = worldClassName;
		Class<?> provisionalWorldClass = loadWorldClass();
		Class<World> worldInterfaceClass = World.class;
		if (!worldInterfaceClass.isAssignableFrom(provisionalWorldClass))
			throw new NoWorldClassSpecified(String.format(
					"Class %s does not implement %s.", worldClass.getName(),
					worldInterfaceClass.getName()));
		worldClass = (Class<World>) provisionalWorldClass;
	}

	private Class<?> loadWorldClass() throws ClassNotFoundException {
		ClassLoader classLoader = Evolve.class.getClassLoader();
		return classLoader.loadClass(worldClassName);
	}

	/**
	 * Get the worldClass stored by this application
	 *
	 * @return the worldClass specified when running this application.
	 */
	public Class<World> getWorldClass() {
		return worldClass;
	}

	/**
	 * Construct a World object (as specified on the commandline) using the given worldParameters.
	 * @param wp world parameters to use in initializing the world
	 * @return the initizlized world object if all went well; null otherwise
	 */
	public World makeWorld(WorldParameters wp) {
		Constructor<World> worldMaker;
			try {
				worldMaker = worldClass.getConstructor();
				World world = worldMaker.newInstance();
				world.init(wp);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return null;
	}

	/**
	 * Load a WorldParameters from a file. Returns the loaded parameters object
	 * or it returns null (if there was a problem)
	 *
	 * @param loadFile
	 *            file from which to load
	 * @return a filled-in WorldParameters object from the file if there were no
	 *         problems; null otherwise
	 */
	public WorldParameters load(File loadFile) {
		try {
			Scanner s = new Scanner(loadFile);
			WorldParameters world = WorldParameters.load(s);
			world.setBasePath(loadFile.getParent());
			return world;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	synchronized public void runWorld(World current) {
		this.currentWorld = current;
	}

	synchronized public World getCurrentWorld() {
		World world = this.currentWorld;
		this.currentWorld = null;
		return world;
	}

	public void run() {
		face = new EvolveInterface(this);
		face.setVisible(true);
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			World newWorld = getCurrentWorld();
			if (newWorld != null) {
				newWorld.run();
			}
		}
	}

	/**
	 *
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			Exception {
		if (args.length < 1)
			throw new NoWorldClassSpecified("Expected a world class name.");
		String worldClassName = args[0];
		Evolve instance = new Evolve(worldClassName);

		instance.run();
	}

}
