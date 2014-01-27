package com.data.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.util.Utilities;

public class DishHelper extends SQLHelper {

    private static final String KEY_TYPE = "type";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMG = "img";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_DISH_ID = "dishId";

    public DishHelper(DatabaseManager dbManager) {
        super(dbManager);
    }

    private static final String TABLE_NAME = "dishes";


    public List<FoodMenuItem> searchDishes(String searchCriteria) {
        String GET_ALL_EVENTS = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE '%" + searchCriteria
                + "%' OR description LIKE '%" + searchCriteria + "%'";
        Cursor cursor = dbManager.rawQuery(GET_ALL_EVENTS, null);

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
                dishId = cursor.getString(cursor.getColumnIndex(KEY_DISH_ID));
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION));
                img = cursor.getString(cursor.getColumnIndex(KEY_IMG));
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE));
                type = cursor.getString(cursor.getColumnIndex(KEY_TYPE));
                Dish dish = new Dish(dishId, name, description, img, Float.valueOf(price), FoodType.valueOf(type));
                FoodMenuItem foodMenuItem = new FoodMenuItem(dish);

                searchFoodList.add(foodMenuItem);

            } while (cursor.moveToNext());
        }

        Utilities.info(searchFoodList.toString());
        return searchFoodList;
    }

    public void addDish(Dish dish) {
        try {
            if (dish != null) {
                ContentValues values = new ContentValues();
                values.put(KEY_DISH_ID, dish.getDishId());
                values.put(KEY_NAME, dish.getName());
                values.put(KEY_DESCRIPTION, dish.getDescription());
                values.put(KEY_IMG, (dish.getImg()!=null)?dish.getImg():"");
                values.put(KEY_PRICE, Float.toString(dish.getPrice()));
                values.put(KEY_TYPE, dish.getType().toString());
                
                dbManager.insert(TABLE_NAME, null, values);
                Utilities.info("insert " + values);
            }
        } catch (Exception e) {
            Utilities.error("Exception in adding dish " + e);
        }

    }

}
