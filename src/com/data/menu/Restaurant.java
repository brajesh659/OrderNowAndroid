package com.data.menu;

import java.util.List;
import java.util.Map;

import com.example.ordernowandroid.filter.MenuPropertyKey;
import com.example.ordernowandroid.filter.MenuPropertyValue;

public class Restaurant {
	private String rId;
	private String name;
	private String address;
	private String contactInfo;
	private String img;
	private Menu menu;
    private Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters;

    public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	public String getrId() {
		return rId;
	}
	public void setrId(String rId) {
		this.rId = rId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public Map<MenuPropertyKey, List<MenuPropertyValue>> getAvailableFilters() {
	    return availableFilters;
	}	
	   
    public void setAvailableFilters(Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters) {
        this.availableFilters = availableFilters;
    }
	
	@Override
	public String toString() {
	    return "Res = "+ name + " " + menu.toString();
	}
}
