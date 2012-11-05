package generations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import automata.TransitionFunction;
import environment.Environment;

public class Generations {

	private static int neighborhoodSize = 7;
	private static int numberOfArenas = 409;
	// private static int numberOfContenders = 150;
	private static int numberOfContenders = 4;
	private static int numberOfSteps = 203;
	private static int numberOfGenerations = 400;
	private static int numberOfStates = 2;
	private static int sizeOfEnvironments = 101;

	private static List<List<Integer>> someStarters =
			Arrays.asList(Arrays.asList(1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1),
					      Arrays.asList(1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1),
					      Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1),
					      Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
	public static void main(String[] args) {
		Generations instance = new Generations();
		instance.populate(someStarters);
		for (int g = 0; g < numberOfGenerations; ++g) {
			instance.oneGeneration();
			System.out.print(instance);
		}
	}

	private int currentGeneration;
	private boolean dotProgress = false;

	private boolean justOne = false;

	private List<TransitionFunction> population;

	private Random random;

	private boolean showReproduction = false;

	public Generations() {
		this(new Random());
	}

	public Generations(Random random) {
		this.random = random;
		population = new ArrayList<TransitionFunction>();
		currentGeneration = 0;
		Environment.setSize(sizeOfEnvironments);
	}

	public void oneGeneration() {
		List<List<Integer>> arenas = new ArrayList<List<Integer>>();
		for (int arenaCount = 0; arenaCount < numberOfArenas; ++arenaCount)
			arenas.add(Environment.randomStates(random, numberOfStates));

		for (TransitionFunction program : population) {
			if (dotProgress) {
				System.out.print(".");
				System.out.flush();
			}
			Environment env = new Environment(program);
			double fitnessTotal = 0.0;
			for (List<Integer> arena : arenas) {
				int total = 0;
				for (Integer i : arena)
					total += i;
				int expected = (total > sizeOfEnvironments / 2) ? 1 : 0;
				fitnessTotal += env.run(arena, expected, numberOfSteps);
			}
			program.setFitness(fitnessTotal / numberOfArenas);
		}
		Collections.sort(population, Collections.reverseOrder());
		winnow();
		currentGeneration++;
	}

	public void populate(List<List<Integer>> programs) {
		while (population.size() < numberOfContenders)
			population.add(new TransitionFunction(programs.get(population.size()), neighborhoodSize,
					numberOfStates));
	}

	public void populate() {
		while (population.size() < numberOfContenders)
			population.add(new TransitionFunction(random, neighborhoodSize,
					numberOfStates));
	}

	private double totalPopulationScore() {
		double sum = 0.0;
		for (TransitionFunction program : population)
			sum += program.getFitness();
		return sum;
	}
	
	private int whichParent(double score) {
		double sum = 0.0;
		for (int i = 0; i < population.size(); ++i) {
			sum += population.get(i).getFitness();
			if (score < sum) return i;
		}
		return -1;
	}
	
	public void repopulate() {
		double range = totalPopulationScore();

		TransitionFunction.clearIdentical();
		while (population.size() < numberOfContenders) {
			int mama = whichParent(random.nextDouble() * range);
			int papa = whichParent(random.nextDouble() * range);
			if (mama == papa)
				continue;
			TransitionFunction offspringA = TransitionFunction.reproduce(random,
					population.get(mama), population.get(papa));
			TransitionFunction offspringB = TransitionFunction.reproduce(random,
					population.get(papa), population.get(mama));
			
			if (!population.contains(offspringA) && population.size() < numberOfContenders)
				population.add(offspringA);
			if (!population.contains(offspringB) && population.size() < numberOfContenders)
				population.add(offspringB);
			
			if (showReproduction) {
				System.out.println(String.format("  %s\n +%s\n =  %s",
						population.get(mama), population.get(papa),
						population.get(population.size() - 1)));
			}
		}
		System.out.println(String.format("Identical to a parent: %d", TransitionFunction.getIdentical()));
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("[%d]\n", currentGeneration));
		if (justOne) {
			buffer.append(population.get(0));
			buffer.append("\n");
		} else {
			for (TransitionFunction program : population) {
				buffer.append(program);
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

	public void winnow() {
		population = new ArrayList<TransitionFunction>(population.subList(0,
				numberOfContenders / 2));
		repopulate();
	}
}
