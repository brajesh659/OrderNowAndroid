package com.biznow.ordernow.model;

import java.io.Serializable;

import com.data.menu.IngredientOption;
import com.data.menu.RecommendationType;
import com.util.Utilities;

public class IngredientOptionView implements Serializable {

	@Override
	public String toString() {
		return "OptionView [option=" + option + "]";
	}

	private static final long serialVersionUID = 1L;
	private IngredientOption option;

	public IngredientOption getOption() {
		return option;
	}

	public IngredientOptionView(IngredientOption option) {
		this.option = option;

		int random = Utilities.randInt();
		if ((random % 7) == 0) {
			recommendation = RecommendationType.NotRecommended;
			recommendationString = "Not Recommended based on selections";
		} else if ((random % 3) == 0) {
			recommendation = RecommendationType.Recommended;
			recommendationString = "Recommended based on selections";
		} else {
			recommendation = RecommendationType.None;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IngredientOptionView) {
			IngredientOptionView option = (IngredientOptionView) o;

			return this.getOptionName().equals(option.getOptionName());

		}
		return false;
	}

	private boolean isSelected;
	private RecommendationType recommendation;
	private String recommendationString;

	public String getRecommendationString() {
		return recommendationString;
	}

	public void setWhyRecommended(String whyRecommended) {
		this.recommendationString = whyRecommended;
	}

	public String getOptionName() {
		return option.getOptionName();
	}

	public String getDescription() {
		return option.getDescription();
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public RecommendationType getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(RecommendationType recommendation) {
		this.recommendation = recommendation;
	}

	public void setDescription(String description) {
		this.option.setDescription(description);
	}

}
