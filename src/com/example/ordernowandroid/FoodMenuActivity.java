package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.Category;
import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.data.menu.Restaurant;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.example.ordernowandroid.adapter.DownloadResturantMenu;
import com.example.ordernowandroid.adapter.NavDrawerListAdapter;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.model.CategoryNavDrawerItem;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;

public class FoodMenuActivity extends FragmentActivity implements numListener{

    public static final String TABLE_ID = "TableId";
    private String tableId;
    private static final int MY_ORDER_REQUEST_CODE = 1;
    protected static final String MY_ORDER = "MyOrder";
	protected static final String CATEGORY_ID = null;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle; // nav drawer title
    private CharSequence mTitle; // used to store app title
    private ArrayList<CategoryNavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Restaurant restaurant;
    private HashMap<String, MyOrderItem> foodMenuItemQuantityMap = new HashMap<String, MyOrderItem>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        tableId = b.getString(TABLE_ID);
        
        setContentView(R.layout.food_menu);
        restaurant = getResturant(tableId);
        mTitle = getTitle();
        mDrawerTitle = restaurant.getName();

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
            displayView(0);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private List<Category> getCategories() {
        return restaurant.getMenu().getCategories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        RelativeLayout food_cart_layout = (RelativeLayout)menu.findItem(R.id.action_cart).getActionView();
        TextView food_item_notification = (TextView)food_cart_layout.findViewById(R.id.food_cart_notifcation_textview);
        food_item_notification.setText(Integer.toString(foodMenuItemQuantityMap.keySet().size()));
        ImageView cart_image = (ImageView)food_cart_layout.findViewById(R.id.action_cart_image);
        final Context context = this;
        cart_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<MyOrderItem> orderItems = new ArrayList<MyOrderItem>();
	            if (foodMenuItemQuantityMap != null) {
	                orderItems.addAll(foodMenuItemQuantityMap.values());
	            }
	            Intent intent = new Intent(context, MyOrderActivity.class);
	            intent.putExtra(MY_ORDER, orderItems);
	            intent.putExtra(CATEGORY_ID, mDrawerList.getCheckedItemPosition());
	            intent.putExtra(TABLE_ID, tableId);
	            startActivityForResult(intent, MY_ORDER_REQUEST_CODE);		
			}
		});
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_cart :
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);                
        switch (requestCode) {
        case MY_ORDER_REQUEST_CODE:
            if (resultCode == RESULT_OK) {            	    	
                Bundle bundleExtra = data.getExtras();
                @SuppressWarnings("unchecked")
                ArrayList<MyOrderItem> myOrders = (ArrayList<MyOrderItem>) bundleExtra.getSerializable(MyOrderActivity.RETURN_FROM_MY_ORDER);
                if(myOrders!=null){
                	foodMenuItemQuantityMap = new HashMap<String, MyOrderItem>();
                    for (MyOrderItem myOrderItem : myOrders) {
                        foodMenuItemQuantityMap.put(myOrderItem.getFoodMenuItem().getItemName(), myOrderItem);
                    }
                }
                displayView(bundleExtra.getInt(MyOrderActivity.FOOD_MENU_CATEGORY_ID));
            } else if(resultCode == RESULT_CANCELED && data != null) {
                String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                if(!TextUtils.isEmpty(error)) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, FoodMenuActivity.class);
                startActivity(intent);
            }
            break;
        }
        invalidateOptionsMenu();
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
	public float getQuantity(FoodMenuItem foodMenuItem) {
        final String itemName = foodMenuItem.getItemName();
        if (foodMenuItemQuantityMap.get(itemName) != null) {
            return foodMenuItemQuantityMap.get(itemName).getQuantity();
        }
        return 0;
    }

    @Override
    public void incrementQuantity(FoodMenuItem foodMenuItem) {
		float quantity = 0;
        final String itemName = foodMenuItem.getItemName();
        
        if (foodMenuItemQuantityMap.get(itemName) != null) {
            quantity = foodMenuItemQuantityMap.get(itemName).getQuantity();
        }
        quantity++;
        MyOrderItem myOrderItem = new MyOrderItem(foodMenuItem, quantity);
        foodMenuItemQuantityMap.put(itemName, myOrderItem);
        //updateFoodCartNotificationText();
        invalidateOptionsMenu();
    }

    @Override
    public void decrementQuantity(FoodMenuItem foodMenuItem) {
		float quantity = 0;
        final String itemName = foodMenuItem.getItemName();
        if (foodMenuItemQuantityMap.get(itemName) != null) {
            quantity = foodMenuItemQuantityMap.get(itemName).getQuantity();
        }
        if (quantity == 0) {
            Toast.makeText(this, foodMenuItem.toString() + " " + quantity + " can't be decreased", Toast.LENGTH_SHORT)
                    .show();
        } else {
            quantity--;
            if (quantity == 0) {
                foodMenuItemQuantityMap.remove(itemName);
            } else {
                MyOrderItem myOrderItem = new MyOrderItem(foodMenuItem, quantity);
                foodMenuItemQuantityMap.put(itemName, myOrderItem );
            }
        }
        //updateFoodCartNotificationText();
        invalidateOptionsMenu();
    }

    public Restaurant getResturant(String tableId) {
        //http://ordernow.herokuapp.com/serveTable?tableId=T1
        Restaurant restaurant = null;
        try {
            restaurant = new DownloadRestaurantTask().execute("http://ordernow.herokuapp.com/serveTable",tableId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    private Restaurant getResturantLocaly() {
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

		List<Integer> categoryItemID = new LinkedList<Integer>();
		categoryItemID.add(R.array.soups_ids);
		categoryItemID.add(R.array.starters_ids);
		categoryItemID.add(R.array.salads_ids);
		categoryItemID.add(R.array.sizzlers_ids);
		categoryItemID.add(R.array.favourites_ids);
		
		List<Integer> imageId = new LinkedList<Integer>();
		imageId.add(R.array.soups_icons);
		imageId.add(R.array.starters_icons);
		imageId.add(R.array.salads_icons);
		imageId.add(R.array.sizzlers_icons);
		imageId.add(R.array.favourites_icons);

        String[] categoryNames = getResources().getStringArray(R.array.nav_drawer_items);
        List<Category> categories = new LinkedList<Category>();
        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category();
			getCategory(categoryNames[i], categoryItemName.get(i),
					categoryItemPrice.get(i), categoryItemID.get(i), imageId.get(i), category);
            categories.add(category);
        }

        com.data.menu.Menu menu = new com.data.menu.Menu();
		menu.setCategories(categories);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Eat 3");
        restaurant.setMenu(menu);
        return restaurant;
    }

	private void getCategory(String categoryName, int itemNameResource,
			int itemPriceResource, int itemDishIds, int itemImage , Category soupCategory) {
        soupCategory.setName(categoryName);
        List<Dish> dishes = new LinkedList<Dish>();
		getDishes(dishes, itemNameResource, itemPriceResource, itemDishIds, itemImage);
        soupCategory.setDishes(dishes);
    }

	private void getDishes(List<Dish> dishes, int itemNameResource, int itemPriceResource,
			int itemDishIds, int itemImage) {
        String[] itemNames = getResources().getStringArray(itemNameResource);
        int[] itemPrices = getResources().getIntArray(itemPriceResource);
		String[] itemids = getResources().getStringArray(itemDishIds);
		String[] itemImages = getResources().getStringArray(itemImage);

        for (int i = 0; itemNames != null && i < itemNames.length; i++) {
            Dish dish = new Dish();
            dish.setName(itemNames[i]);
            dish.setPrice(itemPrices[i]);
            dish.setImg(itemImages[i]);
            if (i % 2 == 0) {
                dish.setType(FoodType.Veg);
            } else {
                dish.setType(FoodType.NonVeg);
            }
            dish.setDescription("item description comes here");
			dish.setDishId(itemids[i]);
            dishes.add(dish);
            
        }
    }
	
    private class DownloadRestaurantTask extends AsyncTask<String, Integer, Restaurant> {
        @Override
        protected Restaurant doInBackground(String... params) {
            // "http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png"
            return DownloadResturantMenu.getResturant(params[0], params[1]);
        }

    }

}
