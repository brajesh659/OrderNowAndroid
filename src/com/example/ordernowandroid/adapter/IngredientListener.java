package com.example.ordernowandroid.adapter;

import com.example.ordernowandroid.model.IngredientOptionView;

public interface IngredientListener {

	public boolean isSelected(IngredientOptionView optionView);

	public void updateIngredient(IngredientOptionView optionView, boolean checked);

}
