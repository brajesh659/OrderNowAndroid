package com.data.menu;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.ordernowandroid.model.MyOrderItem;

/**
 * 
 * @author Rohit
 * Class to wrap Server CustomerOrder object along with fields required for App UI View
 */

public class CustomerOrderWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomerOrder customerOrder;
	private ArrayList<MyOrderItem> myOrderItemList;

	public CustomerOrderWrapper (CustomerOrder customerOrder, ArrayList<MyOrderItem> myOrderItemList) {
		super();
		this.customerOrder = customerOrder;
		this.myOrderItemList = myOrderItemList;
	}

	public CustomerOrder getCustomerOrder() {
		return customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}

	public ArrayList<MyOrderItem> getMyOrderItemList() {
		return myOrderItemList;
	}

	public void setMyOrderItemList(ArrayList<MyOrderItem> myOrderItemList) {
		this.myOrderItemList = myOrderItemList;
	}

}

