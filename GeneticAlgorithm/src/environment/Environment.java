package environment;

import java.util.ArrayList;
import java.util.List;

import automata.CA;
import automata.TransitionFunction;

public class Environment {
	private static int size = 0;

	public static int getSize() {
		return Environment.size;
	}

	public static void setSize(int size) {
		Environment.size = size;
	}

	private List<CA> currentGeneration;
	private List<CA> nextGeneration;
	private TransitionFunction program;

	public Environment(TransitionFunction program) {
		CA.setStateFunction(program);
		this.program = program;
		currentGeneration = populate();
		nextGeneration = populate();
	}

	public void calculateNextGeneration() {
		for (int i = 0; i < getSize(); ++i)
			nextGeneration.get(i).setState(currentGeneration.get(i).nextState());
	}
	
	public CA get(int index) {
		return currentGeneration.get(index);
	}

	public List<CA> getCurrentGeneration() {
		return currentGeneration;
	}

	public int getNeighborhoodSize() {
		return program.getNeighborhoodSize();
	}

	public int getNumberOfStates() {
		return program.getNumberOfStates();
	}

	public void nextGeneration() {
		List<CA> temp = currentGeneration;
		currentGeneration = nextGeneration;
		nextGeneration = temp;
	}

	public void advance(int generationCount) {
		for (int gen = 0; gen < generationCount; ++gen) {
			calculateNextGeneration();
			nextGeneration();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < getSize(); ++i)
			buffer.append(currentGeneration.get(i).getState());
		return buffer.toString();
	}
	
	private List<CA> populate() {
		List<CA> circularArray = new ArrayList<CA>();
		// fill circular array
		for (int i = 0; i < getSize(); ++i)
			circularArray.add(new CA(0));

		List<CA> neighborhood = new ArrayList<CA>();
		for (int n = 0; n < program.getNeighborhoodSize(); ++n)
			neighborhood.add(circularArray.get(n));

		// tail .. curr .. head
		// neighborhood of curr is from tail to head
		int tail = 0;
		int head = program.getNeighborhoodSize();
		int curr = head / 2;

		// for all elements, set neighborhood
		for (int i = 0; i < getSize(); ++i) {
			CA c = circularArray.get(curr);
			c.setNeighborhood(neighborhood);

			neighborhood.remove(0);
			neighborhood.add(circularArray.get(head));

			curr = (curr + 1) % getSize();
			head = (head + 1) % getSize();
		}
		return circularArray;
	}

	public void set(List<Integer> states) {
		if (states.size() != getSize())
			throw new IllegalArgumentException(String.format(
					"expected %d values for states, found %d", getSize(),
					states.size()));
		for (int i = 0; i < getSize(); ++i)
			currentGeneration.get(i).setState(states.get(i));
	}
}
