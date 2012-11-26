package solution;

import java.awt.Color;

import livingObjects.Plant;

public class S_Plant implements Plant {
	private int foodValue;
	private String name;
	private Color color;

	public void setFoodValue(int foodValue) {
		this.foodValue = foodValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(Color color) {
		this.color = color;
	}

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



}
