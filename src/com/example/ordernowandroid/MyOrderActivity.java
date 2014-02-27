package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.CustomerOrder;
import com.data.menu.CustomerOrderWrapper;
import com.data.menu.OrderDish;
import com.example.ordernowandroid.adapter.MyOrderAdapter;
import com.example.ordernowandroid.fragments.ConfirmOrderDialogFragment;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.google.gson.Gson;
import com.parse.ParseInstallation;
import com.util.AsyncNetwork;
import com.util.Utilities;

public class MyOrderActivity extends Activity {
	private static final String TEXT_COMMENT = "TextComment"; //FIXME: Make the Properties names more readable
	public static final String SPICE_LEVEL = "SpiceLevel";

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
				onBackPressed();
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
		OrderDish orderDish;
		Map<String, OrderDish> dishes = new HashMap<String, OrderDish>();
		ArrayList<MyOrderItem> myOrderItemList = ApplicationState.getMyOrderItems((ApplicationState)getApplicationContext());
		for (MyOrderItem myOrderItem : myOrderItemList) {
			if (myOrderItem.getMetaData() != null) {
				orderDish = new OrderDish(myOrderItem.getQuantity(), myOrderItem.getMetaData().get(TEXT_COMMENT), myOrderItem.getMetaData().get(SPICE_LEVEL));	
			} else {
				orderDish = new OrderDish(myOrderItem.getQuantity());
			}
            // Clean Dish Ingredient if present
            if (myOrderItem.getFoodMenuItem().isItemCustomizable()) {
                orderDish.setSelectedOptions(myOrderItem.getFoodMenuItem().getCurrentSelectedIngredients());
                ApplicationState.cleanDishSelectedIngredients((ApplicationState) getApplicationContext(), myOrderItem
                        .getFoodMenuItem().getItemName());
            }
            dishes.put(myOrderItem.getFoodMenuItem().getDishId(), orderDish);
        }

		CharSequence text = ParseInstallation.getCurrentInstallation().getObjectId();


		CustomerOrder customerOrder = new CustomerOrder(dishes, "R1",
				text.toString(), ApplicationState.getTableId((ApplicationState)getApplicationContext()), orderNote);
		CustomerOrderWrapper customerOrderWrapper = new CustomerOrderWrapper(customerOrder, myOrderItemList);

		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		ApplicationState.setCustomerOrderWrapper(applicationContext, customerOrderWrapper);

		Gson gs = new Gson();
		String url = "http://ordernow.herokuapp.com/order?order="
				+ gs.toJson(customerOrder);
		try {
			new AsyncNetwork().execute(url).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ApplicationState.setFoodMenuItemQuantityMap(applicationContext, new HashMap<String, MyOrderItem>());
		Intent intent = new Intent(getApplicationContext(), MyParentOrderActivity.class);
		startActivity(intent);
		finish();
	}

	public void doNegativeClick() {
	}

}




