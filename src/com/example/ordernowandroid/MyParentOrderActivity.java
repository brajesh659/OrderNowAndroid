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

	public static final String CUSTOMER_ORDER_WRAPPER = "customerOrderList";
	public static final String FOOD_MENU_CATEGORY_ID = "foodMenuCategoryId";
	protected static final String SUB_ORDER_LIST = "SubOrderList";

	private int categoryId;

	public static CustomerOrderWrapper customerOrderWrapper;
	private ArrayList<MyOrderItem> myOrderItemList = new ArrayList<MyOrderItem>();
	public static ArrayList<CustomerOrderWrapper> subOrdersFromDB;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_parent_order_summary);
		setTitle("Confirmed Order");

		Bundle b = getIntent().getExtras();
		customerOrderWrapper = (CustomerOrderWrapper) b.getSerializable(CUSTOMER_ORDER_WRAPPER);
		categoryId = b.getInt(FOOD_MENU_CATEGORY_ID);

		/**
		 * Fetch Sub Orders from Database //FIXME
		 * Add Current Order to SubOrder List
		 * Calculate Order Total and Display List of Sub-Orders 
		 */

		if (subOrdersFromDB == null) {
			subOrdersFromDB = new ArrayList<CustomerOrderWrapper>();
		} else {
			subOrdersFromDB = new ArrayList<CustomerOrderWrapper>();
			subOrdersFromDB.addAll((ArrayList<CustomerOrderWrapper>) b.getSerializable(SUB_ORDER_LIST));
		}

		TextView totalAmount = (TextView) findViewById(R.id.parentTotalAmount);
		Float totalOrderAmount = (float) 0.00;

		if(customerOrderWrapper !=null) {
			subOrdersFromDB.add(customerOrderWrapper);
		}

		for (CustomerOrderWrapper subOrder: subOrdersFromDB){			
			for (MyOrderItem myOrderItem: subOrder.getMyOrderItemList()) {
				myOrderItemList.add(myOrderItem);
				totalOrderAmount = totalOrderAmount + (myOrderItem.getQuantity() * myOrderItem.getFoodMenuItem().getItemPrice());
			}
		}		

		totalAmount.setText(OrderNowConstants.INDIAN_RUPEE_UNICODE + " " + Float.toString(totalOrderAmount));

		ListView myOrderListView = (ListView) findViewById(R.id.parentListMyOrder);
		MyParentOrderAdapter myParentOrderAdapter = new MyParentOrderAdapter(this, myOrderItemList);
		myOrderListView.setAdapter(myParentOrderAdapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed(); //FIXME: Persist the myOrderItem List Data on FoodMenuActivity Page		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
		intent.putExtra(FOOD_MENU_CATEGORY_ID, categoryId);
		intent.putExtra(SUB_ORDER_LIST, subOrdersFromDB);
		startActivity(intent);

		finish();		
	}

}
