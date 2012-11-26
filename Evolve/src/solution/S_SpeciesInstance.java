package solution;

import livingObjects.Location;
import livingObjects.Species;
import livingObjects.SpeciesInstance;

public class S_SpeciesInstance implements SpeciesInstance {
	private Species species;
	private Location location;
	private Location facing;
	private int initialGeneration;

	public S_SpeciesInstance(Species species, int initialGeneration) {
		this.species = species;
		this.initialGeneration = initialGeneration;
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
}
