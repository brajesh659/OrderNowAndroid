package com.example.ordernowandroid.model;

import java.io.Serializable;
import com.data.menu.FoodType;
import com.data.menu.Dish;

/**
 * 
 * @author Rohit
 * 
 */

public class FoodMenuItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private Dish dish;

    public FoodMenuItem(Dish dish) {
        this.dish = dish;
    }

    public String getItemName() {
        return dish.getName();
    }

    public Float getItemPrice() {
        return dish.getPrice();
    }

    public FoodType getFoodType() {
        return dish.getType();
    }

    @Override
    public String toString() {
        return dish.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FoodMenuItem) {
            FoodMenuItem fmi = (FoodMenuItem) o;
            return this.dish.equals(fmi.getDish());
        }
        return false;
    }

    private Dish getDish() {
        return this.dish;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
