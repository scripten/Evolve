package automata_test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import automata.CA;
import automata.TransitionFunction;

public class TransitionFunctionTest 
	extends TransitionFunctionTestBase {
	
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
	public void testSizeOfStateFunction() {
		for (int neighborhood = 3; neighborhood < 10; neighborhood+=2) {
			for (int states = 2; states < 6; ++states) {
				TransitionFunction transitionFunction = new TransitionFunction(validStateFunction(neighborhood, states), neighborhood, states);
				assertThat(transitionFunction.getStateFunctionSize(), equalTo((int)(Math.pow(states, neighborhood))));
			}
		}
	}
	
	@Test
	public void testNextStateNdx() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> function = validStateFunction(sizeOfNeighborhood, numberOfStates);
		TransitionFunction transitionFunction = new TransitionFunction(function, sizeOfNeighborhood, numberOfStates);

		List<CA> automata = new ArrayList<CA>();
		for (int i = 0; i < transitionFunction.getNeighborhoodSize(); ++i)
			automata.add(new CA(0) {
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
