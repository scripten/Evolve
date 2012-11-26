package livingObjects;

/**
 * Represents an INSTANCE of a Species, an animal in the world.
 *
 * @author blad
 *
 */
public interface Species extends LivingObject {
	/**
	 * Get the current genome used by this critter.
	 *
	 * @return
	 */
	public Genome getGenes();
}
