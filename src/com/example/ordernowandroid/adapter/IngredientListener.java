package com.example.ordernowandroid.adapter;

import com.example.ordernowandroid.model.OptionView;

public interface IngredientListener {

	public boolean isSelected(OptionView optionView);

	public void updateIngredient(OptionView optionView, boolean checked);

}
