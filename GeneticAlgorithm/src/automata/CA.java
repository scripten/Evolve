package automata;

import java.util.ArrayList;
import java.util.List;

public class CA {
	/**
	 * TransitionFunction: next state table indexed by values of the
	 * neighborhood.
	 */
	private static TransitionFunction stateFunction = null;

	/**
	 * Get the number of elements in a neighborhood (determined when
	 * TransitionFunction was constructed).
	 * 
	 * @return size of neighborhood
	 */
	public static int getNeighborhoodSize() {
		return stateFunction.getNeighborhoodSize();
	}

	/**
	 * Get number of unique states any CA can take on; states are numbered
	 * [0..numberOfStates)
	 * 
	 * @return number of states
	 */
	public static int getNumberOfStates() {
		return stateFunction.getNumberOfStates();
	}

	/**
	 * Permits outside classes to see the current TransitionFunction.
	 * 
	 * @return reference to the current TransitionFunction.
	 */
	public static TransitionFunction getStateFunction() {
		return stateFunction;
	}

	/**
	 * Set the new and future TransitionFunction. Does NOT invalidate existing
	 * instances of CA (probably should unless they capture the TF on
	 * construction).
	 * 
	 * @param stateFunction
	 *            current setting of the state function variable
	 */
	public static void setStateFunction(TransitionFunction stateFunction) {
		CA.stateFunction = stateFunction;
	}

	/**
	 * References to the neighborhood (usually includes this CA somewhere inside
	 * the list).
	 */
	private List<CA> neighborhood;
	/**
	 * Current state
	 */
	private int state;

	/**
	 * Construct a new CA with the given initial state.
	 * 
	 * @param state
	 *            initial state value
	 */
	public CA(int state) {
		setState(state);
		neighborhood = null;
	}

	/**
	 * Get neighborhood list
	 * @return current neighborhood list
	 */
	public List<CA> getNeighborhood() {
		return neighborhood;
	}

	/**
	 * Returns the current state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Get the next state value for this CA given its neighborhood's current
	 * states
	 * 
	 * @return state value to transition to
	 */
	public int nextState() {
		return stateFunction.get(neighborhood);
	}

	/**
	 * Install the neighborhood. Since a group of CA are constructed at the same
	 * time and are mutually one another's neighbors, it is necessary to link
	 * the neighborhoods AFTER construction. This is where the linkage takes
	 * place.
	 * 
	 * @param neighborhood
	 *            the neighborhood of this CA; exception thrown if not the
	 *            correct size as determined by current TransitionFunction. This
	 *            value is COPIED so parameter can be modified after setting.
	 */
	public void setNeighborhood(List<CA> neighborhood) {
		if (neighborhood.size() != getNeighborhoodSize())
			throw new IllegalArgumentException(String.format(
					"neighborhood size should be %d; was %d",
					getNeighborhoodSize(), neighborhood.size()));
		this.neighborhood = new ArrayList<CA>(neighborhood);
	}

	/**
	 * Sets the current state. If state is outside permitted range (as specified
	 * by current TransistionFunction), IllegalArgumentException is raised.
	 */
	public void setState(int state) {
		if (state < 0 || getNumberOfStates() <= state)
			throw new IllegalArgumentException(String.format(
					"state expected range [0, %d]; found %d",
					getNumberOfStates(), state));

		this.state = state;
	}
}
