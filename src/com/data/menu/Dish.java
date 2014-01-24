package com.data.menu;

import java.io.Serializable;

public class Dish implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Dish(String dishId, String name, String description, String img, float price, FoodType type) {
        this.dishId = dishId;
        this.name = name;
        this.description = description;
        this.img = img;
        this.price = price;
        this.type = type;
    };

    public Dish() {
    };

    private String dishId;
    private String name;
    private String description;
    private String img;
    private Float price;
    private FoodType type; // Veg or non-veg
	private boolean isAvailable = true;

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

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Name : " + getName() + " Price : " + getPrice();
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


}
