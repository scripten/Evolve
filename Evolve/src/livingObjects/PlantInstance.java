package livingObjects;

/**
 * Represents and INSTANCE of a plant in the world.
 *
 * @author blad
 */
public interface PlantInstance extends LivingObjectInstance {
	/**
	 * Get the plant type represented by this instance.
	 *
	 * @return plant associated with this instance
	 */
	public Plant getPlant();
}
