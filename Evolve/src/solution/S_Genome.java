package solution;

import java.util.List;

import livingObjects.ActionGene;
import livingObjects.Genome;

public class S_Genome implements Genome {
	private List<ActionGene> genes;
	private double metabolism;
	/**
	 * @param genes
	 * @param metabolism
	 * @param spawnThreshold
	 * @param actionGeneMutationChance
	 * @param metabolismMutationChance
	 * @param spawnThresholdMutationChance
	 */
	public S_Genome(int initialGeneration, List<ActionGene> genes, double metabolism,
			int spawnThreshold, double actionGeneMutationChance,
			double metabolismMutationChance, double spawnThresholdMutationChance) {
		this.genes = genes;
		this.metabolism = metabolism;
		this.spawnThreshold = spawnThreshold;
		this.actionGeneMutationChance = actionGeneMutationChance;
		this.metabolismMutationChance = metabolismMutationChance;
		this.spawnThresholdMutationChance = spawnThresholdMutationChance;
	}

	private int initialGeneration;
	private int spawnThreshold;
	private double actionGeneMutationChance;
	private double metabolismMutationChance;
	private double spawnThresholdMutationChance;

	@Override
	public double getActionGeneMutationChance() {
		return actionGeneMutationChance;
	}

	@Override
	public List<ActionGene> getActionGenes() {
		return genes;
	}

	@Override
	public int getInitialGeneration() {
		return initialGeneration;
	}

	@Override
	public double getMetabolism() {
		return metabolism;
	}

	@Override
	public double getMetabolismMutationChance() {
		return metabolismMutationChance;
	}

	@Override
	public int getSpawnThreshold() {
		return spawnThreshold;
	}

	@Override
	public double getSpawnThresholdMutationChance() {
		return spawnThresholdMutationChance;
	}

}
