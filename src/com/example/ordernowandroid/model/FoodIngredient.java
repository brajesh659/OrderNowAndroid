package com.example.ordernowandroid.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.data.menu.Ingredient;
import com.data.menu.Option;
import com.util.Utilities;

public class FoodIngredient implements Serializable{

	private static final long serialVersionUID = 1L;
	private Ingredient ingredient;
	private List<OptionView> selectedOptions;
	
	public List<OptionView> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<OptionView> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public FoodIngredient(Ingredient ingredient, List<OptionView> selectedOptions) {
		super();
		this.ingredient = ingredient;
		this.selectedOptions = selectedOptions;
	}

	public FoodIngredient(Ingredient ingredient) {
		super();
		this.ingredient = ingredient;
		this.selectedOptions = new ArrayList<OptionView>();
	}
	
	public String getTitle() {
		return ingredient.getTitle();
	}
	
	public List<String> getOptions() {	
		return ingredient.getOptions();
	}
	
	public void addOption(OptionView optionview) {
		if(!selectedOptions.contains(optionview)) {
			selectedOptions.add(optionview);
		}
	}
	
	public void removeOption(OptionView optionview) {
		if(selectedOptions.contains(optionview)) {
			selectedOptions.remove(optionview);
		}
	}
	
	public String getBitMapText() {
		String text = getTitle();
		
		if(selectedOptions != null && !selectedOptions.isEmpty()) {
			text += "\nSelected: " ;
			for(OptionView option : selectedOptions) {
				text += option.getOptionName() + ", ";
			}
		}
		return text;
	}

	public void prepareSelectedOptions(List<OptionView> selectedOptions) {
		this.selectedOptions = new ArrayList<OptionView>();
		if(selectedOptions != null) {
			List<String> options = ingredient.getOptions();
			for(String option : options) {
				Option op = new Option(option);
				OptionView opv = new OptionView(op);
				if(selectedOptions.contains(opv)) {
					this.selectedOptions.add(opv);
				}
			}
		}
		
	}
	
}
