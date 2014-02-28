package com.example.ordernowandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;

public class MyParentOrderAdapter extends ArrayAdapter<CustomerOrderWrapper> {

	private static final String TEXT_COMMENT = "TextComment";
	private List<CustomerOrderWrapper> subOrderList;

	public MyParentOrderAdapter(Context context, List<CustomerOrderWrapper> subOrderList) {
		super(context, R.layout.my_parent_order, subOrderList);
		this.subOrderList = subOrderList;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_parent_order, null);
		}

		TextView subOrderNoteView = (TextView) convertView.findViewById(R.id.subOrderNote);
		
		//Implement a List View inside a List View Example
		LinearLayout list = (LinearLayout) convertView.findViewById(R.id.subOrderItemListLinearLayout);
		list.removeAllViews();

		for (MyOrderItem myOrderItem : subOrderList.get(position).getMyOrderItemList()) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View line = li.inflate(R.layout.my_parent_order_item, null);

			TextView itemName = (TextView) line.findViewById(R.id.parentItemName);
			TextView itemNote = (TextView) line.findViewById(R.id.parentItemNote);
			final TextView quantity = (TextView) line.findViewById(R.id.parentQuantity);
			final TextView itemTotalPrice = (TextView) line.findViewById(R.id.parentItemTotalPrice);

			String orderItemName = myOrderItem.getFoodMenuItem().getItemName();
			String orderItemNote = null;
			if (myOrderItem.getMetaData() != null) {
				orderItemNote = myOrderItem.getMetaData().get(TEXT_COMMENT);
			}
			Float orderItemQuantity = myOrderItem.getQuantity();
			Float orderItemPrice = myOrderItem.getFoodMenuItem().getItemPrice();		

			itemName.setText(orderItemName);
			if(orderItemNote != null) {
				itemNote.setText(orderItemNote);
			} else {
				itemNote.setVisibility(View.GONE);
				itemName.setGravity(Gravity.CENTER_VERTICAL);	
			}
			quantity.setText(Float.toString(orderItemQuantity));
			itemTotalPrice.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(orderItemPrice * orderItemQuantity));

			list.addView(line);
		}

		String subOrderNote = subOrderList.get(position).getCustomerOrder().getOrderNote();

		if(subOrderNote != null) {
			subOrderNoteView.setText(subOrderNote);
		} else {
			subOrderNoteView.setVisibility(View.GONE);
		}
		return convertView;
	}
}
