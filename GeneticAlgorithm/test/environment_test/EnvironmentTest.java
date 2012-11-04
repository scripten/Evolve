package environment_test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import automata.CA;
import automata.TransitionFunction;

import environment.Environment;

public class EnvironmentTest {
	private static final int SIZE = 101;

	private TransitionFunction validTransitionFunction() {
		int sizeOfNeighborhood = 7;
		int numberOfStates = 2;
		List<Integer> stateFunction = new ArrayList<Integer>();
		int state = 0;
		for (int i = 0; i < TransitionFunction.calculateStateFunctionSize(
				sizeOfNeighborhood, numberOfStates); ++i) {
			stateFunction.add(state);
			state = (state + 1) % numberOfStates;
		}
		stateFunction.set(stateFunction.size()-1, 0);
		stateFunction.set(0, numberOfStates-1);
		return new TransitionFunction(stateFunction, sizeOfNeighborhood,
				numberOfStates);
	}

	private Environment makeEnvironment() {
		TransitionFunction tf = validTransitionFunction();
		return new Environment(tf);

	}

	@Before
	public void initializeSize() {
		Environment.setSize(SIZE);
	}

	@Test
	public void testSize() {
		assertThat(Environment.getSize(), equalTo(SIZE));
	}

	@Test
	public void testConstructor() {
		TransitionFunction tf = validTransitionFunction();
		int halfNeighborhood = tf.getNeighborhoodSize() / 2;
		Environment env = new Environment(tf);
		assertThat(env, notNullValue());
		// Make sure first and last neighbors are the CA we expect them to be
		for (int k = 0; k < 2; ++k) {
			List<CA> circularArray = env.getCurrentGeneration();
			for (int i = 0; i < SIZE; ++i) {
				CA curr = circularArray.get(i);
				int first = circularArray
						.indexOf(curr.getNeighborhood().get(0));
				int last = circularArray.indexOf(curr.getNeighborhood().get(
						tf.getNeighborhoodSize() - 1));
				assertThat(first, equalTo((i + SIZE - halfNeighborhood) % SIZE));
				assertThat(last, equalTo((i + halfNeighborhood) % SIZE));
			}
			env.nextGeneration();
		}
	}

	@Test
	public void testSettingStates() {
		Environment env = makeEnvironment();
		List<Integer> states = new ArrayList<Integer>(env.getSize());
		for (int i = 0; i < env.getSize(); ++i)
			states.add(env.getNumberOfStates() - 1);

		env.set(states);
		for (int i = 0; i < env.getSize(); ++i)
			assertThat(env.get(i).getState(),
					equalTo(env.getNumberOfStates() - 1));
	}

	@Test
	public void testNextGeneration() {
		TransitionFunction tf = validTransitionFunction();
		Environment env = new Environment(tf);

		List<Integer> states = new ArrayList<Integer>(env.getSize());
		for (int i = 0; i < env.getSize(); ++i)
			states.add(0);

		// check one value for each possible state
		for (int k = 0; k < tf.getNumberOfStates(); ++k) {
			int ndx = 0;
			for (int i = 0; i < tf.getNeighborhoodSize();++i)
				ndx = ndx * tf.getNumberOfStates() + k;
				
			for (int i = 0; i < env.getSize(); ++i) 
				states.set(i, k);
							
			env.set(states);
			env.calculateNextGeneration();
			env.nextGeneration();
			for (int i = 0; i < env.getSize(); ++i)
				assertThat(env.get(i).getState(),
						equalTo(tf.get(ndx)));
		}
	}
	@Test
	public void testAdvance() {
		TransitionFunction tf = validTransitionFunction();
		Environment env = new Environment(tf);

		List<Integer> states = new ArrayList<Integer>(env.getSize());
		for (int i = 0; i < env.getSize(); ++i)
			states.add(0);

		// check one value for each possible state
		for (int k = 0; k < tf.getNumberOfStates(); ++k) {
			int ndx = 0;
			for (int i = 0; i < tf.getNeighborhoodSize();++i)
				ndx = ndx * tf.getNumberOfStates() + k;
				
			for (int i = 0; i < env.getSize(); ++i) 
				states.set(i, k);
							
			env.set(states);
			env.advance(1);
			for (int i = 0; i < env.getSize(); ++i)
				assertThat(env.get(i).getState(),
						equalTo(tf.get(ndx)));
		}
	}
}
