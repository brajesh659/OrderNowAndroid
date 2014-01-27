package com.example.ordernowandroid.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	private String notes;
	private HashMap<String, String> metaData;

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

}
