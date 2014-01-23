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

	private List<MyOrderItem> orders;
    
	public MyOrderAdapter(Context context, List<MyOrderItem> orders) {
	    super(context, R.layout.my_order, orders);
        this.orders = orders;
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

		String orderItemName = orders.get(position).getFoodMenuItem().getItemName();
		float orderItemQuantity = orders.get(position).getQuantity();
		Float orderItemPrice = orders.get(position).getFoodMenuItem().getItemPrice();

		itemName.setText(orderItemName);	
		quantity.setText(Float.toString(orderItemQuantity));
		itemTotalPrice.setText("\u20B9" + " " + Float.toString(orderItemPrice * orderItemQuantity));

		final ImageButton decrementQtyBtn = (ImageButton) convertView.findViewById(R.id.decrementQtyButton);
		final ImageButton incrementQtyBtn = (ImageButton) convertView.findViewById(R.id.incrementQtyButton);

		decrementQtyBtn.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {				
				Integer qty = Integer.parseInt((String) quantity.getText());
				String orderItemPriceStr = (String) itemTotalPrice.getText();				

				if (orderItemPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
					orderItemPriceStr = orderItemPriceStr.substring(orderItemPriceStr.indexOf("\u20B9 ") + 1).trim();
				}

				Float orderItemPrice = Float.parseFloat(orderItemPriceStr);
				if (qty > 1){
					itemTotalPrice.setText("\u20B9" + " " + Float.toString((orderItemPrice/qty) * (qty - 1)));
					quantity.setText(Integer.toString(qty - 1));
					orders.get(position).setQuantity(--qty);					
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
				Integer qty = Integer.parseInt((String) quantity.getText());
				String orderItemPriceStr = (String) itemTotalPrice.getText();				

				if (orderItemPriceStr.indexOf("\u20B9 ") != -1){ //Strip Rupee Symbol from Total Price
					orderItemPriceStr = orderItemPriceStr.substring(orderItemPriceStr.indexOf("\u20B9 ") + 1).trim();
				}

				Float orderItemPrice = Float.parseFloat(orderItemPriceStr);
				if (qty == 0){
					Toast.makeText(getContext(), "Cannot determine Unit Price if the Quantity is zero", Toast.LENGTH_SHORT).show();					
				} else {
					itemTotalPrice.setText("\u20B9" + " " + Float.toString((orderItemPrice/qty) * (qty + 1)));
					quantity.setText(Integer.toString(qty + 1));
					orders.get(position).setQuantity(++qty);					
				}
			}
		});

		return convertView;
	}
}
