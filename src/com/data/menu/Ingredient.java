package com.data.menu;

import java.io.Serializable;
import java.util.List;

public class Ingredient implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private List<String> options;
	
	public Ingredient(String title, List<String> options) {
		super();
		this.title = title;
		this.options = options;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}

}
