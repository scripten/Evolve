package livingObjects;

/**
 * Root interface for INSTANCES of living objects. A living object lives
 * somewhere so the instance can provide its location.
 *
 * @author blad
 */
public interface LivingObjectInstance {
	/**
	 * Get the instance's location in the world.
	 * @return the current location.
	 */
	Location getLocation();

}
