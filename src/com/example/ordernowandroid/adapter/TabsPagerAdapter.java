package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.data.menu.Category;
import com.data.menu.Dish;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.filter.MenuPropertyKey;
import com.example.ordernowandroid.filter.MenuPropertyValue;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;
import com.example.ordernowandroid.model.FoodMenuItem;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
	private static final List<String> TITLES = new ArrayList<String>(Arrays.asList("All", "Veg", "NonVeg"));;
    
    private Category category;
	public TabsPagerAdapter(FragmentManager fm, Category category) {
		super(fm);
        this.category = category;
	}
	
    @Override
    public Fragment getItem(int index) {
        Log.i("TabsPagerAdapter", "slide event " + index);
        List<MenuPropertyValue> value = new ArrayList<MenuPropertyValue>();
        MenuPropertyValue tabType = null;
        if (index == 0) {
            tabType = MenuPropertyValue.All;
        } else if (index == 1) {
            tabType = MenuPropertyValue.Veg;
        } else if (index == 2) {
            tabType = MenuPropertyValue.NonVeg;
        }

        value.add(tabType);

        HashMap<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
        selectedFilters.put(MenuPropertyKey.FoodType, value);

        MenuFilter menuFilter = new MenuFilter();
        menuFilter.addFilter(selectedFilters);

        return IndividualMenuTabFragment.newInstance(category.getName(), getFoodMenuItems(category.getDishes()), menuFilter);
    }

	@Override
	public int getCount() {
		if(TITLES !=  null ) {
			return TITLES.size();
		}
		return 0;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES.get(position);
	}
	

    private ArrayList<FoodMenuItem> getFoodMenuItems(List<Dish> dishes) {
        ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();
        if (dishes != null) {
            for (Dish dish : dishes) {
                foodMenuItem.add(new FoodMenuItem(dish));
            }
        }
        return foodMenuItem;
    }

}
