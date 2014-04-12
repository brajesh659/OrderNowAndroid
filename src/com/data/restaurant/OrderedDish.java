package com.data.restaurant;

import com.data.menu.OrderDish;

public class OrderedDish extends OrderDish {

	private static final long serialVersionUID = 1L;

	private String dishId;
	private String name;
	private float price;

	public OrderedDish(OrderDish orderDish, String dishId, String name,
			float price) {
		super(orderDish);
		this.dishId = dishId;
		this.name = name;
		this.price = price;
	}

	public OrderedDish(Float dishQty) {
		super(dishQty);
	}

	public String getDishNote() {
		return dishNote;
	}

	public void setDishNote(String dishNote) {
		this.dishNote = dishNote;
	}

	public String getSpiceLevel() {
		return spiceLevel;
	}

	public void setSpiceLevel(String spiceLevel) {
		this.spiceLevel = spiceLevel;
	}

	public String getDishId() {
		return dishId;
	}

	public void setDishId(String dishId) {
		this.dishId = dishId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getQuatity() {
		return dishQty;
	}

	public void setQuatity(float quatity) {
		this.dishQty = quatity;
	}
}