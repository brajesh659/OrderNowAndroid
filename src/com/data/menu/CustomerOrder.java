package com.data.menu;

import java.io.Serializable;
import java.util.Map;

public class CustomerOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Will contain the dish id's and quantity. Keeping the Quantity as float to
	 * accommodate for half and full dishes.
	 */	
	private Map<String, OrderDish> dishes;
	private String restaurantId;	

	private int subOrderId;
	private String customerId; //Customer ID same as parse object id.
	private String tableId;
	private String orderNote;
	
	public int getSubOrderId() {
		return subOrderId;
	}

	public void setSubOrderId(int subOrderId) {
		this.subOrderId = subOrderId;
	}

	public CustomerOrder(Map<String, OrderDish> dishes, String restaurantId,
			String customerId, String tableId, String orderNote) {
		super();
		this.dishes = dishes;
		this.restaurantId = restaurantId;

		this.customerId = customerId;
		this.tableId = tableId;
		this.orderNote = orderNote;
	}	
	
	/**
	 * order id would be fetched from the restaurant while constructing the
	 * order
	 * 
	 * @param dishes
	 * @param restaurantId
	 * @param customerId
	 * @param tableId
	 * @param orderNote
	 */

	public CustomerOrder(){}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Map<String, OrderDish> getDishes() {
		return dishes;
	}

	public void setDishes(Map<String, OrderDish> dishes) {
		this.dishes = dishes;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

}
