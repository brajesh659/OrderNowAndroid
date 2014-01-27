package com.data.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.util.Utilities;

public class DishHelper extends SQLHelper {

    public DishHelper(DatabaseManager dbManager) {
        super(dbManager);
    }

    private static final String TABLE_NAME = "dishes";

    private static final String[] columns = { "dishId", "name", "description", "img", "price", "type" };

    public List<FoodMenuItem> searchDishes(String searchCriteria) {
        String GET_ALL_EVENTS = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE '%" + searchCriteria
                + "%' OR description LIKE '%" + searchCriteria + "%'";
        Cursor cursor = dbManager.rawQuery(GET_ALL_EVENTS, null);
        Map<String, String> map = new HashMap<String, String>();

        Utilities.info(Integer.toString(cursor.getCount()));
        String dishId = "";
        String name = "";
        String description = "";
        String img = "";
        String price = "";
        String type = "";

        List<FoodMenuItem> searchFoodList = new ArrayList<FoodMenuItem>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                dishId = cursor.getString(cursor.getColumnIndex("dishId"));
                name = cursor.getString(cursor.getColumnIndex("name"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                img = cursor.getString(cursor.getColumnIndex("img"));
                price = cursor.getString(cursor.getColumnIndex("price"));
                type = cursor.getString(cursor.getColumnIndex("type"));
                Dish dish = new Dish(dishId, name, description, img, Float.valueOf(price), FoodType.valueOf(type));
                FoodMenuItem foodMenuItem = new FoodMenuItem(dish);

                searchFoodList.add(foodMenuItem);

            } while (cursor.moveToNext());
        }

        Utilities.info(searchFoodList.toString());
        return searchFoodList;
    }

}
