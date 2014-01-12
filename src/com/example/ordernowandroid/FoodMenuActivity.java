package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.example.ordernowandroid.fragments.MenuFragment;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_menu);

		orderItems = new ArrayList<MyOrderItem>();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

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
				R.drawable.ic_launcher, //nav menu toggle icon
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
			myfragment.setMyOrders(orderItems);
			fragment = myfragment;
			break;
		case 2:			
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[2], getItemListForCategory(navMenuTitles[2]));
			break;
		case 3:        	
			fragment = IndividualMenuTabFragment.newInstance(navMenuTitles[3], getItemListForCategory(navMenuTitles[3]));
			break;
		case 4:
			fragment = new MenuFragment();
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
		// slide menu items
		String[] itemNames;
		int[] itemPrices;
		ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();

		if(categoryName.equalsIgnoreCase("soups")) {
			itemNames = getResources().getStringArray(R.array.soups);
			itemPrices = getResources().getIntArray(R.array.soups_prices);
			for(int i=0; i<itemNames.length; i++){
				foodMenuItem.add(new FoodMenuItem(itemNames[i], itemPrices[i]));    			
			}    		
		} else {
			for(int i=1; i<=6; i++){
				foodMenuItem.add(new FoodMenuItem("Item " + i, i*10));	
			}
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
		Toast.makeText(this, orderItems.toString(),Toast.LENGTH_SHORT).show();		
	}

}
