package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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

    private MyParentOrderAdapter myParentOrderAdapter;
    private IntentFilter filter;
    ArrayList<CustomerOrderWrapper> subOrderList = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		filter = new IntentFilter();
		filter.addAction(OrderNowConstants.ORDER_STATUS_RESET);
		
        Float totalOrderAmount = (float) 0.00;
        ApplicationState applicationContext = (ApplicationState)getApplicationContext();
        subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
        if(subOrderList==null){
            subOrderList = new ArrayList<CustomerOrderWrapper>();
        }
        
        OrderStatus orderStatus = OrderStatus.NULL;
        
        CustomerOrderWrapper customerOrderWrapper = ApplicationState.getCustomerOrderWrapper((ApplicationState)getApplicationContext());
        Utilities.info("Utilities + " + orderStatus.toString());
        
        if(customerOrderWrapper !=null) { 
            subOrderList.add(customerOrderWrapper);
        }
		
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
		myParentOrderAdapter = new MyParentOrderAdapter(this, subOrderList);
		subOrderListView.setAdapter(myParentOrderAdapter);
		myParentOrderAdapter.notifyDataSetChanged();
		
		requestBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyParentOrderActivity.this);
                builder.setTitle("Request Bill");

                boolean orderAcknowledgement = true;
                for (CustomerOrderWrapper customerOrderWrapper : subOrderList) {
                    orderAcknowledgement = customerOrderWrapper.hasResturantacknowledged() && orderAcknowledgement;
                }

                if (orderAcknowledgement == false) {
                    Toast.makeText(getApplicationContext(), "Please wait for resturant acknowledgment before requesting bill. ", Toast.LENGTH_LONG).show();
                    return;
                }

                final ApplicationState applicationContext = (ApplicationState) getApplicationContext();
                if (ApplicationState.getFoodMenuItemQuantityMap(applicationContext) != null && ApplicationState.getFoodMenuItemQuantityMap(applicationContext).size() > 0) {
                    builder.setMessage("You have items waiting to be ordered in the cart. Would you still like to request for the bill?");
                } else {
                    builder.setMessage("Would you like to request for the bill?");
                }

                builder.setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String orderId = ApplicationState.getActiveOrderId(applicationContext);
                        String url = new URLBuilder().addPath(URLBuilder.Path.serveTable).addAction(URLBuilder.URLAction.requestBill).addParam(URLBuilder.URLParam.orderId, orderId).build();
                        try {
                            new AsyncNetwork(null,MyParentOrderActivity.this).execute(url);

                            ArrayList<String> sharedPrefsToRemove = new ArrayList<String>();
                            sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
                            sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_TABLE_ID);
                            sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
                            OrderNowUtilities.removeSharedPreferences(getApplicationContext(), sharedPrefsToRemove);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } 

                        Toast.makeText(getApplicationContext(), "You will be receiving the bill very shortly!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), RestFeedbackActivity.class);
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
    protected void onResume() {
        if (statusResetreceiver != null) {
            registerReceiver(statusResetreceiver, filter);
            Utilities.info("On resume reciever");
        } else {
            Utilities.info("On resume null reciever");
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(OrderNowConstants.STATUS_CHANGE_NOTIFICATION_ID);
        ApplicationState applicationContext = (ApplicationState) getApplicationContext();
        //if application context is null that means its safe to refresh view from the prefernece storage
        if (ApplicationState.getCustomerOrderWrapper(applicationContext) == null) {
            refreshConfirmedOrderView();
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        if (statusResetreceiver != null) {
            registerReceiver(statusResetreceiver, filter);
            Utilities.info("On start reciever");
        } else {
            Utilities.info("On start null reciever");
        }
        super.onStart();
    }

    @Override
    protected void onRestart() {
        if (statusResetreceiver != null) {
            registerReceiver(statusResetreceiver, filter);
            Utilities.info("On onRestart reciever");
        } else {
            Utilities.info("On onRestart null reciever");
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (statusResetreceiver != null) {
            unregisterReceiver(statusResetreceiver);
            Utilities.info("On pause reciever");
        } else {
            Utilities.info("On pause null reciever");
        }
        super.onPause();
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    Utilities.info("on new intent "+intent.getAction());
	    refreshConfirmedOrderView();
	}

    private void refreshConfirmedOrderView() {
        ArrayList<CustomerOrderWrapper> subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
	    myParentOrderAdapter.clear();
	    myParentOrderAdapter.addAll(subOrderList);
	    myParentOrderAdapter.notifyDataSetChanged();
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
	
    public BroadcastReceiver statusResetreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onNewIntent(intent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(OrderNowConstants.STATUS_CHANGE_NOTIFICATION_ID);
            this.setResultCode(Activity.RESULT_OK);
        }
    };

	
}
