package com.example.ordernowandroid.model;

import java.io.Serializable;

public class MyOrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	public MyOrderItem(FoodMenuItem foodMenuItem, float quantity) {
		super();
		this.foodMenuItem = foodMenuItem;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "MyOrderItem [item=" + foodMenuItem + ", quantity=" + quantity + "]";
	}

	private FoodMenuItem foodMenuItem;
	private float quantity;

	public void setFoodMenuItem(FoodMenuItem foodMenuItem) {
		this.foodMenuItem = foodMenuItem;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public FoodMenuItem getFoodMenuItem() {
		return foodMenuItem;
	}

	public float getQuantity() {
		return quantity;
	}

}
