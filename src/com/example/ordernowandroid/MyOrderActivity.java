package com.example.ordernowandroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.adapter.MyOrderAdapter;
import com.example.ordernowandroid.fragments.ConfirmOrderDialogFragment;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.google.gson.Gson;
import com.util.AsyncNetwork;
import com.util.URLBuilder;
import com.util.Utilities;

public class MyOrderActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("My Order");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		ArrayList<MyOrderItem> myOrderItemList = ApplicationState.getMyOrderItems(applicationContext);
		setContentView(R.layout.my_order_summary);

		Button addMoreItemsBtn = (Button) findViewById(R.id.addMoreItemsButton);
		Button cancelOrderBtn = (Button) findViewById(R.id.cancelOrderButton);
		Button confirmOrderBtn = (Button) findViewById(R.id.confirmOrderButton);        
		final TextView totalAmount = (TextView) findViewById(R.id.totalAmount);

		addMoreItemsBtn.setOnClickListener(new Button.OnClickListener() {           
			@Override
			public void onClick(View v) {
				ApplicationState.setOpenCategoryDrawer((ApplicationState) v.getContext().getApplicationContext(), false);
				Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
				startActivity(intent);
			}
		});

		cancelOrderBtn.setOnClickListener(new Button.OnClickListener() {            
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);            
				builder.setTitle("Cancel Order");
				builder.setMessage("Are you sure you want to cancel the order ?");
				builder.setPositiveButton(R.string.ok, new OnClickListener() {                  
					@Override
					public void onClick(DialogInterface dialog, int which) {                                                
						Toast.makeText(getApplicationContext(), "Order has been canceled.", Toast.LENGTH_LONG).show();
						ApplicationState.setFoodMenuItemQuantityMap((ApplicationState)getApplicationContext(), new HashMap<String, MyOrderItem>());
						Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
						startActivity(intent);
						finish();
					}
				});
				builder.setNegativeButton(R.string.cancel, null);                           
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		confirmOrderBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showConfirmOrderDialog();
			}
		}); 

		Float totalOrderAmount = (float) 0.00;
		for (MyOrderItem myOrderItem: myOrderItemList) {
			totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice()); 
		}

		totalAmount.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(totalOrderAmount)); 

		ListView myOrderListView = (ListView) findViewById(R.id.listMyOrder);

		MyOrderAdapter myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, myOrderItemList);
		myOrderListView.setAdapter(myOrderAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//Start the FoodMenuActivity with updated MyOrderItem List
		Intent intent = new Intent();
		setResult(RESULT_OK, intent); // Activity finished ok, return the data
		finish();
	}

	void showConfirmOrderDialog() {
		new ConfirmOrderDialogFragment().show(getFragmentManager(), "confirmOrderEditText");
	}

	public void doPositiveClick(String orderNote) {
		ArrayList<MyOrderItem> myOrderItemList = ApplicationState.getMyOrderItems((ApplicationState)getApplicationContext());
		String restaurantId = ApplicationState.getRestaurantId((ApplicationState)getApplicationContext());
		Log.i("MyOrderActivity ", restaurantId);

		CustomerOrderWrapper customerOrderWrapper = new CustomerOrderWrapper(myOrderItemList, orderNote);

		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		ApplicationState.setCustomerOrderWrapper(applicationContext, customerOrderWrapper);

		Gson gs = new Gson();
		String order = gs.toJson(customerOrderWrapper.getCustomerOrder(applicationContext));
		String encoded = "";
        try {
            encoded = URLEncoder.encode(order,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		String url = new URLBuilder().addPath(URLBuilder.Path.order)
				.addParam(URLBuilder.URLParam.order, encoded).build();

		try {
			String output = new AsyncNetwork().execute(url).get();
			JSONObject json = new JSONObject(output);
			String orderId = (String) json.get(URLBuilder.URLParam.orderId.toString());
			ApplicationState.setActiveOrderId(applicationContext, orderId);
			//subOrderId not used currently
			Integer subOrderId = (Integer) json.get(URLBuilder.URLParam.subOrderId.toString());
			Utilities.info("Order response: "+ output + " orderId "+ orderId + " subOrderId " + subOrderId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);
		Intent intent = new Intent(getApplicationContext(), MyParentOrderActivity.class);
		startActivity(intent);
		finish();
	}

	public void doNegativeClick() {}

}




