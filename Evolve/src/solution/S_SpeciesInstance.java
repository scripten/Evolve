package solution;

import livingObjects.Genome;
import livingObjects.Location;
import livingObjects.Species;
import livingObjects.SpeciesInstance;

public class S_SpeciesInstance implements SpeciesInstance {
	private Species species;
	private Location location;
	private Location facing;
	private int initialGeneration;
	private int energy;
	private int initialEnergy;

	public S_SpeciesInstance(Species species, int initialGeneration, int initialEnergy, Location location) {
		this.species = species;
		this.initialGeneration = initialGeneration;
		this.initialEnergy = initialEnergy;
		energy = initialEnergy;
		this.location = location;
	}

	public Location getFacing() {
		return facing;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Species getSpecies() {
		return species;
	}

	@Override
	public int getInitialGeneration() {
		return initialGeneration;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public boolean spawning() {
		Genome genes = species.getGenes();
		if(energy >= genes.getSpawnThreshold()) {
			energy = energy - initialEnergy;
			return true;
		} else
			return false;
	}
	
	public void move(Location location, Location facing) {
		this.location = location;
		this.facing = facing;
		energy = energy - (int)(this.getSpecies().getGenes().getMetabolism() / 2);
	}
}
