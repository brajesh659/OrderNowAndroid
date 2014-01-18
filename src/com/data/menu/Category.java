package com.data.menu;

import java.util.List;

public class Category {
	private List<Category> categories;
	private List<Dish> dishes;
	private String name;
	
	public List<Dish> getDishes() {
		return dishes;
	}
	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
	    // TODO Auto-generated method stub
	    return "Name : " + name + "Categories " + dishes.toString();
	}
}
