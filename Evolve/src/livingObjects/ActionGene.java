package livingObjects;

public enum ActionGene {
	/**
	 * The action genes present in Evolve! Lite. The order is taken from the
	 * numbering of the actions in the save files for the game. Thus gene 0 is
	 * NORTH, 1 is NORTH_EAST, and so on.
	 */
	NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST,
	STOP, RANDOM, LEFT, RIGHT, U_TURN, FOLLOW, AVOID, GO;

	/**
	 * Cache for ActionGene.values(); used by fromInt method
	 */
	private static ActionGene [] valuesCache = null;
	/**
	 * Convert an integer into its equivalent ActionGene
	 * @param i index of the gene
	 * @return associated gene
	 */
	public static ActionGene fromInt(int i) {
		if (valuesCache == null)
			valuesCache = ActionGene.values();
		return valuesCache[i];
	}
}
