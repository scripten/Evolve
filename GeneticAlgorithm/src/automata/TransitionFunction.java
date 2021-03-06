package automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Cellular automaton transition function described by a sequence of result
 * state values.
 * 
 * @author blad
 */
public class TransitionFunction implements Comparable<TransitionFunction> {
	public static double mutationChance = 0.5;
	private static int monitorReproduction = 1;
	private static int identical;
	
	public static int getMonitorReproduction() {
		return monitorReproduction;
	}

	public static void setMonitorReproduction(int monitorReproduction) {
		TransitionFunction.monitorReproduction = monitorReproduction;
	}

	public static int getIdentical() {
		return identical;
	}

	public static void clearIdentical() {
		TransitionFunction.identical = 0;
	}

	private List<Integer> function;
	// size of the neighborhood (how many CA are used to index the table)
	private int neighborhoodSize;
	// unique states; base to interpret neighborhood as an integer
	private int numberOfStates;

	private double fitness;
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Get the neighborhood size used to construct this function.
	 * 
	 * @return neighborhoodSize
	 */
	public int getNeighborhoodSize() {
		return neighborhoodSize;
	}

	/**
	 * Return the number of states used to construct this function
	 * 
	 * @return numberOfStates
	 */
	public int getNumberOfStates() {
		return numberOfStates;
	}

	/**
	 * Given neighborhoodSize and numberOfStates, number of entries in the
	 * function.
	 * 
	 * @param neighborhoodSize
	 *            elements in neighborhood
	 * @param numberOfStates
	 *            number of possible values for elements
	 * @return numberOfStates ^ neighborhoodSize
	 */
	static public int calculateStateFunctionSize(int neighborhoodSize,
			int numberOfStates) {
		int result = 1;
		for (int i = 0; i < neighborhoodSize; ++i, result *= numberOfStates)
			;
		return result;
	}

	/**
	 * Number of entries in a constructed transition function.
	 * 
	 * @return calculateStateFunctionSize for the objects neighborhoodSize and
	 *         numberOfStates
	 */
	public int getStateFunctionSize() {
		return calculateStateFunctionSize(neighborhoodSize, numberOfStates);
	}

	/**
	 * Use the neighborhood to calculate an index into the function table.
	 * 
	 * @param neighborhood
	 *            collection of state providers; treated as digits of a base
	 *            numberOfStates number
	 * @return the state stored at the given location
	 */
	public int get(List<CA> neighborhood) {
		if (neighborhood == null)
			throw new IllegalStateException(
					String.format("neighborhood == null; cannot calculate ndx of next state"));
		int ndx = 0;
		for (int i = 0; i < getNeighborhoodSize(); ++i)
			ndx = getNumberOfStates() * ndx + neighborhood.get(i).getState();
		// index into the table
		return get(ndx);
	}

	/**
	 * Get the state value at a given index in the function.
	 * 
	 * @param index
	 *            the number of the entry in the function
	 * @return the state value recorded at the given location
	 */
	public int get(int index) {
		return function.get(index);
	}

	/**
	 * Construct a TransitionFunction with the given table as the value and the
	 * given dimensions
	 * 
	 * @param function
	 *            table of values
	 * @param neighborhoodSize
	 *            elements in a neighborhood
	 * @param numberOfStates
	 *            number of states any element could have
	 */
	public TransitionFunction(List<Integer> function, int neighborhoodSize,
			int numberOfStates) {
		if (neighborhoodSize == 0)
			throw new IllegalArgumentException("neighborhoodSize cannot be 0");
		if (numberOfStates == 0)
			throw new IllegalArgumentException("numberOfStates cannot be 0");
		if (function.size() != calculateStateFunctionSize(neighborhoodSize,
				numberOfStates))
			throw new IllegalArgumentException(
					String.format(
							"Function size mismatch; expected %d, found %d",
							calculateStateFunctionSize(neighborhoodSize,
									numberOfStates), function.size()));
		int min = Collections.min(function);
		int max = Collections.max(function);
		if (min < 0 || numberOfStates <= max)
			throw new IllegalArgumentException(String.format(
					"range error; expected [0,%d), found [%d,%d)",
					numberOfStates, min, max + 1));

		this.neighborhoodSize = neighborhoodSize;
		this.numberOfStates = numberOfStates;

		this.function = new ArrayList(function);
	}

	/**
	 * Construct a TransitionFunction with a random table as the value and the
	 * given dimensions
	 * 
	 * @param random
	 * a seeded random number generator to fill in the function table
	 * @param neighborhoodSize
	 *            elements in a neighborhood
	 * @param numberOfStates
	 *            number of states any element could have
	 */
	public TransitionFunction(Random random, int neighborhoodSize,
			int numberOfStates) {
		if (neighborhoodSize == 0)
			throw new IllegalArgumentException("neighborhoodSize cannot be 0");
		if (numberOfStates == 0)
			throw new IllegalArgumentException("numberOfStates cannot be 0");

		this.neighborhoodSize = neighborhoodSize;
		this.numberOfStates = numberOfStates;

		this.function = new ArrayList<Integer>(getNumberOfStates());
		for (int s = 0; s < getStateFunctionSize(); ++s)
			this.function.add(random.nextInt(getNumberOfStates()));
	}

	public static TransitionFunction reproduce(Random random, TransitionFunction left, TransitionFunction right) {
		int size = left.getStateFunctionSize();
		int crossover = random.nextInt(size);
		List<Integer> offspringFunction = new ArrayList<Integer>(left.function.subList(0, crossover));
		offspringFunction.addAll(right.function.subList(crossover, size));
		while (random.nextDouble() < mutationChance) {
			int ndx = random.nextInt(size);
			offspringFunction.set(ndx, 1 - offspringFunction.get(ndx));
		}
		
		int lDiff = -1;
		int rDiff = -1;
		for (int i = 0; i < size && (lDiff == -1 || rDiff == -1); ++i) {
			if (left.get(i) != offspringFunction.get(i)) lDiff = i;
			if (right.get(i) != offspringFunction.get(i)) rDiff = i;
		}
		if (monitorReproduction == 1) {
			identical += (lDiff == -1 || rDiff == -1)?1:0;
		} else if (monitorReproduction == 2) {
			System.out.println(String.format("reproduce differences %d, %d", lDiff, rDiff));
		}
		
		return new TransitionFunction(offspringFunction, left.getNeighborhoodSize(), left.getNumberOfStates());
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("%f6: ", fitness));
		for (Integer i : function) 
			if (i == 0) buffer.append(' ');
			else buffer.append('.');
//		buffer.append(function);
		return buffer.toString();
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result + neighborhoodSize;
		result = prime * result + numberOfStates;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransitionFunction other = (TransitionFunction) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (neighborhoodSize != other.neighborhoodSize)
			return false;
		if (numberOfStates != other.numberOfStates)
			return false;
		return true;
	}

	@Override
	public int compareTo(TransitionFunction o) {
		if (fitness < o.fitness) return -1;
		if (fitness == o.fitness) return 0;
		return 1;
	}
}
