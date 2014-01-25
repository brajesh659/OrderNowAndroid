package com.data.menu;

import java.util.Map;

public class CustomerOrder {
	/*
	 * Will contain the dish id's and quantity. Keeping the Quantity as float to
	 * accommodate for half and full dishes.
	 */

	private Map<String, Float> dishes;

	public CustomerOrder(Map<String, Float> dishes, String restaurantId,
			String orderId, String customerId, String tableId) {
		super();
		this.dishes = dishes;
		this.restaurantId = restaurantId;
		this.orderId = orderId;
		this.customerId = customerId;
		this.tableId = tableId;
	}

	private String restaurantId;
	/*
	 * Giving an order id so that the communication between the Restaurant and
	 * Client can happen.
	 */
	private String orderId;
	private String tableId;

	/*
	 * Customer ID same as parse object id.
	 */
	private String customerId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Map<String, Float> getDishes() {
		return dishes;
	}

	public void setDishes(Map<String, Float> dishes) {
		this.dishes = dishes;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
}
