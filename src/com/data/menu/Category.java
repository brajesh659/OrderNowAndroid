package com.data.menu;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<Category> categories;
	private List<Dish> dishes;
	private String name;
	private CategoryLevelFilter categoryLevelFilter = CategoryLevelFilter.NONE_CATEGORY_LEVEL_FILTER;
	
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
	
	public CategoryLevelFilter getCategoryLevelFilter() {
        return categoryLevelFilter;
    }
	
	public void setCategoryLevelFilter(CategoryLevelFilter categoryProperty) {
        this.categoryLevelFilter = categoryProperty;
    }

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	@Override
	public String toString() {
	    // TODO Auto-generated method stub
	    if(categories!=null) {
	        return "Name : " + name + "Categories " + categories.toString();
	    }
	    if (dishes!=null) {
	        return "Name : " + name + "Categories " + dishes.toString();
	    }
	    return "Name : " + name + "No sub Categories and no dish available";
	}
}
