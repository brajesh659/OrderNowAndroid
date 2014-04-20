package com.biznow.ordernow.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.biznow.ordernow.filter.MenuFilter;
import com.biznow.ordernow.fragments.IndividualMenuTabFragment;
import com.biznow.ordernow.model.FoodMenuItem;
import com.data.menu.Category;
import com.data.menu.Dish;
import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;
import com.google.gson.Gson;
import com.util.OrderNowUtilities;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    private Category category;

    public TabsPagerAdapter(FragmentManager fm, Category category) {
        super(fm);
        this.category = category;
    }

    private List<MenuPropertyValue> getTitles() {
        if(category==null || category.getCategoryLevelFilter() == null){
            return new ArrayList<MenuPropertyValue>();
        }
        return category.getCategoryLevelFilter().getFilterValue();
    }

    @Override
    public Fragment getItem(int index) {
        Log.i("TabsPagerAdapter", "slide event " + index);
        List<MenuPropertyValue> value = new ArrayList<MenuPropertyValue>();
        MenuPropertyValue tabType = null;

        tabType = category.getCategoryLevelFilter().getFilterValue().get(index);

        value.add(tabType);

        HashMap<MenuPropertyKey, List<MenuPropertyValue>> selectedFilters = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
        selectedFilters.put(getFilterType(), value);

        MenuFilter menuFilter = new MenuFilter();
        menuFilter.addFilter(selectedFilters);

        return IndividualMenuTabFragment.newInstance(category.getName(), OrderNowUtilities.getFoodMenuItems(category.getDishes()), menuFilter);
    }

    private MenuPropertyKey getFilterType() {
        return category.getCategoryLevelFilter().getFilterName();
    }

    @Override
    public int getCount() {
        if (getTitles() != null) {
            return getTitles().size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitles().get(position).toString();
    }


}
