package com.example.ordernowandroid.model;

import java.io.Serializable;

import com.data.menu.Option;
import com.data.menu.RecommendationType;

public class OptionView implements Serializable {

	@Override
	public String toString() {
		return "OptionView [option=" + option + "]";
	}

	private static final long serialVersionUID = 1L;
	private Option option;
	
	public OptionView(Option option) {
		this.option = option;
		recommendation = RecommendationType.All;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OptionView) {
			OptionView option = (OptionView) o;

			return this.getOptionName().equals(option.getOptionName());

		}
		return false;
	}
	
	private boolean isSelected;
	private RecommendationType recommendation;
	private String recommendationString;

	public String getWhyRecommended() {
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

}
