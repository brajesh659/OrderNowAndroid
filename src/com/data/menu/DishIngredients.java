package com.data.menu;

import java.io.Serializable;
import java.util.ArrayList;

public class DishIngredients implements Serializable {

	private ArrayList<Ingredient> ingredients;
	private String did;

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String get_id() {
		return did;
	}

	public void set_id(String _id) {
		this.did = _id;
	}
}
