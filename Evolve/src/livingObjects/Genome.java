package livingObjects;

import java.util.List;

/**
 * A species genome. This represents the three parts of a Species genome along
 * with the mutation chance and initial generation the genome appeared.
 *
 * @author blad
 */
public interface Genome {
	/**
	 * Get the ordered list of action genes
	 *
	 * @return list of ActionGene objects
	 */
	public List<ActionGene> getActionGenes();

	/**
	 * Get the first generation when this genome was associated with a living
	 * critter.
	 *
	 * @return first generation for this genome in the simulation
	 */
	public int getInitialGeneration();

	/**
	 * Get the metabolism rate of the animal. 0.0 < metabolism < 1.0
	 *
	 * @return metabolism rate of genome
	 */
	public double getMetabolism();

	/**
	 * Get probability of mutation happening when an animal with this genome
	 * reproduces.
	 *
	 * @return 0.0 <= chance <= 1.0 chance of mutation during reproduction
	 */
	public double getMutationChance();

	/**
	 * Get the energy threshold where an animal with this genome will reproduce
	 *
	 * @return spawn threshold energy
	 */
	public int getSpawnThreshold();
}
