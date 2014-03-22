package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.adapter.MyParentOrderAdapter;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.example.ordernowandroid.model.OrderStatus;
import com.util.AsyncNetwork;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class MyParentOrderActivity extends Activity {

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String action = "";
		ArrayList<String> unAvailableDishes = null;
		Bundle b = getIntent().getExtras();
		if (b != null) {
			action = b.getString(OrderNowConstants.ACTION);
			Utilities.info("Utilities + bundle not null" + action);
			unAvailableDishes = (ArrayList<String>) b.getSerializable(OrderNowConstants.UNAVAILABLEITEMS);
			Utilities.info("Utilities in bundle + " + unAvailableDishes);
		}

		Utilities.info("Utilities +" + action);
		OrderStatus orderStatus = OrderStatus.Sent;
		if (action != null && action.trim() != "") {
			orderStatus = OrderNowConstants.actionToOrderStatusMap.get(action);
		}
		setContentView(R.layout.my_parent_order_summary);
		setTitle("Confirmed Order");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		ArrayList<CustomerOrderWrapper> subOrderList = ApplicationState.getSubOrderList(applicationContext);

		TextView totalAmount = (TextView) findViewById(R.id.parentTotalAmount);
		Button requestBillButton = (Button) findViewById(R.id.requestBillButton);
		Float totalOrderAmount = (float) 0.00;

		CustomerOrderWrapper customerOrderWrapper = ApplicationState.getCustomerOrderWrapper((ApplicationState)getApplicationContext());
		Utilities.info("Utilities + " + orderStatus.toString());
		if(customerOrderWrapper !=null) {
			customerOrderWrapper.modifyItemStatus(orderStatus, unAvailableDishes);
			subOrderList.add(customerOrderWrapper);
			ApplicationState.setCustomerOrderWrapper(applicationContext, null);
			
			//Only update Shared Prefs Object when there is a new suborder
			OrderNowUtilities.putObjectToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST, subOrderList);
		}

		/*Hack this should be based on order id.*/
		subOrderList.get(subOrderList.size()-1).modifyItemStatus(orderStatus, unAvailableDishes);

		for (CustomerOrderWrapper subOrder:subOrderList) {			
			for (MyOrderItem myOrderItem: subOrder.getMyOrderItemList()) {
				totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice());
			}
		}

		totalAmount.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(totalOrderAmount));

		ListView subOrderListView = (ListView) findViewById(R.id.subOrderList);
		MyParentOrderAdapter myParentOrderAdapter = new MyParentOrderAdapter((ApplicationState)getApplicationContext(), subOrderList);
		subOrderListView.setAdapter(myParentOrderAdapter);

		requestBillButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyParentOrderActivity.this);
				builder.setTitle("Request Bill");
				
				final ApplicationState applicationContext = (ApplicationState) getApplicationContext();
				if(ApplicationState.getFoodMenuItemQuantityMap(applicationContext) != null && ApplicationState.getFoodMenuItemQuantityMap(applicationContext).size() > 0){
					builder.setMessage("You have items waiting to be ordered in the Cart. Would you still like to request for the bill?");
				} else {
					builder.setMessage("Would you like to request for the bill?");
				}
				
				builder.setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String orderId = ApplicationState.getActiveOrderId(applicationContext);
						String url = new URLBuilder()
						.addPath(URLBuilder.Path.serveTable)
						.addAction(URLBuilder.URLAction.requestBill)
						.addParam(URLBuilder.URLParam.orderId,
								orderId).build();
						try {
							new AsyncNetwork().execute(url).get();
							
							ArrayList<String> sharedPrefsToRemove = new ArrayList<String>();
							sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
							sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_TABLE_ID);
							sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
							OrderNowUtilities.removeSharedPreferences(getApplicationContext(), sharedPrefsToRemove);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}

						Toast.makeText(getApplicationContext(),
								"You will be receiving the bill very shortly!",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(getApplicationContext(),RestFeedbackActivity.class);
						startActivity(intent);
						finish();
					}
				});
				builder.setNegativeButton(R.string.no, null);
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.waiter:
			FoodMenuActivity.callWaiterFunction(MyParentOrderActivity.this);			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirmed_page_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
