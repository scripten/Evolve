package livingObjects;

/**
 * Represents a single INSTANCE of a Species, a single animal.
 *
 * @author blad
 */
public interface SpeciesInstance extends LivingObjectInstance {
	/**
	 * Get the Species of which this is an instance.
	 *
	 * @return Species of this instance
	 */
	Species getSpecies();

	/**
	 * Get the generation when this instance was first born.
	 *
	 * @return generation of birth
	 */
	public int getInitialGeneration();
}
