package com.data.menu;

import java.io.Serializable;
import java.util.List;

public class OrderDish implements Serializable {

	private static final long serialVersionUID = 1L;
	private Float dishQty;
	private String dishNote;
	private String spiceLevel;
	private List<IngredientOption> selectedOptions;
	
	public OrderDish (Float dishQty, String dishNote, String spiceLevel) {
		this.dishQty = dishQty;
		this.dishNote = dishNote;
		this.spiceLevel = spiceLevel;
	};
	
	public OrderDish(Float dishQty) {
		this.dishQty = dishQty;
		this.dishNote = null;
		this.spiceLevel = null;
	}

	public Float getDishQty() {
		return dishQty;
	}
	
	public void setDishQty(Float dishQty) {
		this.dishQty = dishQty;
	}
	
	public String getDishNote() {
		return dishNote;
	}
	
	public void setDishNote(String dishNote) {
		this.dishNote = dishNote;
	}

	public String getSpiceLevel() {
		return spiceLevel;
	}

	public void setSpiceLevel(String spiceLevel) {
		this.spiceLevel = spiceLevel;
	}

	public List<IngredientOption> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<IngredientOption> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}
	
}
