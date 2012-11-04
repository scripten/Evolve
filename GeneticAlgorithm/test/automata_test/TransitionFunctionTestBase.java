package automata_test;

import java.util.ArrayList;
import java.util.List;

import automata.TransitionFunction;

public class TransitionFunctionTestBase {

	protected List<Integer> validStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = new ArrayList<Integer>();
		int state = 0;
		for (int i = 0; i < TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates); ++i) {
			stateFunction.add(state);
			state = (state + 1) % numberOfStates;
		}
		return stateFunction;
	}

	/**
	 * Create a state function with a single out-of-range function value in the
	 * last slot. 
	 * 
	 * @param sizeOfNeighborhood
	 *            number of elements participating in the transition function
	 * @param numberOfStates
	 *            number of unique states to have in the function
	 * @return invalid transition function table
	 */
	protected List<Integer> invalidStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		
		// last element one too large
		int sizeOfFunction = TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates);
		stateFunction.set(sizeOfFunction - 1, numberOfStates);

		return stateFunction;
	}
	
	protected List<Integer> shortStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		
		// last element removed
		int sizeOfFunction = TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates);
		stateFunction.remove(sizeOfFunction - 1);

		return stateFunction;
	}

	protected List<Integer> longStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		
		// add extra element
		stateFunction.add(0);

		return stateFunction;
	}
	
	/**
	 * Create a state function with values that cycle across the range of valid
	 * value.
	 * 
	 * @param sizeOfNeighborhood
	 *            number of elements participating in the transition function
	 * @param numberOfStates
	 *            number of unique states to have in the function
	 * @return a valid, cycling transition function
	 */
	protected TransitionFunction makeValidTransitionFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		return new TransitionFunction(stateFunction, sizeOfNeighborhood, numberOfStates);
	}
}
