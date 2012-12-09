package solution;

import livingObjects.Location;
import livingObjects.Plant;
import livingObjects.PlantInstance;
import livingObjects.Species;

public class S_PlantInstance implements PlantInstance {
	private Plant plant;
	private Location location;
	private int initialGeneration;
	
	public S_PlantInstance(Plant plant, int initialGeneration, Location location) {
		this.plant = plant;
		this.initialGeneration = initialGeneration;
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Plant getPlant() {
		return plant;
	}

}
