package model;

import static java.awt.Color.blue;
import static java.awt.Color.cyan;
import static java.awt.Color.darkGray;
import static java.awt.Color.gray;
import static java.awt.Color.green;
import static java.awt.Color.lightGray;
import static java.awt.Color.magenta;
import static java.awt.Color.orange;
import static java.awt.Color.pink;
import static java.awt.Color.red;
import static java.awt.Color.yellow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Collection of world parameters for an Evolve world: these are the pieces of
 * the world that are not set directly in a species or plant: the random number
 * generator, the sound/animation settings, and the game delay. This is also
 * where the colors for the critters are kept since that is a property of the
 * simulation and not a property of the critter or plant.
 *
 * @author blad
 */
public class WorldParameters {
	/**
	 * Since plant patterns are really an enumeration (random, single, or double
	 * source) encoded as integers, this class exposes that.
	 *
	 * @author blad
	 *
	 */
	public enum PlantPattern {
		/**
		 * The three values known to version 1.0 worlds.
		 */
		RandomGrowth, SingleSource, DualSource;

		/**
		 * Holds all the values in ordered array. Makes it easy to convert from
		 * an int (in a world file) into this type. Constructing the array is a
		 * bit costly so this caches the value.
		 */
		private static PlantPattern[] patternsFromInt = null;

		/**
		 * Converts an integer to an enum. NO range checking done.
		 *
		 * @param p
		 *            value to convert to a plant pattern
		 * @return plant pattern corresponding to the given value
		 */
		public static PlantPattern fromInt(int p) {
			if (patternsFromInt == null)
				patternsFromInt = PlantPattern.values();
			return patternsFromInt[p];
		}
	};

	/**
	 * Number of rows of species in a version 1 world (.EVO) file
	 */
	public static final int VERSION_1_SPECIES_COUNT = 12;

	/**
	 * Default colors assigned to each animal type when it is created.
	 */
	public static final Color[] DEFAULT_SPECIES_COLORS = { blue, cyan,
			darkGray, gray, lightGray, magenta, orange, pink, red };

	/**
	 * Default colors assigned to each plant type when it is created.
	 */
	public static final Color[] DEFAULT_PLANT_COLORS = { green, yellow };

	public static Color[] getDefaultPlantColors() {
		return DEFAULT_PLANT_COLORS;
	}

	/**
	 * Sound effects are on if this bit is set in soundSettings.
	 */
	public static final int SOUND_EFFECTS = 1;

	/**
	 * Music is on if this bit is set in soundSettings
	 */
	public static final int SOUND_MUSIC = 2;

	/**
	 * Default soundSettings: all off
	 */
	public static final int SOUND_DEFAULT = 0;

	/**
	 * Explosive death animations used if this bit is set in animationSettings
	 */
	public static final int ANIMATION_EXPLOSION = 1;

	/**
	 * Default animationSettings: all on
	 */
	public static final int ANIMATION_DEFAULT = ANIMATION_EXPLOSION;

	/**
	 * Default world delay value
	 */
	public static final int DELAY_DEFAULT = 200;

	/**
	 * Default width of the playing field
	 */
	public static final int WIDTH_DEFAULT = 540;

	/**
	 * Default height of the playing field
	 */
	public static final int HEIGHT_DEFAULT = 370;

	/**
	 * Base path from which this world was loaded
	 */
	private String basePath;

	/**
	 * The seed used to start the random number generator. Value of 0L means
	 * that the generator is constructed with no seed as in Random().
	 */
	private long randomSeed;
	/**
	 * A bit field holding bits for what animations are played.
	 */
	private int animationSettings;
	/**
	 * A bit field holding bits for what sound effects are to be played.
	 */
	private int soundSettings;
	/**
	 * Delay factor for the program; lower number means faster running program.
	 */
	private int programDelay;

	/**
	 * Should the random seed be ignored?
	 */
	private boolean ignoreRandomSeed;

	/**
	 * Random number generator; to permit rerunning of "known" games, this
	 * should be the only place random numbers are drawn from.
	 */
	private final Random random;

	private int version;

	/**
	 * Get the version of the underlying data file (or the file that would be
	 * saved)
	 *
	 * @return the version (1 or 2)
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Set the version number
	 *
	 * @param version
	 *            new version number
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Plant and animal environment values used in version 1.0 worlds. They are
	 * read and provided to the world initialization code for 1.0 worlds; they
	 * permit mimicing the version 2.0 interfaces for plants and animals.
	 */
	private int plantFoodValue;
	private int plantSproutRate;
	private int maximumNumberOfPlants;
	private int plantLife;
	private PlantPattern plantPattern;
	private int initialSpeciesPopulation;
	private int maximumNumberOfAnimals;
	private int animalFoodValue;
	private int initialEnergy;
	private int spawnThreshold;

	/**
	 * Size of the world
	 */
	private int width;
	private int height;

	/**
	 * Map associating colors with species names; used for loading the species
	 * files by the model
	 */
	private final Map<String, Color> speciesColorMap;

	/**
	 * Map associating colors with plant names; used for loading/initializing
	 * plants.
	 */
	private final Map<String, Color> plantColorMap;

	/**
	 * List of change subscribers to be informed if there are changes.
	 */
	private final List<WorldParametersChangeListener> subscribers;

	/**
	 * Construct a set of WorldParameters with the default values. Note that
	 * this method causes the created Random instance to use the default seed
	 * (determined by Java and the clock).
	 */
	public WorldParameters() {
		this(WIDTH_DEFAULT, HEIGHT_DEFAULT, true, 0L, ANIMATION_DEFAULT,
				SOUND_DEFAULT, DELAY_DEFAULT);
	}

	/**
	 * Construct an instance of the given WorldParameters.
	 *
	 * @param width
	 *            width of the world
	 * @param height
	 *            height of the world
	 * @param ignoreRandomSeed
	 *            is random seed ignored?
	 * @param randomSeed
	 *            seed for random number generator; if randomSeed is 0L, then
	 *            default Java seeding is used.
	 * @param animationSettings
	 *            bit field for what animations to show
	 * @param soundSettings
	 *            bit field for what sounds to play
	 * @param programDelay
	 *            program delay value; lower value = faster game down to 0
	 *            (should be >= 0).
	 */
	public WorldParameters(int width, int height, boolean ignoreRandomSeed,
			long randomSeed, int animationSettings, int soundSettings,
			int programDelay) {
		this.width = width;
		this.height = height;
		this.ignoreRandomSeed = ignoreRandomSeed;
		this.randomSeed = randomSeed;
		this.soundSettings = soundSettings;
		this.programDelay = programDelay;
		this.animationSettings = animationSettings;
		this.random = (!this.ignoreRandomSeed()) ? (new Random())
				: (new Random(this.randomSeed));
		this.subscribers = new ArrayList<WorldParametersChangeListener>();
		this.plantColorMap = new HashMap<String, Color>();
		this.speciesColorMap = new HashMap<String, Color>();
	}

	/**
	 * Get the base path from which the world was loaded.
	 *
	 * @return string of the path
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * Set the path
	 *
	 * @param basePath
	 *            new value for the base path
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * Get the color associated with a given species name.
	 *
	 * @param name
	 *            species/plant name to match to a color
	 * @return the color associated with the name or null if there is no such
	 *         association
	 */
	public Color getColor(String name) {
		if (speciesColorMap.containsKey(name))
			return speciesColorMap.get(name);
		if (plantColorMap.containsKey(name))
			return plantColorMap.get(name);
		return null;
	}

	/**
	 * Associate (in the load list) a species name with a color.
	 *
	 * @param critterName
	 *            name to load
	 * @param color
	 *            display color for species
	 */
	public void associateSpeciesNameWithColor(String critterName, Color color) {
		speciesColorMap.put(critterName, color);
	}

	/**
	 * Add the given species to the world with the next default color
	 *
	 * @param animal
	 *            the species object
	 */
	public void associateSpeciesNameWithColor(String animal) {
		associateSpeciesNameWithColor(animal,
				DEFAULT_SPECIES_COLORS[speciesColorMap.size()
						% DEFAULT_SPECIES_COLORS.length]);
	}

	/**
	 * Associate (in the load list) a plant name with a color.
	 *
	 * @param critterName
	 *            name to load
	 * @param color
	 *            display color for Plant
	 */
	public void associatePlantNameWithColor(String critterName, Color color) {
		plantColorMap.put(critterName, color);
	}

	/**
	 * Add the given plant to the world with the next default color
	 *
	 * @param animal
	 *            the Plant object
	 */
	public void associatePlantNameWithColor(String animal) {
		associatePlantNameWithColor(animal,
				DEFAULT_PLANT_COLORS[plantColorMap.size()
						% DEFAULT_PLANT_COLORS.length]);
	}

	/**
	 * Get the map of species to color mapping.
	 *
	 * @return
	 */
	public Map<String, Color> speciesNameMap() {
		return speciesColorMap;
	}

	/**
	 * Get the map of plant to color mapping.
	 *
	 * @return
	 */
	public Map<String, Color> plantColorMap() {
		return plantColorMap;
	}

	/**
	 * Get a reference to the world's random number generator
	 *
	 * @return random number generator
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Get the current animation settings
	 *
	 * @return current animation settings
	 */
	public int getAnimationSettings() {
		return animationSettings;
	}

	/**
	 * Set the current animation settings
	 *
	 * @param animationSettings
	 *            new animation settings as a bit field
	 */
	public void setAnimationSettings(int animationSettings) {
		this.animationSettings = animationSettings;
		publish();
	}

	/**
	 * Get the current sound settings
	 *
	 * @return current sound settings
	 */
	public int getSoundSettings() {
		return soundSettings;
	}

	/**
	 * Set the current sound settings
	 *
	 * @param soundSettings
	 *            new sound settings as a bit field
	 */
	public void setSoundSettings(int soundSettings) {
		this.soundSettings = soundSettings;
		publish();
	}

	/**
	 * Get the current delay settings
	 *
	 * @return current delay settings
	 */
	public int getProgramDelay() {
		return programDelay;
	}

	/**
	 * Change current delay settings
	 *
	 * @param programDelay
	 *            new delay settings
	 */
	public void setProgramDelay(int programDelay) {
		this.programDelay = programDelay;
		publish();
	}

	/**
	 * Get the value of the ignoreRandomSeed flag
	 *
	 * @return is the world to ignore the set random seed?
	 */
	public boolean ignoreRandomSeed() {
		return ignoreRandomSeed;
	}

	/**
	 * Reset and return the new value of the ignoreRandomSeed flag
	 *
	 * @param newIgnore
	 *            the new value
	 * @return the value _after_ setting
	 */
	public boolean ignoreRandomSeed(boolean newIgnore) {
		ignoreRandomSeed = newIgnore;
		return ignoreRandomSeed;
	}

	/**
	 * Get the width of the world
	 *
	 * @return width in cells (or pixels)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the world
	 *
	 * @return height in cells (or pixels)
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the world's plantFood value; valid value for a version 1.0 world. Not
	 * used by the display routines
	 *
	 * @return energy gained by eating a plant in the current world
	 */
	public int getPlantFoodValue() {
		return plantFoodValue;
	}

	/**
	 * Get the world's sprout rate; valid value for version 1.0 worlds. Display
	 * routines do not use this value.
	 *
	 * @return plant sprout rate (guess it in in percentage)
	 */
	public int getPlantSproutRate() {
		return plantSproutRate;
	}

	/**
	 * Get maximum number of plants allowed to be alive in the world. Valid for
	 * version 1.0 games and not used by display.
	 *
	 * @return max plant population in world
	 */
	public int getMaximumNumberOfPlants() {
		return maximumNumberOfPlants;
	}

	/**
	 * Length of plant life in generations. Valid in version 1.0 worlds and not
	 * used by display.
	 *
	 * @return plant lifespan in generations
	 */
	public int getPlantLife() {
		return plantLife;
	}

	/**
	 * Get the plant pattern described in the file.
	 *
	 * @return plant spawning pattern
	 */
	public PlantPattern getPlantPattern() {
		return plantPattern;
	}

	/**
	 * Get initial number of each critter in the world.
	 *
	 * @return starting population (same for all creatures)
	 */
	public int getInitialSpeciesPopulation() {
		return initialSpeciesPopulation;
	}

	/**
	 * Get maximum population overall. Given wording, it appears to be a global
	 * cap (on the sum of the populations of all critters)
	 *
	 * @return max population
	 */
	public int getMaximumNumberOfAnimals() {
		return maximumNumberOfAnimals;
	}

	/**
	 * Get the food value of any critter. Version 1.0 value not used by display
	 * code.
	 *
	 * @return energy produced by consuming any animal.
	 */
	public int getAnimalFoodValue() {
		return animalFoodValue;
	}

	/**
	 * Energy each critter is spawned with.
	 *
	 * @return initial spawning energy
	 */
	public int getInitialEnergy() {
		return initialEnergy;
	}

	/**
	 * Spawn energy cost for any creature
	 *
	 * @return cost in energy of reproduction
	 */
	public int getSpawnThreshold() {
		return spawnThreshold;
	}

	/**
	 * Get the set of names of species as loaded in this world.
	 *
	 * @return set of string names
	 */
	public Set<String> getSpeciesNames() {
		return speciesColorMap.keySet();
	}

	/**
	 * Get the original random seed used to create this world
	 *
	 * @return random seed used to start the world.
	 */
	public long getRandomSeed() {
		return randomSeed;
	}

	/**
	 * Reverse engineered format of Evolve! Lite world files.
	 *
	 * <pre>
	 * Line 1 (read before this method is called) "ver 1.0"
	 * Lines 2-13 Y<SPECIES> or N (yes or no creature, creature name).
	 * Line 14 plant food value
	 * Line 15 plant sprout Line
	 * 16 plant max Line
	 * 17 plant life
	 * Line 18 =0
	 * Line 19 =0
	 * Line 20 plant pattern 0 random, 1,2 number of groups
	 * Line 21 initial species population
	 * Line 22 max total animal population
	 * Line 23 animal food energy
	 * Line 24 initial energy
	 * Line 25 spawning threshold
	 * Line 26 =3
	 * Line 27 =0
	 * Line 28 =8
	 * Line 29 random on/off
	 * Line 30 random seed
	 * Line 31 =0
	 * Line 32 =0
	 * Line 33 - 46 Lab notebook
	 * </pre>
	 *
	 * @param fin
	 *            Scanner opened onto a file of the version 1.0 format
	 */
	@SuppressWarnings("resource")
	private static WorldParameters version10(Scanner fin) {
		WorldParameters v1 = new WorldParameters();

		v1.setVersion(1);

		for (int i = 0; i < VERSION_1_SPECIES_COUNT; ++i) {
			String speciesLine = fin.nextLine();
			if (speciesLine.startsWith("Y")) {
				String animalName = speciesLine.substring(1);
				v1.associateSpeciesNameWithColor(animalName);
			}
		}
		// line read and line scanner
		String line;
		Scanner sin;

		line = fin.nextLine();
		sin = new Scanner(line);
		v1.plantFoodValue = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.plantSproutRate = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.maximumNumberOfPlants = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.plantLife = sin.nextInt();

		for (int j = 18; j < 20; ++j)
			fin.nextLine(); // skip 18-19

		line = fin.nextLine();
		sin = new Scanner(line);
		v1.plantPattern = PlantPattern.fromInt(sin.nextInt());

		line = fin.nextLine();
		sin = new Scanner(line);
		v1.initialSpeciesPopulation = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.maximumNumberOfAnimals = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.animalFoodValue = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.initialEnergy = sin.nextInt();
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.spawnThreshold = sin.nextInt();

		for (int j = 26; j < 29; ++j)
			fin.nextLine(); // skip 26-28

		line = fin.nextLine();
		sin = new Scanner(line);
		v1.ignoreRandomSeed = sin.nextInt() != 0;
		line = fin.nextLine();
		sin = new Scanner(line);
		v1.randomSeed = sin.nextInt();

		// ignore the lab notebook for the moment

		return v1;
	}

	private static WorldParameters version20(Scanner fin) {
		WorldParameters v2 = new WorldParameters();
		v2.setVersion(2);

		return v2;
	}

	/**
	 * Create a WorldParameters object from a .EVO file.
	 *
	 * @param fin
	 *            scanner opened on a file of the right format
	 * @return an instantiated WorldParameters if all goes well, null otherwise
	 */
	public static WorldParameters load(Scanner fin) {
		String line = fin.nextLine();
		if (line.equals("ver 1.0"))
			return version10(fin);
		else if (line.equals("ver 2.0"))
			return version20(fin);
		return null;
	}

	/*
	 * WorldParameterChangeListener handling code. Set methods should use
	 * publish to publish changes they make.
	 */

	/**
	 * Add a new listener for changes.
	 *
	 * @param newbie
	 *            the listener
	 */
	public void add(WorldParametersChangeListener newbie) {
		subscribers.add(newbie);
	}

	/**
	 * Remove the listener from the subscriber list
	 *
	 * @param gone
	 *            listener to remove
	 */
	public void remove(WorldParametersChangeListener gone) {
		subscribers.remove(gone);
	}

	/**
	 * Publish changes to all of the subscribers.
	 */
	public void publish() {
		for (WorldParametersChangeListener subscriber : subscribers)
			subscriber.change(this);
	}
}
