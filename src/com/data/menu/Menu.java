package com.data.menu;

import java.util.List;

public class Menu {
	private List<Category> categories;

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
	    return categories.toString();
	}
}
 