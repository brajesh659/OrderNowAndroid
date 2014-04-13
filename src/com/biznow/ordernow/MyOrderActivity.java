package com.biznow.ordernow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.biznow.ordernow.R;
import com.biznow.ordernow.adapter.MyOrderAdapter;
import com.biznow.ordernow.fragments.ConfirmOrderDialogFragment;
import com.biznow.ordernow.model.MyOrderItem;
import com.biznow.ordernow.model.Order;
import com.biznow.ordernow.model.OrderNowConstants;
import com.biznow.ordernow.model.OrderStatus;
import com.data.menu.CustomerOrderWrapper;
import com.google.gson.Gson;
import com.util.AsyncNetwork;
import com.util.AsyncURLHandler;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class MyOrderActivity extends Activity implements AsyncURLHandler {
    
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
		
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		final TextSwitcher totalAmount = (TextSwitcher) findViewById(R.id.totalAmount);
		totalAmount.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				TextView t = new TextView(MyOrderActivity.this);
				t.setTextColor(getResources().getColor(R.color.green));
				t.setTextSize(19);
				return t;
			}
		});

		totalAmount.setInAnimation(in);
		totalAmount.setOutAnimation(out);

		addMoreItemsBtn.setOnClickListener(new Button.OnClickListener() {           
			@Override
			public void onClick(View v) {
				//Cannot use onBackPressed() here due to dependency with Past Orders Page
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

	void showConfirmOrderDialog() {
		new ConfirmOrderDialogFragment().show(getFragmentManager(), "confirmOrderEditText");
	}

	public void doPositiveClick(String orderNote) {
		ApplicationState applicationContext = (ApplicationState)getApplicationContext();
		ArrayList<MyOrderItem> myOrderItemList = ApplicationState.getMyOrderItems(applicationContext);
		String restaurantId = OrderNowUtilities.getKeyFromSharedPreferences(applicationContext.getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
		Log.i("MyOrderActivity ", restaurantId);
		
		CustomerOrderWrapper customerOrderWrapper = new CustomerOrderWrapper(myOrderItemList, orderNote);
		ApplicationState.setCustomerOrderWrapper(applicationContext, customerOrderWrapper);

		Gson gs = new Gson();
		String order = gs.toJson(customerOrderWrapper.getCustomerOrder(applicationContext));
		String encoded = "";
        try {
            encoded = URLEncoder.encode(order,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
		String url = new URLBuilder().addPath(URLBuilder.Path.order)
				.addParam(URLBuilder.URLParam.order, encoded).build();

		new AsyncNetwork(this, null).execute(url);
		
		//start other activity
		Intent intent = new Intent(getApplicationContext(), MyParentOrderActivity.class);
		startActivity(intent);
		finish();
	}

	public void doNegativeClick() {}

    @Override
    public void handleException(Exception e) {
        OrderNowUtilities.generateNotification(getApplicationContext(), "Your Order was not Sent", MyOrderActivity.class);
        ApplicationState applicationContext = (ApplicationState) getApplicationContext();
        ApplicationState.setCustomerOrderWrapper(applicationContext, null);
    }

    @Override
    public void handleSuccess(String output) {
        
        try {
            Utilities.info("handleSuccess output " + output);
            Toast.makeText(getApplicationContext(), "Order has been successfully sent.", Toast.LENGTH_LONG).show();
            ApplicationState applicationContext = (ApplicationState) getApplicationContext();

            CustomerOrderWrapper customerOrderWrapper = ApplicationState.getCustomerOrderWrapper(applicationContext);
            Utilities.info("handleSuccess customerOrderWrapper " + customerOrderWrapper);

            JSONObject json = new JSONObject(output);
            String orderId = (String) json.get(URLBuilder.URLParam.orderId.toString());
            ApplicationState.setActiveOrderId(applicationContext, orderId);
            // subOrderId not used currently
            Integer subOrderId = (Integer) json.get(URLBuilder.URLParam.subOrderId.toString());
            customerOrderWrapper.setOrder(new Order(orderId, subOrderId));

            Utilities.info("Order response: " + output + " orderId " + orderId + " subOrderId " + subOrderId);
            ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);
            ApplicationState.setCustomerOrderWrapper(applicationContext, customerOrderWrapper);
            ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);

            //cut from MyParentOrderActivity
            ArrayList<CustomerOrderWrapper> subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(
                    getApplicationContext(), OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
            if (subOrderList == null) {
                subOrderList = new ArrayList<CustomerOrderWrapper>();
            }
            if (customerOrderWrapper != null) {
                customerOrderWrapper.modifyItemStatus(OrderStatus.Sent, null);
                subOrderList.add(customerOrderWrapper);
                ApplicationState.setCustomerOrderWrapper(applicationContext, null);

                // Only update Shared Prefs Object when there is a new suborder
                OrderNowUtilities.putObjectToSharedPreferences(getApplicationContext(),
                        OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST, subOrderList);
                OrderNowUtilities.orderStatusResetReceiver(getApplicationContext(), "Order Sent");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
