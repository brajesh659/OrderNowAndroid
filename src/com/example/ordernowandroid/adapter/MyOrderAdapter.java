package com.example.ordernowandroid.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.model.MyOrderItem;

/**
 * 
 * @author Rohit
 *
 */

public class MyOrderAdapter extends ArrayAdapter<MyOrderItem> {

	private List<MyOrderItem> myOrderItemList;

	public MyOrderAdapter(Context context, List<MyOrderItem> myOrderItemList) {
		super(context, R.layout.my_order, myOrderItemList);
		this.myOrderItemList = myOrderItemList;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.my_order, null);
		}

		TextView itemName = (TextView) convertView.findViewById(R.id.itemName);		
		final TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
		final TextView itemTotalPrice = (TextView) convertView.findViewById(R.id.itemTotalPrice);

		RelativeLayout r = (RelativeLayout) ((ViewGroup) parent).getParent(); // This is to get the Parent View of the List View
		final TextView orderTotalPriceView = (TextView) r.findViewById(R.id.relativeBtnLayout).findViewById(R.id.totalAmount);
		
		String orderItemName = myOrderItemList.get(position).getFoodMenuItem().getItemName();
		Float orderItemQuantity = myOrderItemList.get(position).getQuantity();
		Float orderItemPrice = myOrderItemList.get(position).getFoodMenuItem().getItemPrice();		

		itemName.setText(orderItemName);	
		quantity.setText(Float.toString(orderItemQuantity));
		itemTotalPrice.setText("\u20B9" + " " + Float.toString(orderItemPrice * orderItemQuantity));

		final ImageButton decrementQtyBtn = (ImageButton) convertView.findViewById(R.id.decrementQtyButton);
		final ImageButton incrementQtyBtn = (ImageButton) convertView.findViewById(R.id.incrementQtyButton);

		decrementQtyBtn.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {				
				Float qty = Float.parseFloat((String) quantity.getText());
				String orderItemPriceStr = (String) itemTotalPrice.getText();				

				if (orderItemPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
					orderItemPriceStr = orderItemPriceStr.substring(orderItemPriceStr.indexOf("\u20B9 ") + 1).trim();
				}

				final Float orderItemPrice = Float.parseFloat(orderItemPriceStr);
				if (qty > 1) {
					itemTotalPrice.setText("\u20B9" + " " + Float.toString((orderItemPrice/qty) * (qty - 1)));
					quantity.setText(Float.toString(qty - 1));					
					
					String orderTotalPriceStr = (String) orderTotalPriceView.getText();
					if (orderTotalPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
						orderTotalPriceStr = orderTotalPriceStr.substring(orderTotalPriceStr.indexOf("\u20B9 ") + 1).trim();
					}
					Float newOrderTotalPrice = Float.parseFloat(orderTotalPriceStr) - (orderItemPrice/qty);					
					orderTotalPriceView.setText("\u20B9" + " " + Float.toString(newOrderTotalPrice));					
					myOrderItemList.get(position).setQuantity(qty - 1);				
				} else if (qty == 1){
					//Show Dialog and Remove Item from ListView on Positive Button Action
					AlertDialog.Builder builder = new AlertDialog.Builder(getContext());            
					builder.setTitle("Remove Item");
					builder.setMessage("Are you sure you want to remove this item from the order ?");
					builder.setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							remove(getItem(position));
							notifyDataSetChanged();
							String orderTotalPriceStr = (String) orderTotalPriceView.getText();
							if (orderTotalPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
								orderTotalPriceStr = orderTotalPriceStr.substring(orderTotalPriceStr.indexOf("\u20B9 ") + 1).trim();
							}
							Float newOrderTotalPrice = Float.parseFloat(orderTotalPriceStr) - (orderItemPrice);					
							orderTotalPriceView.setText("\u20B9" + " " + Float.toString(newOrderTotalPrice));
						}
					});

					builder.setNegativeButton(R.string.cancel, null);	            	            
					AlertDialog alert = builder.create();
					alert.show();					
				} else {
					Toast.makeText(getContext(), "Quantity cannnot be decreased below zero", Toast.LENGTH_SHORT).show();
				}
			}
		});

		incrementQtyBtn.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {				
				Float qty = Float.parseFloat((String) quantity.getText());
				String orderItemPriceStr = (String) itemTotalPrice.getText();				

				if (orderItemPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
					orderItemPriceStr = orderItemPriceStr.substring(orderItemPriceStr.indexOf("\u20B9 ") + 1).trim();
				}

				Float orderItemPrice = Float.parseFloat(orderItemPriceStr);
				if (qty == 0){
					Toast.makeText(getContext(), "Cannot determine Unit Price if the Quantity is zero", Toast.LENGTH_SHORT).show();					
				} else {
					itemTotalPrice.setText("\u20B9" + " " + Float.toString((orderItemPrice/qty) * (qty + 1)));
					quantity.setText(Float.toString(qty + 1));					
					
					String orderTotalPriceStr = (String) orderTotalPriceView.getText();
					if (orderTotalPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
						orderTotalPriceStr = orderTotalPriceStr.substring(orderTotalPriceStr.indexOf("\u20B9 ") + 1).trim();
					}
					Float newOrderTotalPrice = Float.parseFloat(orderTotalPriceStr) + (orderItemPrice/qty);					
					orderTotalPriceView.setText("\u20B9" + " " + Float.toString(newOrderTotalPrice));
					myOrderItemList.get(position).setQuantity(qty + 1);
				}
			}
		});

		return convertView;
	}
}
