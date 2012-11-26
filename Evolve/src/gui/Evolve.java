package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import exceptions.NoWorldClassSpecified;
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
			throw new NoWorldClassSpecified(String.format("Class %s does not implement %s.", worldClass.getName(), worldInterfaceClass.getName()));
		worldClass = (Class<World>)provisionalWorldClass;
	}


	private Class<?> loadWorldClass() throws ClassNotFoundException {
	    ClassLoader classLoader = Evolve.class.getClassLoader();
	    return classLoader.loadClass(worldClassName);
	}

	public Class<World> getWorldClass() {
		return worldClass;
	}

	public World makeWorld(WorldParameters wp) {
		Constructor<World> worldMaker;
		try {
			worldMaker = worldClass.getConstructor();
			World world = worldMaker.newInstance();
			world.init(wp);
			world.addListener(face);
			return world;
		} catch (NoSuchMethodException | SecurityException e0) {
			e0.printStackTrace();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param loadFile
	 * @return
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
	public static void main(String[] args) throws ClassNotFoundException, Exception {
		if (args.length < 1)
			throw new NoWorldClassSpecified("Expected a world class name.");
		String worldClassName = args[0];
		Evolve instance = new Evolve(worldClassName);

		instance.run();
	}

}
