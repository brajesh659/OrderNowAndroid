package com.data.menu;

import java.io.Serializable;

public class Option implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String optionId;
	private String optionName;
	@Override
	public String toString() {
		return "Option [optionName=" + optionName + "]";
	}

	private String description;
	
	public Option(String optionName) {
		this.optionName = optionName;
	}

	public Option(String optionName, String description) {
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
		if (o instanceof Option) {
			Option option = (Option) o;

			return this.optionName.equals(option.getOptionName());

		}
		return false;
	}

	public String getOptionId() {
		return optionId;
	}

}
