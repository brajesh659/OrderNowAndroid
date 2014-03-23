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
import com.parse.ParseAnalytics;
import com.util.AsyncNetwork;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class MyParentOrderActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		ParseAnalytics.trackAppOpened(getIntent());
		
        Float totalOrderAmount = (float) 0.00;
        ApplicationState applicationContext = (ApplicationState)getApplicationContext();
        ArrayList<CustomerOrderWrapper> subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
        if(subOrderList==null){
            subOrderList = new ArrayList<CustomerOrderWrapper>();
        }
        
        OrderStatus orderStatus = OrderStatus.NULL;
        
        CustomerOrderWrapper customerOrderWrapper = ApplicationState.getCustomerOrderWrapper((ApplicationState)getApplicationContext());
        Utilities.info("Utilities + " + orderStatus.toString());
        
        if(customerOrderWrapper !=null) { 
            customerOrderWrapper.modifyItemStatus(OrderStatus.Sent, null);
            subOrderList.add(customerOrderWrapper);
            ApplicationState.setCustomerOrderWrapper(applicationContext, null);
            
        }

        //Only update Shared Prefs Object when there is a new suborder
        OrderNowUtilities.putObjectToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST, subOrderList);

		
		setContentView(R.layout.my_parent_order_summary);
		TextView totalAmount = (TextView) findViewById(R.id.parentTotalAmount);
        Button requestBillButton = (Button) findViewById(R.id.requestBillButton);
		setTitle("Confirmed Order");
		getActionBar().setDisplayHomeAsUpEnabled(true);



		for (CustomerOrderWrapper subOrder:subOrderList) {			
			for (MyOrderItem myOrderItem: subOrder.getMyOrderItemList()) {
				totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice());
			}
		}

		totalAmount.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(totalOrderAmount));

		ListView subOrderListView = (ListView) findViewById(R.id.subOrderList);
		MyParentOrderAdapter myParentOrderAdapter = new MyParentOrderAdapter(this, subOrderList);
		subOrderListView.setAdapter(myParentOrderAdapter);
		myParentOrderAdapter.notifyDataSetChanged();
		
		requestBillButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MyParentOrderActivity.this);
				builder.setTitle("Request Bill");
				
				final ApplicationState applicationContext = (ApplicationState) getApplicationContext();
				if(ApplicationState.getFoodMenuItemQuantityMap(applicationContext) != null && ApplicationState.getFoodMenuItemQuantityMap(applicationContext).size() > 0){
					builder.setMessage("You have items waiting to be ordered in the cart. Would you still like to request for the bill?");
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

	@Override
	public void onBackPressed() {
		ApplicationState.setOpenCategoryDrawer((ApplicationState) getApplicationContext(), true); //FIXME: Persist the myOrderItem List Data on FoodMenuActivity Page
		Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
		startActivity(intent);
		finish();		
	}
	
}
