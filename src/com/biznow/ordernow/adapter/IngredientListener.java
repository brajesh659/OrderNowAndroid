package com.biznow.ordernow.adapter;

import com.biznow.ordernow.model.IngredientOptionView;

public interface IngredientListener {

	public boolean isSelected(IngredientOptionView optionView);

	public void updateIngredient(IngredientOptionView optionView, boolean checked);

}
