package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.data.menu.Category;
import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.data.menu.Restaurant;
import com.example.ordernowandroid.adapter.NavDrawerListAdapter;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;
import com.example.ordernowandroid.fragments.MyOrderFragment;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.model.CategoryNavDrawerItem;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;

public class FoodMenuActivity extends FragmentActivity implements numListener, ActionBar.TabListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle; // nav drawer title
    private CharSequence mTitle; // used to store app title
    private ArrayList<CategoryNavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private ActionBar actionBar;
    private Restaurant restaurant;
    private Map<FoodMenuItem, Integer> foodItemQuantityMap = new HashMap<FoodMenuItem, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_menu);
        displayActionTabBar();
        restaurant = getResturant();
        mTitle = getTitle();
        mDrawerTitle = restaurant.getName();
        Toast.makeText(this, "onCreate FoodMenuActivytCalled", Toast.LENGTH_SHORT).show();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<CategoryNavDrawerItem>();

        for (Category category : getCategories()) {
            CategoryNavDrawerItem categoryNavDrawerItem = new CategoryNavDrawerItem(category);
            navDrawerItems.add(categoryNavDrawerItem);
        }

        // how to add a counter example
        // navDrawerItems.add(new NavDrawerItem(navMenuTitles[3],
        // navMenuIcons.getResourceId(3, -1), true, "22"));

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, // nav
                                                                                             // menu
                                                                                             // toggle
                                                                                             // icon
                R.string.app_name, // nav drawer open - description for
                                   // accessibility
                R.string.app_name // nav drawer close - description for
                                  // accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first menu item
            displayView(2);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private List<Category> getCategories() {
        return restaurant.getMenu().getCategories();
    }

    private void displayActionTabBar() {
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText(R.string.tabActionBarMyOrder).setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) { // update the main content by
                                             // replacing fragments
        Fragment fragment = null;
        Category category = getCategories().get(position);
        fragment = IndividualMenuTabFragment.newInstance(category.getName(), getFoodMenuItems(category.getDishes()));

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        } else {
            // error in creating fragment
            Log.e("FoodMenuActivity", "Error in creating fragment");
        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(category.getName());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private ArrayList<FoodMenuItem> getFoodMenuItems(List<Dish> dishes) {
        ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();
        for (Dish dish : dishes) {
            foodMenuItem.add(new FoodMenuItem(dish));
        }
        return foodMenuItem;
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        Toast.makeText(this, "Tab reselected", Toast.LENGTH_SHORT).show();
        displayMyOrders();
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void displayMyOrders() {
        List<MyOrderItem> orderItems = new LinkedList<MyOrderItem>();
        MyOrderFragment myfragment = new MyOrderFragment();
        if (foodItemQuantityMap != null) {
            for (FoodMenuItem key : foodItemQuantityMap.keySet()) {
                orderItems.add(new MyOrderItem(key, foodItemQuantityMap.get(key)));
            }
            myfragment.setMyOrders(orderItems);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, myfragment).addToBackStack(null).commit();
    }

    @Override
    public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
        Toast.makeText(this, "Tab selected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        Toast.makeText(this, "Tab unselected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public Integer getQuantity(FoodMenuItem foodMenuItem) {
        if (foodItemQuantityMap.get(foodMenuItem) != null) {
            return foodItemQuantityMap.get(foodMenuItem);
        }
        return 0;
    }

    @Override
    public void incrementQuantity(FoodMenuItem foodMenuItem) {
        int quantity = 0;
        if (foodItemQuantityMap.get(foodMenuItem) != null) {
            quantity = foodItemQuantityMap.get(foodMenuItem);
        }
        quantity++;
        foodItemQuantityMap.put(foodMenuItem, quantity);
        Toast.makeText(this, foodMenuItem.toString() + "  " + quantity, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decrementQuantity(FoodMenuItem foodMenuItem) {
        int quantity = 0;
        if (foodItemQuantityMap.get(foodMenuItem) != null) {
            quantity = foodItemQuantityMap.get(foodMenuItem);
        }
        if (quantity == 0) {
            Toast.makeText(this, foodMenuItem.toString() + " " + quantity + " can't be decreased", Toast.LENGTH_SHORT)
                    .show();
        } else {
            quantity--;
            if (quantity == 0) {
                foodItemQuantityMap.remove(foodMenuItem);
            } else {
                foodItemQuantityMap.put(foodMenuItem, quantity);
            }
            Toast.makeText(this, foodMenuItem.toString() + "  " + quantity, Toast.LENGTH_SHORT).show();
        }
    }

    public Restaurant getResturant() {
        List<Integer> categoryItemName = new LinkedList<Integer>();
        categoryItemName.add(R.array.soups);
        categoryItemName.add(R.array.starters);
        categoryItemName.add(R.array.salads);
        categoryItemName.add(R.array.sizzlers);
        categoryItemName.add(R.array.favourites);

        List<Integer> categoryItemPrice = new LinkedList<Integer>();
        categoryItemPrice.add(R.array.soups_prices);
        categoryItemPrice.add(R.array.starters_prices);
        categoryItemPrice.add(R.array.salads_prices);
        categoryItemPrice.add(R.array.sizzlers_prices);
        categoryItemPrice.add(R.array.favourites_prices);
        String[] categoryNames = getResources().getStringArray(R.array.nav_drawer_items);
        List<Category> categories = new LinkedList<Category>();
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category();
            getCategory(categoryNames[i], categoryItemName.get(i), categoryItemPrice.get(i), category);
            categories.add(category);
        }

        com.data.menu.Menu menu = new com.data.menu.Menu();
        menu.setCategories(categories);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Eat 3");
        restaurant.setMenu(menu);
        return restaurant;
    }

    private void getCategory(String categoryName, int itemNameResource, int itemPriceResource, Category soupCategory) {
        soupCategory.setName(categoryName);
        List<Dish> dishes = new LinkedList<Dish>();
        getDishes(dishes, itemNameResource, itemPriceResource);
        soupCategory.setDishes(dishes);
    }

    private void getDishes(List<Dish> soupDishes, int soups, int soupsPrices) {
        String[] itemNames = getResources().getStringArray(soups);
        int[] itemPrices = getResources().getIntArray(soupsPrices);

        for (int i = 0; itemNames != null && i < itemNames.length; i++) {
            Dish dish = new Dish();
            dish.setName(itemNames[i]);
            dish.setPrice(itemPrices[i]);
            if (i % 2 == 0) {
                dish.setType(FoodType.Veg);
            } else {
                dish.setType(FoodType.NonVeg);
            }
            dish.setDescription("item description comes here");
            soupDishes.add(dish);
        }
    }
}
