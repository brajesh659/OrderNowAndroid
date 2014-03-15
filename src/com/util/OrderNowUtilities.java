package com.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.data.menu.Dish;
import com.example.ordernowandroid.model.FoodMenuItem;

public class OrderNowUtilities {
    private static final String ORDER_NOW = "OrderNow";

	public static ArrayList<FoodMenuItem> getFoodMenuItems(List<Dish> dishes) {
        ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();
        if (dishes != null) {
            for (Dish dish : dishes) {
                foodMenuItem.add(new FoodMenuItem(dish));
            }
        }
        return foodMenuItem;
    }
    
    public static String getKeyFromSharedPreferences(Context context, String key) {
    	SharedPreferences preferences = getSharedPreferences(context);
    	String value =  preferences.getString(key, "");
    	Utilities.info("SharedPrefs get key " + key + " value " + value);
    	return value;
    }

    public static void putKeyToSharedPreferences(Context context, String key, String value) {
    	SharedPreferences preferences = getSharedPreferences(context);
    	preferences.edit().putString(key, value).commit();
    	Utilities.info("SharedPrefs put key " + key + " value " + value);
    }
    
	private static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(ORDER_NOW, Context.MODE_PRIVATE);
	}
}
