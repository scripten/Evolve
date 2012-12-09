package solution;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import livingObjects.ActionGene;
import livingObjects.Genome;
import livingObjects.Species;
import model.WorldParameters;

public class S_Species implements Species {
	public static S_Species loadSpecies(String name, String s_name, Scanner fin, WorldParameters worldParameters) {
		String line = fin.nextLine();
		if (line.equals("ver 1.0"))
			return version10(name, s_name, fin, worldParameters);
		else if (line.equals("ver 2.0"))
			return version20(name, s_name, fin, worldParameters);
		return null;
	}
	/**
	 * <pre>
	 * Line 1: ver 1.0 (already consumed)
	 * Line 2: 9 action gene numbers [0-15]
	 * Line 3: Metabolism (percentage)
	 * Line 4: Spawn Threshold / 10
	 * Line 5: Action mutation %
	 * Line 6: Metabolism mutation %
	 * Line 7: Spawn Threshold mutation %
	 * Line 8: Energy cost per generation(?)
	 * Line 9: empty
	 * </pre>
	 * @param name
	 * @param fin
	 * @param worldParameters
	 * @return
	 */
	private static S_Species version10(String name, String s_name, Scanner fin, WorldParameters worldParameters) {
		S_Species the_species = new S_Species(name);
		String line = fin.nextLine();
		Scanner sin = new Scanner(line);
		List<ActionGene> actionGenes = new ArrayList<ActionGene>();
		while (sin.hasNextInt()) {
			int ag = sin.nextInt();
			actionGenes.add(ActionGene.fromInt(ag));
		}

		line = fin.nextLine();
		sin = new Scanner(line);
		double metabolism = sin.nextInt() / 100.0;

		line = fin.nextLine();
		sin = new Scanner(line);
		int spawnThreshold = sin.nextInt() * 10;

		line = fin.nextLine();
		sin = new Scanner(line);
		double actionGeneMutation = sin.nextInt() / 100.0;

		line = fin.nextLine();
		sin = new Scanner(line);
		double metabolismMutation = sin.nextInt() / 100.0;

		line = fin.nextLine();
		sin = new Scanner(line);
		double spawnMutation = sin.nextInt() / 100.0;

		the_species.genome = new S_Genome(0, actionGenes, metabolism, spawnThreshold, actionGeneMutation, metabolismMutation, spawnMutation);

		the_species.setColor(worldParameters.getColor(s_name));
		the_species.setName(name);
		the_species.setFoodValue(worldParameters.getAnimalFoodValue());

		return the_species;
	}
	private static S_Species version20(String name, String s_name, Scanner fin, WorldParameters worldParameters) {
		S_Species retval = new S_Species(name);

		return retval;
	}

	private Color color;
	private int foodValue;
	private S_Genome genome;
	private String name;

	private S_Species(String name) {
		this.name = name;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getFoodValue() {
		return foodValue;
	}

	@Override
	public Genome getGenes() {
		return genome;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFoodValue(int foodValue) {
		this.foodValue = foodValue;
	}

	public void setName(String name) {
		this.name = name;
	}
}
