package solution;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import livingObjects.Location;
import livingObjects.PlantInstance;
import livingObjects.SpeciesInstance;
import model.World;
import model.WorldListener;
import model.WorldParameters;

public class S_World implements World {
	private static final String SPECIES_FILE_EXTENSION = "SPC";
	private S_Species[][] grid;

	private int generation;

	WorldParameters worldParameters;
	List<S_Species> species;
	List<S_Plant> plants;

	List<S_SpeciesInstance> fauna;
	List<S_PlantInstance> flora;
	List<WorldListener> subscribers;

	@Override
	public void init(WorldParameters worldParameters)
			throws FileNotFoundException {
		this.worldParameters = worldParameters;

		grid = new S_Species[worldParameters.getHeight()][];
		for (int row = 0; row < worldParameters.getHeight(); ++row) {
			grid[row] = new S_Species[worldParameters.getWidth()];
			for (int column = 0; column < worldParameters.getHeight(); ++column) {
				grid[row][column] = null;
			}
		}

		generation = 0;
		subscribers = new ArrayList<WorldListener>();

		if (worldParameters.getVersion() == 1) {
			species = new ArrayList<S_Species>();
			plants = new ArrayList<S_Plant>();
			for (String name : worldParameters.getSpeciesNames()) {
				String speciesName = name + "." + SPECIES_FILE_EXTENSION;
				String worldSpeciesName = worldParameters.getBasePath() + "/" + speciesName;
				System.out.println(String.format("name: %s; speciesName: %s; worldSpeciesName: %s", name, speciesName, worldSpeciesName));
				File fin = new File(speciesName);
				if (!fin.exists()) {
					fin = new File(worldSpeciesName);
					if (!fin.exists())
						throw new FileNotFoundException("Could not find file "
								+ speciesName);
				}
				S_Species the_species = S_Species.loadSpecies(speciesName,
						new Scanner(fin), worldParameters);
				species.add(the_species);
			}
			S_Plant plant = new S_Plant();
			plant.setColor(Color.green);
			plant.setName("soybeans");
			plant.setFoodValue(worldParameters.getAnimalFoodValue());
			plants.add(plant);
		}


	}

	private boolean running = true;

	public synchronized boolean getRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}

	public synchronized void toggleRunning() {
		running = !running;
	}

	@Override
	public void run() {
		long oldTime = System.currentTimeMillis();
		boolean wasPaused = true;
		while (true) {
			if (getRunning()) {
				if (wasPaused) {
					wasPaused = false;
					oldTime = System.currentTimeMillis();
				}
				long currentTime = System.currentTimeMillis();
				long delta = currentTime - oldTime;
				if (delta > worldParameters.getProgramDelay()) {
					publish();
					++generation;
					oldTime = currentTime;
				} else {
					try {
						Thread.sleep(worldParameters.getProgramDelay() - delta);
					} catch (InterruptedException e) {
						// IGNORED: the loop and the if statement make this safe
					}
				}
			}
		}
	}

	Random r = new Random();
	@Override
	public void update(BufferedImage display) {
		Graphics d = display.getGraphics();
		d.setColor(Color.black);
		d.fillRect(0,  0,  display.getWidth(), display.getHeight());

		for (int i = 0; i < 100; ++i) {
			display.setRGB(r.nextInt(display.getWidth()),
					       r.nextInt(display.getHeight()), Color.red.getRGB() );
		}
	}

	@Override
	public void addListener(WorldListener listener) {
		subscribers.add(listener);
	}

	@Override
	public void removeListener(WorldListener listener) {
		subscribers.remove(listener);
	}

	private void publish() {
		for (WorldListener subscriber : subscribers)
			subscriber.next(this);
	}

	@Override
	public int getGeneration() {
		return generation;
	}

	@Override
	public int getPopulation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPopulation(String critterName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SpeciesInstance critterAt(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlantInstance plantAt(Location location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pause() {
		setRunning(false);
	}

	@Override
	public void unpause() {
		setRunning(true);
	}

	@Override
	public boolean paused() {
		return !running;
	}
}
