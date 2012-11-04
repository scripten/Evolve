package automata;

/**
 * Interface for something providing a state value
 * 
 * @author blad
 */
public interface StateProvider {
	/**
	 * Get the current state (a small integer)
	 * 
	 * @return the state value
	 */
	public int getState();

	/**
	 * Directly set the current state
	 * 
	 * @param state
	 *            new state value
	 */
	public void setState(int state);
}
