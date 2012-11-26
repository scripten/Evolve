package model;

/**
 * Interface for objects interested in seeing when a WorldParameters object
 * changes values.
 *
 * @author blad
 */
public interface WorldParametersChangeListener {
	/**
	 * Called by WorldParameters.publish when a value in the parameters changes.
	 *
	 * @param changed
	 *            the WorldParamters object where something changed.
	 */
	public void change(WorldParameters changed);
}
