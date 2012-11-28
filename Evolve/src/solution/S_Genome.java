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
	public S_Genome(List<ActionGene> genes, double metabolism,
			int spawnThreshold, double actionGeneMutationChance,
			double metabolismMutationChance, double spawnThresholdMutationChance) {
		this.genes = genes;
		this.metabolism = metabolism;
		this.spawnThreshold = spawnThreshold;
		this.actionGeneMutationChance = actionGeneMutationChance;
		this.metabolismMutationChance = metabolismMutationChance;
		this.spawnThresholdMutationChance = spawnThresholdMutationChance;
	}

	private int spawnThreshold;
	private double actionGeneMutationChance;
	private double metabolismMutationChance;
	private double spawnThresholdMutationChance;

	@Override
	public double getActionGeneMutationChance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ActionGene> getActionGenes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInitialGeneration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMetabolism() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMetabolismMutationChance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpawnThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSpawnThresholdMutationChance() {
		// TODO Auto-generated method stub
		return 0;
	}

}
