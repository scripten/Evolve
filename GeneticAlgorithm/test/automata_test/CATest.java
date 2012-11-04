package automata_test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import automata.CA;
import automata.TransitionFunction;

public class CATest
	extends TransitionFunctionTestBase {
	private void initializeStateFunction(int sizeOfNeighborhood, int numberOfStates) {
		TransitionFunction stateFunction = makeValidTransitionFunction(sizeOfNeighborhood, numberOfStates);
		CA.setStateFunction(stateFunction);		
	}
	
	private CA makeInstance(int state, int sizeOfNeighborhood, int numberOfStates) {
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		return new CA(state);
	}
	
// static properties tests
	@Test
	public void testSetStateFunction() {
		TransitionFunction stateFunction = makeValidTransitionFunction(7, 2);
		CA.setStateFunction(stateFunction);
		TransitionFunction setValue = CA.getStateFunction();
		assertThat(setValue, equalTo(stateFunction));
	}


// instance properties tests
	@Test
	public void testCAConstructor() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		
		for (int state = 0; state < CA.getNumberOfStates(); ++state) {
			CA cellularAutomaton = new CA(state);
			assertThat(cellularAutomaton, notNullValue());
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCAConstructStateTooBig() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		int state = CA.getNumberOfStates();
		CA cellularAutomaton = new CA(state);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCAConstructStateTooSmall() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		int state = -1;
		CA cellularAutomaton = new CA(state);
	}
	
	@Test
	public void testSetState() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		CA cellularAutomaton = new CA(0);
	
		for (int state = CA.getNumberOfStates(); state > 0; ) {
			--state;
			cellularAutomaton.setState(state);
			assertThat(cellularAutomaton, notNullValue());
		}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCASetStateTooBig() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		CA cellularAutomaton = new CA(0);
		int state = CA.getNumberOfStates();
		cellularAutomaton.setState(state);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCASetStateTooSmall() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		initializeStateFunction(sizeOfNeighborhood, numberOfStates);
		CA cellularAutomaton = new CA(0);
		int state = -1;
		cellularAutomaton.setState(state);
	}
	
}
