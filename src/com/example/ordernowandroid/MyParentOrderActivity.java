package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.adapter.MyParentOrderAdapter;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;

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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		ApplicationState.setOpenCategoryDrawer((ApplicationState) getApplicationContext(), true); //FIXME: Persist the myOrderItem List Data on FoodMenuActivity Page
		Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
		startActivity(intent);
		finish();		
	}

}
