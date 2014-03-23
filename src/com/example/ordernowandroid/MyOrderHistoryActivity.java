package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.data.menu.CustomerOrderWrapper;
import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.example.ordernowandroid.adapter.MyOrderHistoryAdapter;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;

public class MyOrderHistoryActivity extends Activity {

	private ArrayList<CustomerOrderWrapper> myOrderHistoryList = new ArrayList<CustomerOrderWrapper>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Past Orders");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_my_order_history);

		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		if(applicationContext.getMyOrderHistoryList() == null) { //TODO: Make call to Server by providing Table Id and Customer Id and remove the next LOC
			populateOrderHistoryLocally();		
			ApplicationState.setMyOrderHistoryList(applicationContext, myOrderHistoryList);
		} else {
			myOrderHistoryList = ApplicationState.getMyOrderHistoryList(applicationContext);
		}

		ListView myOrderHistoryListView = (ListView) findViewById(R.id.orderHistoryList);
		MyOrderHistoryAdapter myOrderHistoryAdapter = new MyOrderHistoryAdapter(this, myOrderHistoryList);
		myOrderHistoryListView.setAdapter(myOrderHistoryAdapter);
	}

	private void populateOrderHistoryLocally() {
		
		ArrayList<MyOrderItem> myOrderItemList = new ArrayList<MyOrderItem>();
		myOrderItemList.add(new MyOrderItem(new FoodMenuItem(new Dish("d0", "Cream of Veg", null, null, 95, FoodType.Veg, true)), 2));
		myOrderItemList.add(new MyOrderItem(new FoodMenuItem(new Dish("d1", "Roasted Bell Pepper Soup", null, null, 115, FoodType.Veg, true)), 3));
		
		CustomerOrderWrapper customerOrderWrapper1 = new CustomerOrderWrapper(myOrderItemList, "First Order");
		CustomerOrderWrapper customerOrderWrapper2 = new CustomerOrderWrapper(myOrderItemList, "Second Order");
		
		myOrderHistoryList.add(customerOrderWrapper1);
		myOrderHistoryList.add(customerOrderWrapper2);
	}
	
}
