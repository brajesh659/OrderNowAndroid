package com.example.ordernowandroid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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
import android.view.ViewConfiguration;
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.database.CustomDbAdapter;
import com.data.database.RestaurantHelper;
import com.data.menu.Category;
import com.data.menu.CategoryLevelFilter;
import com.data.menu.CustomerOrderWrapper;
import com.data.menu.Dish;
import com.data.menu.DishIngredients;
import com.data.menu.FoodType;
import com.data.menu.Ingredient;
import com.data.menu.IngredientOption;
import com.data.menu.MenuPropertyKey;
import com.data.menu.MenuPropertyValue;
import com.data.menu.Restaurant;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.example.ordernowandroid.adapter.DownloadResturantMenu;
import com.example.ordernowandroid.adapter.ImageService;
import com.example.ordernowandroid.adapter.NewNavDrawerListAdapter;
import com.example.ordernowandroid.filter.AvailableMenuFilter;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.fragments.AddNoteDialogFragment;
import com.example.ordernowandroid.fragments.AddNoteListener;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment;
import com.example.ordernowandroid.fragments.IndividualMenuTabFragment.numListener;
import com.example.ordernowandroid.fragments.MenuFragment;
import com.example.ordernowandroid.model.CategoryNavDrawerItem;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.parse.ParseAnalytics;
import com.util.AsyncNetwork;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class FoodMenuActivity extends FragmentActivity implements numListener, AddNoteListener,
SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {

    private static final int MY_ORDER_REQUEST_CODE = 1;
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle; // nav drawer title
	private CharSequence mTitle; // used to store app title
	private ArrayList<CategoryNavDrawerItem> navDrawerItems;
	private HashMap<String, ArrayList<CategoryNavDrawerItem>> childDrawerItems;
	private NewNavDrawerListAdapter adapter;
	private Restaurant restaurant;
	private RestaurantHelper dh;
	private static Map<String, Boolean> restaurantLoadedInDb = new HashMap<String, Boolean>();
	private SearchRecentSuggestions suggestionProvider;
	private CursorAdapter suggestionAdapter;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_menu);
		
		ParseAnalytics.trackAppOpened(getIntent());
		
		ApplicationState applicationContext = (ApplicationState) getApplicationContext();

		mTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<CategoryNavDrawerItem>();
		childDrawerItems = new HashMap<String, ArrayList<CategoryNavDrawerItem>>();

		// setting the nav drawer list adapter
		//adapter = new NewNavDrawerListAdapter(getApplicationContext(), navDrawerItems, childDrawerItems);
		adapter = new NewNavDrawerListAdapter(getApplicationContext(), navDrawerItems,childDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		ActionBar actionBar = getActionBar();               
		actionBar.setCustomView(R.layout.search_layout); //load your layout
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_TITLE); //show it 
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility 
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
		if(OrderNowConstants.IS_DEBUG_MODE && OrderNowConstants.IS_LOCAL_RESTURANT_ENABLED) {
		    restaurant = getResturantLocaly();
		} else {
		    restaurant = getResturant(applicationContext.getTableId(), applicationContext.getRestaurantId());
		}

		if (restaurant == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(FoodMenuActivity.this);            
			builder.setTitle("Invalid QR code");
			builder.setMessage("Please scan a valid QR code");
			builder.setPositiveButton(R.string.ok, new OnClickListener() {                  
				@Override
				public void onClick(DialogInterface dialog, int which) {                                                
					Intent intent = new Intent(getApplicationContext(), QRCodeScannerActivity.class);
					startActivity(intent);                                              
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return ;
		} else {
			String resName = restaurant.getName();
			mDrawerTitle = resName;
			ApplicationState.setRestaurantName(applicationContext, resName);
			OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_NAME, resName);
			
			//save preferences
			OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID, applicationContext.getTableId());
			OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID, applicationContext.getRestaurantId());
			
			if (savedInstanceState == null) {
			    if (ApplicationState.getCategoryId(applicationContext)> 0) {
			    	displayView(ApplicationState.getCategoryId(applicationContext), 0);	
			    } else {
			    	displayView(0, 0);
			    }
			}
			mDrawerList.setOnGroupClickListener(new SlideMenuClickListener());
			mDrawerList.setOnChildClickListener(new SlideMenuChildClickListener());

            for (Category category : getCategories()) {
                CategoryNavDrawerItem categoryNavDrawerItem = new CategoryNavDrawerItem(category);
                navDrawerItems.add(categoryNavDrawerItem);
                List<Category> childCategories = category.getCategories();
                if (childCategories != null && !childCategories.isEmpty()) {
                    ArrayList<CategoryNavDrawerItem> childArrayList = new ArrayList<CategoryNavDrawerItem>();
                    for (Category childCategory : childCategories) {
                        CategoryNavDrawerItem childCategoryNavDrawerItem = new CategoryNavDrawerItem(childCategory);
                        childArrayList.add(childCategoryNavDrawerItem);
                    }
                    childDrawerItems.put(categoryNavDrawerItem.getTitle(), childArrayList);
                }
            }
		}
		if (applicationContext.isOpenCategoryDrawer()) {
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}

		CustomDbAdapter dbManager = CustomDbAdapter
				.getInstance(getBaseContext());
		dh = new RestaurantHelper(dbManager); 

		suggestionProvider = new SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY,
				SearchSuggestionProvider.MODE);

		getOverflowMenu();
	}
	

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Category> getCategories() {
		return restaurant.getMenu().getCategories();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//MenuItem searchMenuItem = menu.findItem(R.id.search);
		//searchView = (SearchView) searchMenuItem.getActionView();
		ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.search_layout);
        searchView = (SearchView) actionBar.getCustomView().findViewById(R.id.search);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		searchView.setOnSuggestionListener(this);
		suggestionAdapter = searchView.getSuggestionsAdapter();
		// searchMenuItem.collapseActionView();
		// searchView.setIconifiedByDefault(false);
		
		ImageView call_waiter = (ImageView) actionBar.getCustomView().findViewById(R.id.waiter);
		call_waiter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callWaiterFunction(FoodMenuActivity.this);
			}
		});

		RelativeLayout food_cart_layout = (RelativeLayout)menu.findItem(R.id.action_cart).getActionView();
		TextView food_item_notification = (TextView)food_cart_layout.findViewById(R.id.food_cart_notifcation_textview);

		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap((ApplicationState) getApplicationContext());
		food_item_notification.setText(Integer.toString(foodMenuItemQuantityMap.keySet().size()));
		ImageView cart_image = (ImageView)food_cart_layout.findViewById(R.id.action_cart_image);

		final Context context = this;

		cart_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startMyOrderActivity(context);
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	public static void callWaiterFunction(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle("Call Waiter");
		builder.setMessage("Would you like to call a waiter?");
		builder.setPositiveButton(R.string.yes,
				new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						ApplicationState applicationContext = (ApplicationState) context.getApplicationContext();
						String tableId = applicationContext
								.getTableId();
						String url = new URLBuilder()
								.addPath(URLBuilder.Path.serveTable)
								.addAction(URLBuilder.URLAction.callWaiter)
								.addParam(URLBuilder.URLParam.tableId, tableId)
								.build();
						try {
							new AsyncNetwork().execute(url).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						Toast.makeText(
								context.getApplicationContext(),
								"A waiter will be coming soon to help you!",
								Toast.LENGTH_LONG).show();
					}
				});
		builder.setNegativeButton(R.string.no, null);
		AlertDialog alert = builder.create();
		alert.show();
	}


	private void startParentOrderActivity(final Context context) {
		ArrayList<CustomerOrderWrapper> subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
		if (subOrderList !=null && subOrderList.size() >= 1) {
			Intent intent = new Intent(context, MyParentOrderActivity.class);
			startActivity(intent);
		}  else {
			Toast.makeText(getApplicationContext(), "You currently have no confirmed orders to view.", Toast.LENGTH_LONG).show();
		}
	}

	private void startMyOrderActivity(final Context context) {
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState
				.getFoodMenuItemQuantityMap((ApplicationState) getApplicationContext());
		ArrayList<MyOrderItem> myOrderItems = new ArrayList<MyOrderItem>();
		myOrderItems.addAll(foodMenuItemQuantityMap.values());
		if (myOrderItems != null && myOrderItems.size() >= 1) {
			Intent intent = new Intent(context, MyOrderActivity.class);
			startActivityForResult(intent, MY_ORDER_REQUEST_CODE);
		} else {
			Toast.makeText(getApplicationContext(),
					"Hey wait!! Let's add some items to the order first.",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		searchView.setQuery("", true);
		mDrawerLayout.closeDrawer(Gravity.LEFT);
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_cart :
			startMyOrderActivity(getApplicationContext());
			return true;
		case R.id.confirmed_order :
			startParentOrderActivity(getApplicationContext());
			return true;
		case R.id.search:
			onSearchRequested();
			return true;
		case R.id.filter_menu:
			intent = new Intent(this, FilterMenuActivity.class);
			startActivity(intent);
			return true;
		case R.id.historybutton:
			intent = new Intent(this, MyOrderHistoryActivity.class);
			startActivity(intent);
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
				final ApplicationState applicationContext = (ApplicationState)getApplicationContext();
				Log.i("on RESULT_OK ",""+ApplicationState.getCategoryId(applicationContext) + " " + ApplicationState.getChildCategoryId(applicationContext));
				displayView(ApplicationState.getCategoryId(applicationContext), ApplicationState.getChildCategoryId(applicationContext));
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
	private class SlideMenuClickListener implements ExpandableListView.OnGroupClickListener {

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
            
            if(adapter.getChildrenCount(position)==0) {
                displayView(position, -1);
                return true;
            }
            return false;
        }

	}
	
	private class SlideMenuChildClickListener implements OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//            Toast.makeText(getApplicationContext(), "child selected" + groupPosition, Toast.LENGTH_LONG).show();
            displayView(groupPosition, childPosition);
            return true;
        }

    }

	private void displayView(int position, int childPosition) {
	    Category category;
	    Category parentCategory = getCategories().get(position);
        category = parentCategory;
	    if(childPosition>=0) {
	        List<Category> categories = parentCategory.getCategories();
	        if(categories!=null && !categories.isEmpty()) {
	            ApplicationState.setChildCategoryId((ApplicationState)getApplicationContext(), childPosition);
	            category = categories.get(childPosition);
	        }
	    }
	   // Toast.makeText(this, category.toString(), Toast.LENGTH_LONG).show();
	    Fragment menuFragment = null;
	    Utilities.info("called displayView "+position + "  " + childPosition +" "+category.toString());
        if (category.getCategoryLevelFilter().getFilterName() == MenuPropertyKey.NULL) {
            menuFragment = IndividualMenuTabFragment.newInstance(category.getName(), OrderNowUtilities.getFoodMenuItems(category.getDishes()), new MenuFilter());
        } else {
            menuFragment = MenuFragment.newInstance(category);
        }
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction().replace(R.id.frame_container, menuFragment).commit();
	    CustomDbAdapter dbManager = CustomDbAdapter.getInstance(getBaseContext());
		dh = new RestaurantHelper(dbManager); 	    
	    mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        ApplicationState.setCategoryId((ApplicationState)getApplicationContext(), position);
        setTitle(category.getName());
        mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public float getQuantity(FoodMenuItem foodMenuItem) {
		final String itemName = foodMenuItem.getItemName();
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap((ApplicationState)getApplicationContext());
		if (foodMenuItemQuantityMap.get(itemName) != null) {
			return foodMenuItemQuantityMap.get(itemName).getQuantity();
		}
		return 0;
	}

	@Override
	public void incrementQuantity(FoodMenuItem foodMenuItem) {
		final String itemName = foodMenuItem.getItemName();

		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap(applicationContext);
		if (foodMenuItemQuantityMap.get(itemName) == null) {
			MyOrderItem myOrderItem = new MyOrderItem(foodMenuItem, 1);
			foodMenuItemQuantityMap.put(itemName, myOrderItem);
		} else {
			float quantity = foodMenuItemQuantityMap.get(itemName).getQuantity();
			foodMenuItemQuantityMap.get(itemName).setQuantity(++quantity);
		}
		//updateFoodCartNotificationText();
		invalidateOptionsMenu();
	}

	@Override
	public void decrementQuantity(FoodMenuItem foodMenuItem) {
		float quantity = 0;
		final String itemName = foodMenuItem.getItemName();
		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap(applicationContext);
		if (foodMenuItemQuantityMap.get(itemName) != null) {
			quantity = foodMenuItemQuantityMap.get(itemName).getQuantity();
			quantity--;
			if (quantity == 0) {
				foodMenuItemQuantityMap.remove(itemName);
			} else {
				foodMenuItemQuantityMap.get(itemName).setQuantity(quantity);
			}
		}
		// updateFoodCartNotificationText();
		invalidateOptionsMenu();
	}

	public Restaurant getResturant(String tableId, String restaurantId) {
		if(tableId == null || tableId.trim().length() == 0) {
			return null;
		}
		//http://ordernow.herokuapp.com/serveTable?tableId=T1
		Restaurant restaurant = null;
		try {
			restaurant =  new DownloadRestaurantTask().execute(tableId, restaurantId).get();
			if (restaurant != null) {
				loadRestaurantDishes(restaurant);
				ApplicationState.setRestaurantId((ApplicationState)getApplicationContext(), restaurant.getrId());
			} else {
				throw new Exception("Server failed to load Menu for Table Id: " + tableId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return restaurant;
	}

    private void loadRestaurantDishes(final Restaurant restaurant) {
        Utilities.info("restaurant load " + restaurant.getName() + restaurantLoadedInDb);
        if (!restaurantLoadedInDb.containsKey(restaurant.getName())) {
            // load filter first time
            AvailableMenuFilter availableMenuFilter = null;
            if (restaurant.getAvailableFilters() != null) {
                availableMenuFilter = new AvailableMenuFilter(restaurant.getAvailableFilters());
            } else {
                Map<MenuPropertyKey, List<MenuPropertyValue>> dishProperties = new HashMap<MenuPropertyKey, List<MenuPropertyValue>>();
                // dishProperties.put(MenuPropertyKey.FoodType,
                // Arrays.asList(MenuPropertyValue.Veg,
                // MenuPropertyValue.NonVeg));
                dishProperties.put(MenuPropertyKey.CousineType, Arrays.asList(MenuPropertyValue.NorthIndian, MenuPropertyValue.SouthIndian));
                availableMenuFilter = new AvailableMenuFilter(dishProperties);
            }
            ApplicationState.setAvailableMenuFilter((ApplicationState) getApplicationContext(), availableMenuFilter);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        CustomDbAdapter dbManager = CustomDbAdapter.getInstance(getBaseContext());
                        RestaurantHelper dh = new RestaurantHelper(dbManager);
                        for (Category category : restaurant.getMenu().getCategories()) {
                            if (category.getDishes() != null && !category.getDishes().isEmpty()) {
                                for (Dish dish : category.getDishes()) {
                                    dh.addDish(dish, category.getName());
                                    // Populating images during db creation.
                                    if (dish.getImg() != null) {
                                        try {
                                            ImageService.getInstance().getImageWithCache(dish.getImg());
                                        } catch (Exception e) {
                                            Utilities.error("Could not load iamge " + dish.getImg());
                                        }
                                    }
                                }
                            }
                        }
                        restaurantLoadedInDb.put(restaurant.getName(), true);
                        // for(Res)
                    } catch (Exception e) {
                        Utilities.error("Failed to load into DB " + e);
                    }
                }
            }).start();
        }
    }

	private Restaurant getResturantLocaly() {
        List<Integer> categoryItemName = new LinkedList<Integer>();
        categoryItemName.add(R.array.soups);
        categoryItemName.add(R.array.starters);
        categoryItemName.add(R.array.salads);
        categoryItemName.add(R.array.sizzlers);
        categoryItemName.add(R.array.favourites);
        categoryItemName.add(R.array.frybowl);       

        List<Integer> categoryItemPrice = new LinkedList<Integer>();
        categoryItemPrice.add(R.array.soups_prices);
        categoryItemPrice.add(R.array.starters_prices);
        categoryItemPrice.add(R.array.salads_prices);
        categoryItemPrice.add(R.array.sizzlers_prices);
        categoryItemPrice.add(R.array.favourites_prices);
        categoryItemPrice.add(R.array.frybowl_prices);

		List<Integer> categoryItemID = new LinkedList<Integer>();
		categoryItemID.add(R.array.soups_ids);
		categoryItemID.add(R.array.starters_ids);
		categoryItemID.add(R.array.salads_ids);
		categoryItemID.add(R.array.sizzlers_ids);
		categoryItemID.add(R.array.favourites_ids);
		categoryItemID.add(R.array.frybowl_ids);

		List<Integer> imageId = new LinkedList<Integer>();
		imageId.add(R.array.soups_icons);
		imageId.add(R.array.starters_icons);
		imageId.add(R.array.salads_icons);
		imageId.add(R.array.sizzlers_icons);
		imageId.add(R.array.favourites_icons);
		imageId.add(R.array.frybowl_icons);

        String[] categoryNames = getResources().getStringArray(R.array.nav_drawer_items);
        List<Category> categories = new LinkedList<Category>();
        for (int i = 0; i < categoryNames.length-1; i++) {
            Category category = new Category();
            CategoryLevelFilter categoryProperty;
            if(i==2){
                List<MenuPropertyValue> propertyValue = new ArrayList<MenuPropertyValue>(Arrays.asList(MenuPropertyValue.Beer, MenuPropertyValue.Whisky, MenuPropertyValue.Wine, MenuPropertyValue.Scotch));
                categoryProperty = new CategoryLevelFilter(MenuPropertyKey.DrinkType, propertyValue );
            } else { 
                List<MenuPropertyValue> propertyValue = new ArrayList<MenuPropertyValue>(Arrays.asList(MenuPropertyValue.All, MenuPropertyValue.Veg, MenuPropertyValue.NonVeg));
                categoryProperty = new CategoryLevelFilter(MenuPropertyKey.FoodType, propertyValue );
            }            
            category.setCategoryLevelFilter(categoryProperty );
			getCategory(categoryNames[i], categoryItemName.get(i),
					categoryItemPrice.get(i), categoryItemID.get(i), imageId.get(i), category);
			if(category.getName().equals("Soups")) {
			    Category category1 = new Category();
			    List<Category> categories1 = new ArrayList<Category>();
			    categories1.add(category);
                category1.setCategories(categories1);
                category1.setName("Chinese");
                category = category1;
			}
            categories.add(category);
        }
        Category category = new Category();
        int i = categoryNames.length-1;
        getLastCategory(categoryNames[i], categoryItemName.get(i),
				categoryItemPrice.get(i), categoryItemID.get(i), imageId.get(i), category);
        categories.add(category);
        com.data.menu.Menu menu = new com.data.menu.Menu();
		menu.setCategories(categories);
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Eat 3");
        restaurant.setMenu(menu);
        loadRestaurantDishes(restaurant);
        return restaurant;
    }

	private void getCategory(String categoryName, int itemNameResource,
			int itemPriceResource, int itemDishIds, int itemImage , Category soupCategory) {
        soupCategory.setName(categoryName);
        if(categoryName == "soups"){
            
        }
        List<Dish> dishes = new LinkedList<Dish>();
		getDishes(dishes, itemNameResource, itemPriceResource, itemDishIds, itemImage);
        soupCategory.setDishes(dishes);
    }
	
	private void getLastCategory(String categoryName, int itemNameResource,
			int itemPriceResource, int itemDishIds, int itemImage , Category soupCategory) {
        soupCategory.setName(categoryName);
        List<Dish> dishes = new LinkedList<Dish>();
        getLastCategoryDishes(dishes, itemNameResource, itemPriceResource, itemDishIds, itemImage);
        soupCategory.setDishes(dishes);
    }

	private void getDishes(List<Dish> dishes, int itemNameResource, int itemPriceResource,
			int itemDishIds, int itemImage) {
        String[] itemNames = getResources().getStringArray(itemNameResource);
        int[] itemPrices = getResources().getIntArray(itemPriceResource);
		String[] itemids = getResources().getStringArray(itemDishIds);
		//String[] itemImages = getResources().getStringArray(itemImage);

        for (int i = 0; itemNames != null && i < itemNames.length; i++) {
            Dish dish = new Dish();
            dish.setName(itemNames[i]);
            dish.setPrice(itemPrices[i]);
            //dish.setImg(itemImages[i]);
            dish.setImg("");
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
	
	private void getLastCategoryDishes(List<Dish> dishes, int itemNameResource, int itemPriceResource,
			int itemDishIds, int itemImage) {
        String[] itemNames = getResources().getStringArray(itemNameResource);
        int[] itemPrices = getResources().getIntArray(itemPriceResource);
		String[] itemids = getResources().getStringArray(itemDishIds);
		//String[] itemImages = getResources().getStringArray(itemImage);

		int i = 0;
        for ( i = 0; itemNames != null && i < itemNames.length - 1; i++) {
            Dish dish = new Dish();
            dish.setName(itemNames[i]);
            dish.setPrice(itemPrices[i]);
            //dish.setImg(itemImages[i]);
            dish.setImg("");
            if (i % 2 == 0) {
                dish.setType(FoodType.Veg);
            } else {
                dish.setType(FoodType.NonVeg);
            }
            dish.setDescription("item description comes here");
			dish.setDishId(itemids[i]);
			dish.setDishIngredients(constructDishIngredients());
			dish.setIngredientCustomizable(true);
            dishes.add(dish);

        }
        
        Dish dish = new Dish();
        dish.setName(itemNames[i]);
        dish.setPrice(itemPrices[i]);
        //dish.setImg(itemImages[i]);
        dish.setImg("");
        if (i % 2 == 0) {
            dish.setType(FoodType.Veg);
        } else {
            dish.setType(FoodType.NonVeg);
        }
        dish.setDescription("item description comes here");
		dish.setDishId(itemids[i]);
		dish.setDishIngredients(constructDishIngredients());
		ArrayList<IngredientOption> selectedIngredientOptions = new ArrayList<IngredientOption>();
		List<String> names = Arrays.asList(getResources().getStringArray(R.array.chef0));
		for(String optionName : names ) {
			IngredientOption op = new IngredientOption(optionName);
			selectedIngredientOptions.add(op);
		}
		dish.setSelectedIngredientOptions(selectedIngredientOptions);
		dish.setIngredientCustomizable(true);
        dishes.add(dish);
    }
	
	private DishIngredients constructDishIngredients() {
		DishIngredients di = new DishIngredients();
		ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();
		String[] ingTitle = getResources().getStringArray(
				R.array.ingredients_title);


		Ingredient ing = new Ingredient(ingTitle[0],constuctionOptions(Arrays.asList(getResources().getStringArray(R.array.ing0))));
		ing.setMinOptionSelection(2);
		//FoodIngredient fi = new FoodIngredient(ing);
		ingList.add(ing);

		Ingredient ing1 = new Ingredient(ingTitle[1],constuctionOptions(Arrays.asList(getResources().getStringArray(R.array.ing1))));
		//FoodIngredient fi1 = new FoodIngredient(ing1);
		ingList.add(ing1);

		Ingredient ing2 = new Ingredient(ingTitle[2],constuctionOptions(Arrays.asList(getResources().getStringArray(R.array.ing2))));
		//FoodIngredient fi2 = new FoodIngredient(ing2);
		ingList.add(ing2);

		Ingredient ing3 = new Ingredient(ingTitle[3],constuctionOptions(Arrays.asList(getResources().getStringArray(R.array.ing3))));
		//FoodIngredient fi3 = new FoodIngredient(ing3);
		ingList.add(ing3);
		di.setIngredients(ingList);
		return di;
	}
	
	private ArrayList<IngredientOption> constuctionOptions(List<String> names) {
		//List<String> optionNames = Arrays.asList(getResources().getStringArray(R.array.ing0));
		ArrayList<IngredientOption> optionList =  new ArrayList<IngredientOption>();
		for(String optionName : names) {
			IngredientOption op = new IngredientOption(optionName);
			int random = Utilities.randInt();
			if((random % 7) == 0) {
			op.setDescription("small description comes here");
			}
			optionList.add(op);
		}
		return optionList;
	}

	private class DownloadRestaurantTask extends AsyncTask<String, Integer, Restaurant> {
		@Override
		protected Restaurant doInBackground(String... params) {
			// "http://www.creativefreedom.co.uk/icon-designers-blog/wp-content/uploads/2013/03/00-android-4-0_icons.png"
			CustomDbAdapter dbManager = CustomDbAdapter.getInstance(getBaseContext());
	        RestaurantHelper restHelper = new RestaurantHelper(dbManager);
			return DownloadResturantMenu.getInstance().getResturant(params[0],params[1],restHelper);
		}
	}

	@Override
	public void showNote(FoodMenuItem foodMenuItem) {
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap((ApplicationState)getApplicationContext());
		AddNoteDialogFragment noteFragment = AddNoteDialogFragment.newInstance(foodMenuItem,
				foodMenuItemQuantityMap.get(foodMenuItem.getItemName()));
		noteFragment.show(getSupportFragmentManager(), "notes");
		invalidateOptionsMenu();
	}

	@Override
	public void saveNote(FoodMenuItem foodMenuItem, HashMap<String, String> metaData) {
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = ApplicationState.getFoodMenuItemQuantityMap((ApplicationState)getApplicationContext());
		foodMenuItemQuantityMap.get(foodMenuItem.getItemName()).setNotes("");
		foodMenuItemQuantityMap.get(foodMenuItem.getItemName()).setMetaData(metaData);
		invalidateOptionsMenu();
	}

	@Override
	public boolean onSearchRequested() {
		//overriding this method is letting onQueryTextChange to work
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if(newText == null || newText.isEmpty()) {
			return false;
		}
		String query = newText;
		if(dh!=null) {
			ArrayList<FoodMenuItem> searchDishList = (ArrayList<FoodMenuItem>) dh.searchDishes(query);
			//IndividualMenuTabFragment.newInstance("Search", searchDishList);
			Fragment fragment = IndividualMenuTabFragment.newInstance("Search", searchDishList, null);
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			setTitle(newText);
			return true;
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String finalText) {
		onQueryTextChange(finalText);
		suggestionProvider.saveRecentQuery(finalText, null);
		searchView.clearFocus();
		return true;
	}

	@Override
	public boolean onSuggestionClick(int position) {
		String suggestion = (String) suggestionAdapter.convertToString((Cursor) suggestionAdapter.getItem(position));
		searchView.setQuery(suggestion, false);
		searchView.clearFocus();
		return true;
	}

	@Override
	public boolean onSuggestionSelect(int position) {
		return false;
	}
	 
	@Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        int position = ApplicationState.getCategoryId((ApplicationState) getApplicationContext());
        int childPosition = ApplicationState.getChildCategoryId((ApplicationState) getApplicationContext());
        Utilities.info("on resume " + position + " " + childPosition);

        if (position >= 0) {
            displayView(position, childPosition);
        }
    }

}
