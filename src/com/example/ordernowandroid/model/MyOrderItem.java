package com.example.ordernowandroid.model;

import java.io.Serializable;
import java.util.HashMap;

public class MyOrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private FoodMenuItem foodMenuItem;
	private float quantity;
	private String notes;
	private HashMap<String, String> metaData;
	private OrderStatus itemStatus = OrderStatus.NULL; 

	public MyOrderItem(FoodMenuItem foodMenuItem, float quantity) {
		super();
		this.foodMenuItem = foodMenuItem;
		this.quantity = quantity;
	}
	
	public void setFoodMenuItem(FoodMenuItem foodMenuItem) {
		this.foodMenuItem = foodMenuItem;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public FoodMenuItem getFoodMenuItem() {
		return foodMenuItem;
	}

	public float getQuantity() {
		return quantity;
	}

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setMetaData(String key, String value) {
        if(metaData==null){
            metaData = new HashMap<String,String>();
        }
        metaData.put(key, value);
    }

    public HashMap<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(HashMap<String, String> metaData) {
       this.metaData = metaData; 
    }

    @Override
	public String toString() {
		return "MyOrderItem [item=" + foodMenuItem + ", quantity=" + quantity + "]";
	}

    public OrderStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(OrderStatus itemStatus) {
        this.itemStatus = itemStatus;
    }
    
}
