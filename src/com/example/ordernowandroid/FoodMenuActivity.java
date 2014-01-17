package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ordernowandroid.adapter.NavDrawerListAdapter;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;
import com.example.ordernowandroid.fragments.MyOrderFragment;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.NavDrawerItem;

public class FoodMenuActivity extends FragmentActivity implements IndividualMenuTabFragment.numListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private List<MyOrderItem> orderItems;	
	private CharSequence mDrawerTitle; // nav drawer title	
	private CharSequence mTitle; // used to store app title
	private String[] navMenuTitles; // slide menu items
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private Map<FoodMenuItem, Integer> foodItemQuantityMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_menu);

		orderItems = new ArrayList<MyOrderItem>();
		foodItemQuantityMap = new HashMap<FoodMenuItem, Integer>();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		//add the drawer menu items
		List<String> navMenuTitlesList = Arrays.asList(navMenuTitles);
		for(int i=0;i<navMenuTitlesList.size();i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitlesList.get(i), navMenuIcons.getResourceId(i, -1)));
		}
		//how to add a counter example
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));

		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
				){
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
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) { // update the main content by replacing fragments        
		Fragment fragment = null;
		switch (position) {
		case 0:        	
			MyOrderFragment myfragment = new MyOrderFragment();
			if(foodItemQuantityMap != null) {
				for (FoodMenuItem key : foodItemQuantityMap.keySet()) {
					orderItems.add(new MyOrderItem(key, foodItemQuantityMap.get(key)));
				}
			myfragment.setMyOrders(orderItems);			
			}
			fragment = myfragment;
			break;
		case 2:
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[2], getItemListForCategory(navMenuTitles[2]));
			break;
		case 3:        	
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[3], getItemListForCategory(navMenuTitles[3]));
			break;
		case 4:
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[4], getItemListForCategory(navMenuTitles[4]));
			//fragment = new MenuFragment();
			break;
		case 5:
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[5], getItemListForCategory(navMenuTitles[5]));
			break;
		case 6:
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[6], getItemListForCategory(navMenuTitles[6]));
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		} else {
			// error in creating fragment
			Log.e("FoodMenuActivity", "Error in creating fragment");
		}
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private ArrayList<FoodMenuItem> getItemListForCategory(String categoryName) {

		if(categoryName == null || categoryName.trim() == ""){
			return null;
		}		

		String[] itemNames = null;
		int[] itemPrices = null;
		ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();		

		categoryName = categoryName.toLowerCase();
		if(categoryName.equals("soups")) {
			//TODO
			/*int itemNameIdentifier = getResources().getIdentifier(categoryName, "id", getPackageName());
			int itemPriceIdentifier = getResources().getIdentifier(categoryName + "_prices", "id", getPackageName());*/
			itemNames = getResources().getStringArray(R.array.soups);
			itemPrices = getResources().getIntArray(R.array.soups_prices);			
		} else if (categoryName.equals("starters")){
			itemNames = getResources().getStringArray(R.array.starters);
			itemPrices = getResources().getIntArray(R.array.starters_prices);
		} else if (categoryName.equals("salads")){
			itemNames = getResources().getStringArray(R.array.salads);
			itemPrices = getResources().getIntArray(R.array.salads_prices);
		}  else if (categoryName.equals("sizzlers")){
			itemNames = getResources().getStringArray(R.array.sizzlers);
			itemPrices = getResources().getIntArray(R.array.sizzlers_prices);
		}  else if (categoryName.equals("favourites")){
			itemNames = getResources().getStringArray(R.array.favourites);
			itemPrices = getResources().getIntArray(R.array.favourites_prices);
		}
		
		
		for(int i=0; itemNames !=null && i<itemNames.length; i++){
			foodMenuItem.add(new FoodMenuItem(itemNames[i], itemPrices[i]));    			
		}

		return foodMenuItem;
	}

	@Override
	public void onQtyChange(FoodMenuItem foodMenuItem, int quantity) {
		if(orderItems == null) {
			orderItems = new ArrayList<MyOrderItem>();
		}
		MyOrderItem orderItem = new MyOrderItem(foodMenuItem, quantity);
		orderItems.add(orderItem);
		//Toast.makeText(this, orderItems.toString(),Toast.LENGTH_SHORT).show();		
	}

	@Override
	public Integer getQuantity(FoodMenuItem foodMenuItem) {
		if(foodItemQuantityMap.get(foodMenuItem) != null) {
			return foodItemQuantityMap.get(foodMenuItem);
		}
		return 0;
	}

	@Override
	public void incrementQuantity(FoodMenuItem foodMenuItem) {
		int quantity = 0;
		if(foodItemQuantityMap.get(foodMenuItem) != null) {
			quantity = foodItemQuantityMap.get(foodMenuItem);
		}
		quantity++;
		foodItemQuantityMap.put(foodMenuItem, quantity);
		Toast.makeText(this, foodMenuItem.toString() + " " + quantity,Toast.LENGTH_SHORT).show();
	}

	@Override
	public void decrementQuantity(FoodMenuItem foodMenuItem) {
		int quantity = 0;
		if (foodItemQuantityMap.get(foodMenuItem) != null) {
			quantity = foodItemQuantityMap.get(foodMenuItem);
		}
		if (quantity == 0) {
			Toast.makeText(
					this,
					foodMenuItem.toString() + " " + quantity
							+ " can't be decreased", Toast.LENGTH_SHORT).show();
		} else {
			quantity--;
			foodItemQuantityMap.put(foodMenuItem, quantity);
			Toast.makeText(this, foodMenuItem.toString() + " " + quantity,
					Toast.LENGTH_SHORT).show();
		}
	}

}
