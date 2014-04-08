package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.data.menu.CustomerOrderWrapper;
import com.data.menu.Dish;
import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.MyOrderActivity;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;

public class MyOrderHistoryAdapter extends ArrayAdapter<CustomerOrderWrapper> {

	private ArrayList<CustomerOrderWrapper> myOrderHistoryList;

	public MyOrderHistoryAdapter(Context context, ArrayList<CustomerOrderWrapper> myOrderHistoryList) {
		super(context, R.layout.my_order_history_item, myOrderHistoryList);
		this.myOrderHistoryList = myOrderHistoryList;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_order_history_item, null);
		}
		
		final ApplicationState applicationContext = (ApplicationState) getContext().getApplicationContext();
		TextView orderDateView = (TextView) convertView.findViewById(R.id.orderDate);
		String orderDate = myOrderHistoryList.get(position).getCustomerOrder(applicationContext).getOrderNote(); //TODO: Change this to Order Date
		orderDateView.setText(orderDate);
		
		Button reorderNowBtn = (Button) convertView.findViewById(R.id.reorderNowBtn);
		
		reorderNowBtn.setOnClickListener(new Button.OnClickListener() {           
			@Override
			public void onClick(View v) {
				HashMap<String, MyOrderItem> foodItemQtyMap = new HashMap<String, MyOrderItem>();
				foodItemQtyMap.put("Cream of Veg", new MyOrderItem(new FoodMenuItem(new Dish("d0", "Cream of Veg", null, null, 95, true)), 2));
				foodItemQtyMap.put("Roasted Bell Pepper Soup", new MyOrderItem(new FoodMenuItem(new Dish("d1", "Roasted Bell Pepper Soup", null, null, 115, true)), 3));
				
				ApplicationState.setFoodMenuItemQuantityMap(applicationContext, foodItemQtyMap);
				Intent intent = new Intent(getContext(), MyOrderActivity.class);
				getContext().startActivity(intent);
			}
		});
		
		return convertView;
	}
	
}
