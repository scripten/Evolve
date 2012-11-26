package model;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import livingObjects.Location;
import livingObjects.SpeciesInstance;
import livingObjects.PlantInstance;

/**
 * The interface of the world. This is what is used by the main application to
 * start and communicate with the running instance of the world.
 *
 * @author blad
 */
public interface World {
	/**
	 * Initialize the model
	 *
	 * @throws FileNotFoundException
	 *             if there is a problem finding any Species or Plant file
	 */
	public void init(WorldParameters worldParameters)
			throws FileNotFoundException;

	/**
	 * Called to start the simulation. It is assumed that the run() method will
	 * publish generational changes to all subscribed WorldListeners.
	 */
	public void run();

	/**
	 * Called by a generational listener to have the world update an image with
	 * the current locations of critters, plants, and, if appropriate,
	 * explosions.
	 *
	 * @param display
	 *            image with dimensions matching the world that the model world
	 *            can update for display.
	 */
	public void update(BufferedImage display);

	/**
	 * Pause the game; no more generations until the game is unpaused. Nothing
	 * happens if called on an already paused world.
	 */
	public void pause();

	/**
	 * Start running a paused game. Nothing happens if called on already running
	 * world.
	 */
	public void unpause();

	/**
	 * Get the paused status of the simulation
	 *
	 * @return true if game is paused; false otherwise
	 */
	public boolean paused();

	/**
	 * Get the current generation number.
	 *
	 * @return current generation number
	 */
	public int getGeneration();

	/**
	 * Get the current total population across all Species.
	 *
	 * @return total population
	 */
	public int getPopulation();

	/**
	 * Get the current population for the named Species.
	 *
	 * @param critterName
	 *            name of critter to return population for
	 * @return population of named critter if named critter is part of the
	 *         simulation; -1 if no such critter is known
	 */
	public int getPopulation(String critterName);

	/**
	 * Return the SpeciesInstance at the given world coordinates or null if
	 * there is no critter there.
	 *
	 * @param location
	 *            a Location in the world
	 * @return the species instance in that location (if there is one);
	 *         otherwise null
	 */
	public SpeciesInstance critterAt(Location location);

	/**
	 * Return the PlantInstance at the given world coordinates or null if there
	 * is no plant there.
	 *
	 * @param location
	 *            a Location in the world
	 * @return the plant instance in that location (if there is one); otherwise
	 *         null
	 */
	public PlantInstance plantAt(Location location);

	/**
	 * Add the given generational listener to the list of interested objects
	 * that want to be informed when a new generation is calculated.
	 *
	 * @param listener
	 *            the interested object to be informed of all future generation
	 *            changes
	 */
	public void addListener(WorldListener listener);

	/**
	 * Remove the given generational listener.
	 *
	 * @param listener
	 *            the no-longer-interested object
	 */
	public void removeListener(WorldListener listener);
}
