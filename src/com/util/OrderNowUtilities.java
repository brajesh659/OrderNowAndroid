package com.util;

import java.util.ArrayList;
import java.util.List;

import com.data.menu.Dish;
import com.example.ordernowandroid.model.FoodMenuItem;

public class OrderNowUtilities {
    public static ArrayList<FoodMenuItem> getFoodMenuItems(List<Dish> dishes) {
        ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();
        if (dishes != null) {
            for (Dish dish : dishes) {
                foodMenuItem.add(new FoodMenuItem(dish));
            }
        }
        return foodMenuItem;
    }
}
