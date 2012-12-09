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
	private int[][] grid;

	private int generation;
	private int population;

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

		grid = new int[worldParameters.getWidth()][worldParameters.getHeight()];
		for (int x = 0; x < worldParameters.getWidth(); ++x) {
			for (int y = 0; y < worldParameters.getHeight(); ++y) {
				grid[x][y] = 0;
			}
		}

		generation = 0;
		population = 0;
		subscribers = new ArrayList<WorldListener>();
		Random r = new Random();

		if (worldParameters.getVersion() == 1) {
			species = new ArrayList<S_Species>();
			plants = new ArrayList<S_Plant>();
			for (String name : worldParameters.getSpeciesNames()) {
				String speciesName = name + "." + SPECIES_FILE_EXTENSION;
				String worldSpeciesName = worldParameters.getBasePath() + "\\" + speciesName;
				System.out.println(String.format("name: %s; speciesName: %s; worldSpeciesName: %s", name, speciesName, worldSpeciesName));
				File fin = new File(speciesName);
				if (!fin.exists()) {
					fin = new File(worldSpeciesName);
					if (!fin.exists())
						throw new FileNotFoundException("Could not find file "
								+ speciesName);
				}
				S_Species the_species = S_Species.loadSpecies(speciesName, name,
						new Scanner(fin), worldParameters);
				species.add(the_species);
			}
			S_Plant plant = new S_Plant();
			plant.setColor(Color.green);
			plant.setName("soybeans");
			plant.setFoodValue(worldParameters.getAnimalFoodValue());
			plants.add(plant);
			flora = new ArrayList<S_PlantInstance>();
			fauna = new ArrayList<S_SpeciesInstance>();
			for(S_Species curSpec : species) {
				for(int i = 0; i < worldParameters.getInitialSpeciesPopulation(); i++) {
					while(true){
						int x = r.nextInt(worldParameters.getWidth());
						int y = r.nextInt(worldParameters.getHeight());
						if(grid[x][y] == 0) {
							Location location = new Location();
							location.x = x;
							location.y = y;
							fauna.add(new S_SpeciesInstance(curSpec, generation, worldParameters.getInitialEnergy(), location));
							break;
						}
					}
				}
			}
			for(int i = 0; i < worldParameters.getMaximumNumberOfPlants() / 2; i++) {
				int x = r.nextInt(worldParameters.getWidth());
				int y = r.nextInt(worldParameters.getHeight());
				if(grid[x][y] == 0) {
					Location location = new Location();
					location.x = x;
					location.y = y;
					flora.add(new S_PlantInstance(plant, 0, location));
				}
				
			}
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

	@Override
	public void update(BufferedImage display) {
		Graphics d = display.getGraphics();
		d.setColor(Color.black);
		d.fillRect(0,  0,  display.getWidth(), display.getHeight());
		
		for(S_SpeciesInstance current : fauna) {
			if(current.spawning()){
				for(int x = 0; x < 3; x++) {
					for(int y = 0; y < 3; y++) {
						int x1 = current.getLocation().x + 1 - x;
						int y1 = current.getLocation().y + 1 - y;
						if(x1 < 0)
							x1 = (worldParameters.getWidth() + x1) - x;
						if(y1 < 0)
							y1 = (worldParameters.getHeight() + y1) - y;
						if(grid[x1][y1] == 0) {
							Location location = new Location();
							location.x = current.getLocation().x + 1 - x;
							location.y = current.getLocation().y + 1 - y;
							fauna.add(new S_SpeciesInstance(current.getSpecies(), generation, worldParameters.getInitialEnergy(), location));
						}
					}
				}
			}
			
			int [][] sensor = new int [9][9];
			for(int x = -4; x < 5; x++) {
				for(int y = -4; y < 5; y++) {
					int x1 = current.getLocation().x + x;
					int y1 = current.getLocation().y + y;
					if(x1 < 0)
						x1 = (worldParameters.getWidth() + x1) + x;
					if(y1 < 0)
						y1 = (worldParameters.getHeight() + y1) + y;
					if(x1 >= worldParameters.getWidth())
						x1 = x1 - worldParameters.getWidth();
					if(y1 >= worldParameters.getHeight())
						y1 = y1 - worldParameters.getHeight();
					sensor[x + 4][y + 4] = grid[x1][y1];
				}
			}
			
			Location location = new Location();
			Location facing = new Location();
			
			current.move(current.getLocation(), current.getLocation());
			
			if(current.getEnergy() == 0) {
				display.setRGB(current.getLocation().x,
						current.getLocation().y, Color.WHITE.getRGB());
				fauna.remove(current);
			} else {
				display.setRGB(current.getLocation().x,
					current.getLocation().y, current.getSpecies().getColor().getRGB());
			}
		}
		for(S_PlantInstance current : flora) {
			display.setRGB(current.getLocation().x,
					current.getLocation().y, Color.GREEN.getRGB());
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
		int count = 0;
		for(S_SpeciesInstance current : fauna)
			count++;
		return count;
	}

	@Override
	public int getPopulation(String critterName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SpeciesInstance critterAt(Location location) {
		for (S_SpeciesInstance current : fauna) {
			if(current.getLocation().equals(location))
				return current;
		}
		return null;
	}

	@Override
	public PlantInstance plantAt(Location location) {
		for (S_PlantInstance current : flora) {
			if(current.getLocation().equals(location))
				return current;
		}
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
