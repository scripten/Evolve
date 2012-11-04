package automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cellular automaton transition function described by a sequence of result
 * state values.
 * 
 * @author blad
 */
public class TransitionFunction {
	private List<Integer> function;
	// size of the neighborhood (how many CA are used to index the table)
	private int neighborhoodSize;
	// unique states; base to interpret neighborhood as an integer
	private int numberOfStates;

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
	 * Use the neighborhood to calculate an index into the function table.
	 * 
	 * @param neighborhood
	 *            collection of state providers; treated as digits of a base
	 *            numberOfStates number
	 * @return the state stored at the given location
	 */
	public int get(List<StateProvider> neighborhood) {
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
}
