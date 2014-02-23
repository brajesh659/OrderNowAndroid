package com.example.ordernowandroid.model;

import java.util.List;

import com.data.menu.Ingredient;

public class FoodIngredient {

	private Ingredient ingredient;
	private List<String> selectedOptions;
	
	public List<String> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<String> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public FoodIngredient(Ingredient ingredient, List<String> selectedOptions) {
		super();
		this.ingredient = ingredient;
		this.selectedOptions = selectedOptions;
	}

	public FoodIngredient(Ingredient ingredient) {
		super();
		this.ingredient = ingredient;
	}
	
	public String getTitle() {
		return ingredient.getTitle();
	}
	
	public List<String> getOptions() {	
		return ingredient.getOptions();
	}
	
	public String getBitMapText() {
		String text = getTitle();
		//text += "First\n\tSecond\t\tThird\t\tFourth\t\t\t\t\tfifth";
		
		if(selectedOptions != null && !selectedOptions.isEmpty()) {
			text += "\n" ;
			for(String option : selectedOptions) {
				text += option + "\t";
			}
		}
		return text;
	}
	
}
