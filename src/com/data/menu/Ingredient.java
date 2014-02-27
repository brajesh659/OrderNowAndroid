package com.data.menu;

import java.io.Serializable;
import java.util.List;

public class Ingredient implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private List<IngredientOption> ingredientOptions;
	private int minOptionSelection = 1;
	
	public Ingredient(String title, List<IngredientOption> options) {
		super();
		this.title = title;
		this.ingredientOptions = options;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<IngredientOption> getIngredientOptions() {
		return ingredientOptions;
	}
	public void setIngredientOptions(List<IngredientOption> options) {
		this.ingredientOptions = options;
	}

	public int getMinOptionSelection() {
		return minOptionSelection;
	}

	public void setMinOptionSelection(int minOptionSelection) {
		this.minOptionSelection = minOptionSelection;
	}

}
