package com.example.ordernowandroid.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ordernowandroid.FoodMenuActivity;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.MyOrderAdapter;
import com.example.ordernowandroid.model.MyOrderItem;

public class MyOrderFragment extends Fragment {

	private List<MyOrderItem> myOrders;

	public void setMyOrders(List<MyOrderItem> myOrders) {
		this.myOrders = myOrders;
	}

	public MyOrderFragment() {
		myOrders = new ArrayList<MyOrderItem>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.my_order_summary, container, false);
		
		Button addMoreItemsBtn = (Button) rootView.findViewById(R.id.addMoreItemsButton);
		Button cancelOrderBtn = (Button) rootView.findViewById(R.id.cancelOrderButton);
		Button confirmOrderBtn = (Button) rootView.findViewById(R.id.confirmOrderButton);
		
		addMoreItemsBtn.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		
		cancelOrderBtn.setOnClickListener(new Button.OnClickListener() {			
			public void onClick(View v) {
	            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());            
	            builder.setTitle("Cancel Order");
	            builder.setMessage("Are you sure you want to cancel the order ?");
	            builder.setPositiveButton(R.string.ok, new OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {												
						Toast.makeText(getActivity(), "Order has been canceled.", Toast.LENGTH_LONG).show();
						//Clear the Selected Quantities and Start the Food Menu Activity again
						Intent intent = new Intent(getActivity(), FoodMenuActivity.class);
						startActivity(intent);												
					}
				});
	            builder.setNegativeButton(R.string.cancel, null);           	            
	            AlertDialog alert = builder.create();
                alert.show();
			}
		});
		
		confirmOrderBtn.setOnClickListener(new Button.OnClickListener() {
	        public void onClick(View v) {
	            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());            
	            builder.setTitle("Confirm Order");
	            builder.setMessage("Are you sure you want to confirm the order ?");
	            builder.setPositiveButton(R.string.ok, new OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//TODO: Back End Integration, Make an API Call to let the Server know about Order Confirmation
						Toast.makeText(getActivity(), "Order has been confirmed.", Toast.LENGTH_LONG).show();
						//TODO: Check how should the flow be
						/*Intent intent = new Intent(getActivity(), FoodMenuActivity.class);
						startActivity(intent);*/
					}
				});
	            builder.setNegativeButton(R.string.cancel, null);	            	            
	            AlertDialog alert = builder.create();
                alert.show();
	        }
	    });	
				
		TextView totalAmount = (TextView) rootView.findViewById(R.id.totalAmount);
		Float totalOrderAmount = (float) 0.00;
		for (MyOrderItem myOrderItem: myOrders) {
			totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice()); 
		}
		
		totalAmount.setText("\u20B9" + " " + Float.toString(totalOrderAmount)); //UniCode for Rupee Symbol. HardCoding it here for now, cannot get R.string.id to make it work
				
		ListView myOrderListView = (ListView) rootView.findViewById(R.id.listMyOrder);
		
		
		MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getActivity(), myOrders);
		myOrderListView.setAdapter(myOrderAdapter);
		return rootView;
	}

}
