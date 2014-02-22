package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.model.MyOrderItem;

public class ApplicationState extends Application {
	private String tableId;
	private ArrayList<MyOrderItem> myOrderItems;
	private int categoryId;
	private MenuFilter menuFilter;
	private HashMap<String, MyOrderItem> foodMenuItemQuantityMap;
	private CustomerOrderWrapper customerOrderWrapper;
	private ArrayList<CustomerOrderWrapper> subOrdersFromDB;

	public ApplicationState() {
		myOrderItems = new ArrayList<MyOrderItem>();
		menuFilter = new MenuFilter();
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public ArrayList<MyOrderItem> getMyOrderItems() {
		return myOrderItems;
	}

	public void setMyOrderItems(ArrayList<MyOrderItem> myOrderItems) {
		this.myOrderItems = myOrderItems;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public static int getCategoryId(ApplicationState applicationContext) {
		return applicationContext.getCategoryId();

	}

	public static void setCategoryId(ApplicationState applicationContext,
			int categoryId) {
		applicationContext.setCategoryId(categoryId);
	}

	public static ArrayList<MyOrderItem> getMyOrderItems(
			ApplicationState applicationContext) {
		HashMap<String, MyOrderItem> foodMenuItemQuantityMap = applicationContext
				.getFoodMenuItemQuantityMap();
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

	public HashMap<String, MyOrderItem> getFoodMenuItemQuantityMap() {
		if (foodMenuItemQuantityMap == null)
			foodMenuItemQuantityMap = new HashMap<String, MyOrderItem>();
		return foodMenuItemQuantityMap;
	}

	public static HashMap<String, MyOrderItem> getFoodMenuItemQuantityMap(
			ApplicationState applicationContext) {
		return applicationContext.getFoodMenuItemQuantityMap();
	}

	public void setFoodMenuItemQuantityMap(
			HashMap<String, MyOrderItem> foodMenuItemQuantityMap) {
		this.foodMenuItemQuantityMap = foodMenuItemQuantityMap;
	}

	public static void setFoodMenuItemQuantityMap(
			ApplicationState applicationContext,
			HashMap<String, MyOrderItem> foodMenuItemQuantityMap) {
		applicationContext.setFoodMenuItemQuantityMap(foodMenuItemQuantityMap);
	}

	public CustomerOrderWrapper getCustomerOrderWrapper() {
		return customerOrderWrapper;
	}

	public static CustomerOrderWrapper getCustomerOrderWrapper(
			ApplicationState applicationContext) {
		return applicationContext.getCustomerOrderWrapper();
	}

	public void setCustomerOrderWrapper(
			CustomerOrderWrapper customerOrderWrapper) {
		this.customerOrderWrapper = customerOrderWrapper;
	}

	public static void setCustomerOrderWrapper(
			ApplicationState applicationContext,
			CustomerOrderWrapper customerOrderWrapper) {
		applicationContext.setCustomerOrderWrapper(customerOrderWrapper);
	}

	public ArrayList<CustomerOrderWrapper> getSubOrdersFromDB() {
	    if (subOrdersFromDB==null){
	        subOrdersFromDB = new ArrayList<CustomerOrderWrapper>();
	    }
		return subOrdersFromDB;
	}

	public static ArrayList<CustomerOrderWrapper> getSubOrdersFromDB(
			ApplicationState applicationContext) {
		return applicationContext.getSubOrdersFromDB();
	}

	public void setSubOrdersFromDB(
			ArrayList<CustomerOrderWrapper> subOrdersFromDB) {
		this.subOrdersFromDB = subOrdersFromDB;
	}

	public static void setSubOrdersFromDB(ApplicationState applicationContext,
			ArrayList<CustomerOrderWrapper> subOrdersFromDB) {
		applicationContext.setSubOrdersFromDB(subOrdersFromDB);
	}

}
