package automata_test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import automata.CA;
import automata.StateProvider;
import automata.TransitionFunction;

public class TransitionFunctionTest {

	private List<Integer> validStateFunction(int sizeOfNeighborhood, int numberOfStates) {
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
	private List<Integer> invalidStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		
		// last element one too large
		int sizeOfFunction = TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates);
		stateFunction.set(sizeOfFunction - 1, numberOfStates);

		return stateFunction;
	}
	
	private List<Integer> shortStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		
		// last element removed
		int sizeOfFunction = TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates);
		stateFunction.remove(sizeOfFunction - 1);

		return stateFunction;
	}

	private List<Integer> longStateFunction(int sizeOfNeighborhood, int numberOfStates) {
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
	private TransitionFunction makeValidTransitionFunction(int sizeOfNeighborhood, int numberOfStates) {
		List<Integer> stateFunction = validStateFunction(sizeOfNeighborhood, numberOfStates);		
		return new TransitionFunction(stateFunction, sizeOfNeighborhood, numberOfStates);
	}
	
	@Test
	public void testConstructorWithValidTransitionFunction() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = validStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);
		assertThat(transitionFunction, notNullValue());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFailConstructorWithInvalidTransitionFunction() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = invalidStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFailConstructorWithShortTransitionFunction() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = shortStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFailConstructorWithLongTransitionFunction() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = longStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFailConstructorWithZeroNeighborhood() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = validStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, 0, numberOfStates);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFailConstructorWithZeroStates() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = validStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, 0);
	}

	@Test
	public void testNextStateNdx() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = validStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);

		List<StateProvider> automata = new ArrayList<StateProvider>();
		for (int i = 0; i < transitionFunction.getNeighborhoodSize(); ++i)
			automata.add(new StateProvider() {
				int state = 0;
				@Override public void setState(int i) { state = i; }
				@Override public int getState() { return state; }
			});
		
		for (int neighborhood = 0; neighborhood < TransitionFunction.calculateStateFunctionSize(sizeOfNeighborhood, numberOfStates); ++neighborhood) {
			int bitString = neighborhood;
			for (int neighbor = transitionFunction.getNeighborhoodSize(); neighbor > 0; ) {
				--neighbor;
				int v = bitString % transitionFunction.getNumberOfStates();
				bitString = bitString / transitionFunction.getNumberOfStates();
				automata.get(neighbor).setState(v);
			}
			int nextState = transitionFunction.get(neighborhood);
			assertThat(String.format("mismatch using int at neighborhood %d", neighborhood), nextState, equalTo(function.get(neighborhood)));
			int anotherState = transitionFunction.get(automata);
			assertThat(String.format("mismatch using StateProviders at neighborhood %d", neighborhood), anotherState, equalTo(function.get(neighborhood)));
		}
	}

}
