package com.example.ordernowandroid.model;

import java.io.Serializable;

public class MyOrderItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyOrderItem(String item, Integer quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "MyOrderItem [item=" + item + ", quantity=" + quantity + "]";
	}

	private String item;
	private Integer quantity;

	public String getItem() {
		return item;
	}

	public Integer getQuantity() {
		return quantity;
	}

}
