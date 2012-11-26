package solution;

import java.awt.Color;
import java.util.List;
import java.util.Scanner;

import livingObjects.ActionGene;
import livingObjects.Genome;
import livingObjects.Species;

public class S_Species implements Species {
	private int foodValue;
	private String name;
	private Color color;
	private List<ActionGene> actionGenes;
	private int spawnThreshold;
	private int metabolism;


	@Override
	public int getFoodValue() {
		return foodValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Color getColor() {
		return color;
	}

	public void setFoodValue(int foodValue) {
		this.foodValue = foodValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	private static S_Species version10(String name, Scanner fin) {
		S_Species retval = new S_Species();

		return retval;
	}

	private static S_Species version20(String name, Scanner fin) {
		S_Species retval = new S_Species();

		return retval;
	}

	public static S_Species loadSpecies(String name, Scanner fin) {
		String line = fin.nextLine();
		if (line.equals("ver 1.0"))
			return version10(name, fin);
		else if (line.equals("ver 2.0"))
			return version20(name, fin);
		return null;
	}

	@Override
	public Genome getGenes() {
		// TODO Auto-generated method stub
		return null;
	}

}
