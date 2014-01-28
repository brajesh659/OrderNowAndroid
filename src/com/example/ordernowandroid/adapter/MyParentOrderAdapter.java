package com.example.ordernowandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;

/**
 * 
 * @author Rohit
 *
 */

public class MyParentOrderAdapter extends ArrayAdapter<MyOrderItem> {

	private List<MyOrderItem> myParentOrderItemList;
	
	public MyParentOrderAdapter(Context context, List<MyOrderItem> myOrderItemList) {
		super(context, R.layout.my_parent_order, myOrderItemList);
		this.myParentOrderItemList = myOrderItemList;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_parent_order, null);
		}

		TextView itemName = (TextView) convertView.findViewById(R.id.parentItemName);		
		final TextView quantity = (TextView) convertView.findViewById(R.id.parentQuantity);
		final TextView itemTotalPrice = (TextView) convertView.findViewById(R.id.parentItemTotalPrice);
		
		String orderItemName = myParentOrderItemList.get(position).getFoodMenuItem().getItemName();
		Float orderItemQuantity = myParentOrderItemList.get(position).getQuantity();
		Float orderItemPrice = myParentOrderItemList.get(position).getFoodMenuItem().getItemPrice();		

		itemName.setText(orderItemName);	
		quantity.setText(Float.toString(orderItemQuantity));
		itemTotalPrice.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(orderItemPrice * orderItemQuantity));

		return convertView;
	}
}
