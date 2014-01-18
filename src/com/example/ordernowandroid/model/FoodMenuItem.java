package com.example.ordernowandroid.model;

import java.io.Serializable;

import com.data.menu.FoodType;

/**
 * 
 * @author Rohit
 *
 */

public class FoodMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String itemName;
	private Integer itemPrice;
	private FoodType foodType;

	public FoodMenuItem(String itemName, Integer itemPrice, FoodType foodType){
		this.setItemName(itemName);
		this.setItemPrice(itemPrice);
		this.foodType = foodType;
	}

	public String getItemName() {
		return itemName;
	}

	private void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemPrice() {
		return itemPrice;
	}

	private void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}	
	
	public FoodType getFoodType() {
		
		return foodType;
	}

	@Override
	public String toString() {
		return itemName + " " + itemPrice;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o instanceof FoodMenuItem) {
	        FoodMenuItem fmi = (FoodMenuItem) o;
	        if(this.itemName.equals(fmi.getItemName())) {
	            return this.itemPrice.equals(fmi.getItemPrice());
	        }
	    }
	    return false;
	}
	
	@Override
	public int hashCode() {
	    return this.toString().hashCode();
	}

}
