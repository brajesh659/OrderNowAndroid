package com.example.ordernowandroid.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllHistoryViewItem {

	private String restName;
	private String orderId;
	private Date orderDate;
	private List<String> dishnames;

	public AllHistoryViewItem(String restName, String orderId, Date orderDate,
			List<String> dishnames) {
		super();
		this.restName = restName;
		this.orderId = orderId;
		this.orderDate = orderDate;
		if (dishnames != null) {
			this.dishnames = dishnames;
		} else {
			this.dishnames = new ArrayList<String>();
		}
	}

	public String getRestName() {
		return restName;
	}

	public String getOrderId() {
		return orderId;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public List<String> getDishnames() {
		return dishnames;
	}
}
