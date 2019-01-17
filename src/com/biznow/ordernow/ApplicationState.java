package com.biznow.ordernow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.biznow.ordernow.filter.AvailableMenuFilter;
import com.biznow.ordernow.filter.MenuFilter;
import com.biznow.ordernow.model.IngredientOptionView;
import com.biznow.ordernow.model.MyOrderItem;
import com.data.menu.CustomerOrderWrapper;
import com.data.restaurant.RestaurantOrder;
import com.parse.Parse;
import com.parse.PushService;
import com.util.Utilities;

public class ApplicationState extends Application {
	private String restaurantName;
	private String activeOrderId;
	private String userName;
	private String profilePictureId;
	private int categoryId = -1;
	private MenuFilter menuFilter;
	private AvailableMenuFilter menuFilterAvailable;
	private HashMap<String, MyOrderItem> foodMenuItemQuantityMap;
	private CustomerOrderWrapper customerOrderWrapper;
	private Map<String, List<IngredientOptionView>> dishIngredientMap;
	private ArrayList<RestaurantOrder> myOrderHistoryList;
	private boolean openCategoryDrawer = true;
	private int childCategoryId = 0;
	private String firstName;
	private String lastName;
	private String facebookId;
	private String phoneNo;
	private String parseId;
	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getParseId() {
		return parseId;
	}

	public void setParseId(String parseId) {
		this.parseId = parseId;
	}

	public ApplicationState() {
		menuFilter = new MenuFilter();
		dishIngredientMap = new HashMap<String, List<IngredientOptionView>>();
		restaurantName = "";
	}
	
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        PushService.setDefaultPushCallback(this, MyParentOrderActivity.class);
    }

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getChildCategoryId() {
		return childCategoryId;
	}

	public void setChildCategoryId(int categoryId) {
		this.childCategoryId = categoryId;
	}

	public static int getCategoryId(ApplicationState applicationContext) {
		return applicationContext.getCategoryId();

	}

	public static void setCategoryId(ApplicationState applicationContext, int categoryId) {
		applicationContext.setCategoryId(categoryId);
	}

	public static ArrayList<MyOrderItem> getMyOrderItems(ApplicationState applicationContext) {
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = applicationContext.getFoodMenuItemQuantityMap();
		ArrayList<MyOrderItem> myOrderItems = new ArrayList<MyOrderItem>();
		myOrderItems.addAll(foodMenuItemQuantityMap.values());
		return myOrderItems;
	}

	public MenuFilter getMenuFilter() {
		return menuFilter;
	}

	public static MenuFilter getMenuFilter(ApplicationState applicationContext) {
		return applicationContext.getMenuFilter();
	}

	public AvailableMenuFilter getAvailableMenuFilter() {
		return menuFilterAvailable;
	}

	public void setAvailableMenuFilter(AvailableMenuFilter menuFilterAvailable) {
		this.menuFilterAvailable = menuFilterAvailable;
	}

	public static AvailableMenuFilter getAvailableMenuFilter(ApplicationState applicationContext) {
		return applicationContext.getAvailableMenuFilter();
	}

	public static void setAvailableMenuFilter(ApplicationState applicationContext,AvailableMenuFilter menuFilterAvailable ) {
		applicationContext.setAvailableMenuFilter(menuFilterAvailable);
	}


	public HashMap<String, MyOrderItem> getFoodMenuItemQuantityMap() {
		if (foodMenuItemQuantityMap == null)
			foodMenuItemQuantityMap = new HashMap<String, MyOrderItem>();
		return foodMenuItemQuantityMap;
	}

	public static HashMap<String, MyOrderItem> getFoodMenuItemQuantityMap(ApplicationState applicationContext) {
		return applicationContext.getFoodMenuItemQuantityMap();
	}

	public void setFoodMenuItemQuantityMap(HashMap<String, MyOrderItem> foodMenuItemQuantityMap) {
		this.foodMenuItemQuantityMap = foodMenuItemQuantityMap;
	}

	public static void setFoodMenuItemQuantityMap(ApplicationState applicationContext,
			HashMap<String, MyOrderItem> foodMenuItemQuantityMap) {
		applicationContext.setFoodMenuItemQuantityMap(foodMenuItemQuantityMap);
	}

	public CustomerOrderWrapper getCustomerOrderWrapper() {
		return customerOrderWrapper;
	}

	public static CustomerOrderWrapper getCustomerOrderWrapper(ApplicationState applicationContext) {
		return applicationContext.getCustomerOrderWrapper();
	}

	public void setCustomerOrderWrapper(CustomerOrderWrapper customerOrderWrapper) {
		this.customerOrderWrapper = customerOrderWrapper;
	}

	public static void setCustomerOrderWrapper(ApplicationState applicationContext,
			CustomerOrderWrapper customerOrderWrapper) {
		applicationContext.setCustomerOrderWrapper(customerOrderWrapper);
	}

	public static void cleanFoodMenuItemQuantityMap(ApplicationState applicationContext) {
		applicationContext.setFoodMenuItemQuantityMap(new HashMap<String, MyOrderItem>());
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProfilePictureId() {
		return profilePictureId;
	}

	public void setProfilePictureId(String profilePictureId) {
		this.profilePictureId = profilePictureId;
	}

	public List<IngredientOptionView> getDishSelectedIngredientList(String dishName) {
		return dishIngredientMap.get(dishName);
	}

	public static List<IngredientOptionView> getDishSelectedIngredientList(ApplicationState applicationContext,
			String dishName) {
		return applicationContext.getDishSelectedIngredientList(dishName);
	}

	public void setDishSelectedIngredientList(String dishName, List<IngredientOptionView> optionList) {
		dishIngredientMap.put(dishName, optionList);
	}

	public static void setDishSelectedIngredientList(ApplicationState applicationContext, String dishName,
			List<IngredientOptionView> optionList) {
		applicationContext.setDishSelectedIngredientList(dishName, optionList);
	}

	public static void addDishSelectedIngredient(ApplicationState applicationContext, String dishName,
			IngredientOptionView option) {
		applicationContext.addDishSelectedIngredient(dishName, option);
	}

	public static void removeDishSelectedIngredient(ApplicationState applicationContext, String dishName,
			IngredientOptionView option) {
		applicationContext.removeDishSelectedIngredient(dishName, option);
	}

	public void addDishSelectedIngredient(String dishName, IngredientOptionView option) {
		if (dishIngredientMap.get(dishName) == null) {
			List<IngredientOptionView> optionList = new ArrayList<IngredientOptionView>();
			dishIngredientMap.put(dishName, optionList);
		}
		List<IngredientOptionView> optionList = dishIngredientMap.get(dishName);
		optionList.add(option);
		dishIngredientMap.put(dishName, optionList);
		Utilities.info("addDishSelecteedIng dishIngredientMap " + dishIngredientMap);
	}

	public void removeDishSelectedIngredient(String dishName, IngredientOptionView option) {
		if (dishIngredientMap.get(dishName) == null) {
			return;
		}
		List<IngredientOptionView> optionList = dishIngredientMap.get(dishName);
		optionList.remove(option);
		if (optionList.isEmpty()) {
			dishIngredientMap.remove(dishName);
		} else {
			dishIngredientMap.put(dishName, optionList);
		}
		Utilities.info("removeDishSelectedIngredient dishIngredientMap " + dishIngredientMap);
	}

	public void cleanDishSelectedIngredients(String dishName) {
		if (dishIngredientMap.containsKey(dishName)) {
			dishIngredientMap.remove(dishName);
		}
	}

	public static void cleanDishSelectedIngredients(ApplicationState applicationContext, String dishName) {
		applicationContext.cleanDishSelectedIngredients(dishName);
	}

	public ArrayList<RestaurantOrder> getMyOrderHistoryList() {
		return myOrderHistoryList;
	}

	public void setMyOrderHistoryList(ArrayList<RestaurantOrder> myOrderHistoryList) {
		this.myOrderHistoryList = myOrderHistoryList;
	}

	public static ArrayList<RestaurantOrder> getMyOrderHistoryList(ApplicationState applicationContext) {
		return applicationContext.getMyOrderHistoryList();
	}

	public static void setMyOrderHistoryList(ApplicationState applicationContext,
			ArrayList<RestaurantOrder> myOrderHistoryList) {
		applicationContext.setMyOrderHistoryList(myOrderHistoryList);
	}

	public boolean isOpenCategoryDrawer() {
		return openCategoryDrawer;
	}

	public void setOpenCategoryDrawer(boolean openCategoryDrawer) {
		this.openCategoryDrawer = openCategoryDrawer;
	}

	public static boolean isOpenCategoryDrawer(ApplicationState applicationContext) {
		return applicationContext.isOpenCategoryDrawer();
	}

	public static void setOpenCategoryDrawer(ApplicationState applicationContext, boolean openCategoryDrawer) {
		applicationContext.setOpenCategoryDrawer(openCategoryDrawer);
	}

	public static int getChildCategoryId(ApplicationState applicationContext) {
		return applicationContext.getChildCategoryId();
	}


	public static void setChildCategoryId(ApplicationState applicationContext, int childCategoryId) {
		applicationContext.setChildCategoryId(childCategoryId);
	}

	public static String getRestaurantName(ApplicationState applicationContext) {
		return applicationContext.getRestaurantName();
	}

	public static void setRestaurantName(ApplicationState applicationContext, String restName) {
		applicationContext.setRestaurantName(restName);
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getActiveOrderId() {
		return activeOrderId;
	}

	public void setActiveOrderId(String activeOrderId) {
		this.activeOrderId = activeOrderId;
	}

	public static String getActiveOrderId(ApplicationState applicationContext) {
		return applicationContext.getActiveOrderId();
	}

	public static void setActiveOrderId(ApplicationState applicationContext, String orderId) {
		applicationContext.setActiveOrderId(orderId);
	}

}
