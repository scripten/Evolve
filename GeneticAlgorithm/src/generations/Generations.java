package generations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import automata.TransitionFunction;
import environment.Environment;

public class Generations {

	private static int neighborhoodSize = 7;
	private static int numberOfArenas = 209;
	private static int numberOfContenders = 90;
	private static int numberOfSteps = 303;
	private static int numberOfGenerations = 400;
	private static int numberOfStates = 2;
	private static int sizeOfEnvironments = 101;

	public static void main(String[] args) {
		Generations instance = new Generations();
		instance.populate();
		for (int g = 0; g < numberOfGenerations; ++g) {
			instance.oneGeneration();
			System.out.println(instance);
		}
	}

	private int currentGeneration;
	private boolean dotProgress = false;

	private boolean justOne = true;

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
		repopulate();
		currentGeneration++;
	}

	public void populate() {
		while (population.size() < numberOfContenders)
			population.add(new TransitionFunction(random, neighborhoodSize,
					numberOfStates));
	}

	public void repopulate() {
		int range = population.size();
		while (population.size() < numberOfContenders) {
			int mama = random.nextInt(range);
			int papa = random.nextInt(range);
			if (mama == papa)
				continue;
			population.add(TransitionFunction.reproduce(random,
					population.get(mama), population.get(papa)));
			if (showReproduction) {
				System.out.println(String.format("  %s\n +%s\n =  %s",
						population.get(mama), population.get(papa),
						population.get(population.size() - 1)));
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("[%d] ", currentGeneration));
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
