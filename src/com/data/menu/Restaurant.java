package com.data.menu;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ordernowandroid.filter.MenuPropertyKey;
import com.example.ordernowandroid.filter.MenuPropertyValue;

public class Restaurant {
	private String _id;
	private String img;
	private String name;
	private String address;
	private String contactInfo;
	private Menu menu;
	private Map<String, Integer> tableInformation;
	private Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters;

	private Timestamp lastUpdatedAt;


	public Timestamp getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Timestamp lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public static HashMap<String, Restaurant> _cache = new HashMap<String, Restaurant>();

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getrId() {
		return _id;
	}

	public void setrId(String rId) {
		this._id = rId;
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

	public Map<String, Integer> getTableInformation() {
		return tableInformation;
	}

	public void setTableInformation(Map<String, Integer> tableInformation) {
		this.tableInformation = tableInformation;
	}


	public Map<MenuPropertyKey, List<MenuPropertyValue>> getAvailableFilters() {
		return availableFilters;
	}

	public void setAvailableFilters(
			Map<MenuPropertyKey, List<MenuPropertyValue>> availableFilters) {
		this.availableFilters = availableFilters;
	}

	@Override
	public String toString() {
		return "Res = " + name + " " + menu.toString();
	}
}
