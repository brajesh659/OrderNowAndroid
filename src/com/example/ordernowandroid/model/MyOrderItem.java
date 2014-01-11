package com.example.ordernowandroid.model;

public class MyOrderItem {
	public MyOrderItem(String item, float quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}

	private String item;
	private float quantity;

	public String getItem() {
		return item;
	}

	public float getQuantity() {
		return quantity;
	}

}
