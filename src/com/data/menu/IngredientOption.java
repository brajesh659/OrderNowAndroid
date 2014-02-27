package com.data.menu;

import java.io.Serializable;

public class IngredientOption implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String optionId;
	private String optionName;
	@Override
	public String toString() {
		return "Option [optionName=" + optionName + "]";
	}

	private String description;
	
	public IngredientOption(String optionName) {
		this.optionName = optionName;
	}

	public IngredientOption(String optionName, String description) {
		this.optionName = optionName;
		this.description = description;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IngredientOption) {
			IngredientOption option = (IngredientOption) o;

			return this.optionName.equals(option.getOptionName());

		}
		return false;
	}

	public String getOptionId() {
		return optionId;
	}

}
