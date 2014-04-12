package com.example.ordernowandroid.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.data.menu.Dish;
import com.data.restaurant.OrderedDish;
import com.data.restaurant.RestaurantOrder;
import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.MyOrderActivity;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.FoodMenuItem;
import com.example.ordernowandroid.model.MyOrderItem;
import com.util.Utilities;

public class MyOrderHistoryAdapter extends ArrayAdapter<RestaurantOrder> {

	private ArrayList<RestaurantOrder> myOrderHistoryList;

	public MyOrderHistoryAdapter(Context context, ArrayList<RestaurantOrder> myOrderHistoryList) {
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
		TextView orderIdView = (TextView) convertView.findViewById(R.id.orderId);
		String orderId = myOrderHistoryList.get(position).getOrderId();
		orderIdView.setText("OrderId " + orderId);
		
		TextView orderDateView = (TextView) convertView.findViewById(R.id.orderDate);
		String orderDate = Utilities.defaultDateFormat(new Date(myOrderHistoryList.get(position).getCreatedAt()));
		orderDateView.setText(orderDate);
		
		Button reorderNowBtn = (Button) convertView.findViewById(R.id.reorderNowBtn);
		
		reorderNowBtn.setOnClickListener(new Button.OnClickListener() {           
			@Override
			public void onClick(View v) {
				HashMap<String, MyOrderItem> foodItemQtyMap = new HashMap<String, MyOrderItem>();
				List<OrderedDish> orderedDish = myOrderHistoryList.get(position).getDishes();
				
				String dishName,dishId;
				float dishPrice;
				Float dishQty;
				for(int i = 0; i < orderedDish.size(); i++) {
					dishName = orderedDish.get(i).getName();
					dishId = orderedDish.get(i).getDishId();
					dishPrice = (float) orderedDish.get(i).getPrice();
					dishQty = orderedDish.get(i).getDishQty();
					foodItemQtyMap.put(dishName, new MyOrderItem(new FoodMenuItem(new Dish(dishId, dishName, null, null, dishPrice, true)), dishQty));
				}
				
				ApplicationState.setFoodMenuItemQuantityMap(applicationContext, foodItemQtyMap);
				Intent intent = new Intent(getContext(), MyOrderActivity.class);
				getContext().startActivity(intent);
			}
		});
		
		return convertView;
	}
	
}
