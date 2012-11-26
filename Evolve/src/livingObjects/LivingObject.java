package livingObjects;

import java.awt.Color;

/**
 * Root interface for Plant and Species; represents the commonality of all
 * "types" of "living things" in the simulation. Elements in the simulation have
 * names, food value, and a color.
 *
 * @author blad
 */
public interface LivingObject {

	/**
	 * Get the display color for this living object
	 *
	 * @return current diplay color
	 */
	public Color getColor();

	/**
	 * Get the nutritional value of an object of this type
	 *
	 * @return energy received for eating one of these
	 */
	public int getFoodValue();

	/**
	 * Get the name of this type of world object
	 *
	 * @return name of living object
	 */
	public String getName();

}