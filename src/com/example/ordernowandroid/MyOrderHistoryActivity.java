package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.data.menu.CustomerOrder;
import com.data.menu.CustomerOrderWrapper;
import com.data.menu.Dish;
import com.data.menu.FoodType;
import com.data.menu.OrderDish;
import com.example.ordernowandroid.adapter.MyOrderHistoryAdapter;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;
import com.parse.ParseInstallation;

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
		MyOrderHistoryAdapter myOrderHistoryAdapter = new MyOrderHistoryAdapter(MyOrderHistoryActivity.this, myOrderHistoryList);
		myOrderHistoryListView.setAdapter(myOrderHistoryAdapter);
	}

	private void populateOrderHistoryLocally() {
		Map<String, OrderDish> dishes = new HashMap<String, OrderDish>();
		dishes.put("d0", new OrderDish((float) (2)));
		dishes.put("d1", new OrderDish((float) (3)));
		
		ArrayList<MyOrderItem> myOrderItemList = new ArrayList<MyOrderItem>();
		myOrderItemList.add(new MyOrderItem(new FoodMenuItem(new Dish("d0", "Cream of Veg", null, null, 95, FoodType.Veg)), 2));
		myOrderItemList.add(new MyOrderItem(new FoodMenuItem(new Dish("d1", "Roasted Bell Pepper Soup", null, null, 115, FoodType.Veg)), 3));
		
		CustomerOrder customerOrder1 = new CustomerOrder(dishes, "R1", ParseInstallation.getCurrentInstallation().getObjectId().toString(), ApplicationState.getTableId((ApplicationState)getApplicationContext()), "First Order");
		CustomerOrderWrapper customerOrderWrapper1 = new CustomerOrderWrapper(customerOrder1, myOrderItemList);
		
		CustomerOrder customerOrder2 = new CustomerOrder(dishes, "R1", ParseInstallation.getCurrentInstallation().getObjectId().toString(), ApplicationState.getTableId((ApplicationState)getApplicationContext()), "Second Order");
		CustomerOrderWrapper customerOrderWrapper2 = new CustomerOrderWrapper(customerOrder2, myOrderItemList);
		
		myOrderHistoryList.add(customerOrderWrapper1);
		myOrderHistoryList.add(customerOrderWrapper2);
	}
	
}
