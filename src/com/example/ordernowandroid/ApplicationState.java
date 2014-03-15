package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.filter.AvailableMenuFilter;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.model.IngredientOptionView;
import com.example.ordernowandroid.model.MyOrderItem;
import com.util.Utilities;

public class ApplicationState extends Application {
    private String tableId;
    private String restaurantId;
    private String restaurantName;
    private String activeOrderId;
	private String userName;
    private String profilePictureId;
    private int categoryId = -1;
    private MenuFilter menuFilter;
    private AvailableMenuFilter menuFilterAvailable;
    private HashMap<String, MyOrderItem> foodMenuItemQuantityMap;
    private CustomerOrderWrapper customerOrderWrapper;
    private ArrayList<CustomerOrderWrapper> subOrdersFromDB;
    private Map<String, List<IngredientOptionView>> dishIngredientMap;
    private ArrayList<CustomerOrderWrapper> myOrderHistoryList;
    private boolean openCategoryDrawer = true;
    private int childCategoryId = -1;

    public ApplicationState() {
        menuFilter = new MenuFilter();
        dishIngredientMap = new HashMap<String, List<IngredientOptionView>>();
        restaurantName = "";
    }

    public String getTableId() {
        return tableId;
    }

    public static String getTableId(ApplicationState applicationContext) {
        return applicationContext.getTableId();
    }

    public static void setTableId(ApplicationState applicationContext, String tableId) {
        applicationContext.setTableId(tableId);
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
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

    public ArrayList<CustomerOrderWrapper> getSubOrdersFromDB() {
        if (subOrdersFromDB == null) {
            subOrdersFromDB = new ArrayList<CustomerOrderWrapper>();
        }
        return subOrdersFromDB;
    }

    public static ArrayList<CustomerOrderWrapper> getSubOrdersFromDB(ApplicationState applicationContext) {
        return applicationContext.getSubOrdersFromDB();
    }

    public void setSubOrdersFromDB(ArrayList<CustomerOrderWrapper> subOrdersFromDB) {
        this.subOrdersFromDB = subOrdersFromDB;
    }

    public static void setSubOrdersFromDB(ApplicationState applicationContext,
            ArrayList<CustomerOrderWrapper> subOrdersFromDB) {
        applicationContext.setSubOrdersFromDB(subOrdersFromDB);
    }
    
    public static void cleanSubOrdersFromDB(ApplicationState applicationContext) {
    	applicationContext.setSubOrdersFromDB(new ArrayList<CustomerOrderWrapper>());
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

    public ArrayList<CustomerOrderWrapper> getMyOrderHistoryList() {
        return myOrderHistoryList;
    }

    public void setMyOrderHistoryList(ArrayList<CustomerOrderWrapper> myOrderHistoryList) {
        this.myOrderHistoryList = myOrderHistoryList;
    }

    public static ArrayList<CustomerOrderWrapper> getMyOrderHistoryList(ApplicationState applicationContext) {
        return applicationContext.getMyOrderHistoryList();
    }

    public static void setMyOrderHistoryList(ApplicationState applicationContext,
            ArrayList<CustomerOrderWrapper> myOrderHistoryList) {
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

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	public static String getRestaurantId(ApplicationState applicationContext) {
        return applicationContext.getRestaurantId();
    }

    public static void setRestaurantId(ApplicationState applicationContext, String restId) {
        applicationContext.setRestaurantId(restId);
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
