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
import com.util.AsyncNetwork;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;

public class MyParentOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_parent_order_summary);
		setTitle("Confirmed Order");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		ArrayList<CustomerOrderWrapper> subOrdersFromDB = ApplicationState.getSubOrdersFromDB(applicationContext);

		TextView totalAmount = (TextView) findViewById(R.id.parentTotalAmount);
		Button requestBillButton = (Button) findViewById(R.id.requestBillButton);
		Float totalOrderAmount = (float) 0.00;

		CustomerOrderWrapper customerOrderWrapper = ApplicationState.getCustomerOrderWrapper((ApplicationState)getApplicationContext());
		if(customerOrderWrapper !=null) {
			subOrdersFromDB.add(customerOrderWrapper);
			ApplicationState.setCustomerOrderWrapper(applicationContext, null);
		}
		
		for (CustomerOrderWrapper subOrder:subOrdersFromDB) {			
			for (MyOrderItem myOrderItem: subOrder.getMyOrderItemList()) {
				totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice());
			}
		}
		
		totalAmount.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(totalOrderAmount));

		ListView subOrderListView = (ListView) findViewById(R.id.subOrderList);
		MyParentOrderAdapter myParentOrderAdapter = new MyParentOrderAdapter(this, subOrdersFromDB);
		subOrderListView.setAdapter(myParentOrderAdapter);
		
		requestBillButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyParentOrderActivity.this);
				builder.setTitle("Request Bill");
				builder.setMessage("Are you done with your order ?");
				builder.setPositiveButton(R.string.yes,
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ApplicationState applicationContext = (ApplicationState) getApplicationContext();
								String tableId = applicationContext
										.getTableId();
								String url = new URLBuilder()
										.addPath(URLBuilder.Path.serveTable)
										.addAction(URLBuilder.URLAction.requestBill)
										.addParam(URLBuilder.URLParam.tableId,
												tableId).build();
								try {
									new AsyncNetwork().execute(url).get();
									// clear active tableId/restId preferences
									OrderNowUtilities
											.putKeyToSharedPreferences(
													getApplicationContext(),
													OrderNowConstants.KEY_ACTIVE_TABLE_ID,
													"");
									OrderNowUtilities
											.putKeyToSharedPreferences(
													getApplicationContext(),
													OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID,
													"");
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
								
								Toast.makeText(getApplicationContext(),
										"Your bill is getting ready",
										Toast.LENGTH_LONG).show();
								Intent intent = new Intent(getApplicationContext(),
										RestFeedbackActivity.class);
								startActivity(intent);
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
		case android.R.id.home:
			onBackPressed();		
			return true;
		case R.id.waiter:
			FoodMenuActivity.callWaiterFunction(MyParentOrderActivity.this);			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
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
