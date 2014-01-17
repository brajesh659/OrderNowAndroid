package com.example.ordernowandroid.model;

import java.io.Serializable;

/**
 * 
 * @author Rohit
 *
 */

public class FoodMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String itemName;
	private Integer itemPrice;

	public FoodMenuItem(String itemName, Integer itemPrice){
		this.setItemName(itemName);
		this.setItemPrice(itemPrice);
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
