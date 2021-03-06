package com.data.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Dish implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Dish(String dishId, String name, String description, String img, float price, boolean available) {
        this.dishId = dishId;
        this.name = name;
        this.description = description;
        this.img = img;
        this.price = price;
        this.available = available;
    };

    public Dish() {
    };

    private String dishId;
    private String name;
    private String description;
    private String img;
    private Float price;
    private Map<MenuPropertyKey, MenuPropertyValue> dishProperties;
	private DishIngredients ingredients;
	private String dishIngredientId;
	private boolean available = true;

    public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	private ArrayList<IngredientOption> selectedIngredientOptions;
    private boolean isIngredientCustomizable = false;
    
	public Map<MenuPropertyKey, MenuPropertyValue> getDishProperties() {
	    if(dishProperties==null) {
	        dishProperties = new HashMap<MenuPropertyKey, MenuPropertyValue>();
	    }
        return dishProperties;
    }

    public void setDishProperties(Map<MenuPropertyKey, MenuPropertyValue> dishProperties) {
        this.dishProperties = dishProperties;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Name : " + getName() + " Price : " + getPrice() + " keys " + getDishProperties().keySet() + " values "+ getDishProperties().values() ;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Dish) {
            Dish dish = (Dish) o;
            if(this.name.equals(dish.getName())) {
                return this.price.equals(this.getPrice());
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

	public DishIngredients getDishIngredients() {
		return ingredients;
	}

	public void setDishIngredients(DishIngredients dishIngredients) {
		this.ingredients = dishIngredients;
	}

	public boolean isIngredientCustomizable() {
		return isIngredientCustomizable;
	}

	public void setIngredientCustomizable(boolean isIngredientCustomizable) {
		this.isIngredientCustomizable = isIngredientCustomizable;
	}

	public ArrayList<IngredientOption> getSelectedIngredientOptions() {
		return selectedIngredientOptions;
	}

	public void setSelectedIngredientOptions(
			ArrayList<IngredientOption> selectedIngredientOptions) {
		this.selectedIngredientOptions = selectedIngredientOptions;
	}
}
