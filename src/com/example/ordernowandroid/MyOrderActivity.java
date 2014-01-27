package com.example.ordernowandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.menu.CustomerOrder;
import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.adapter.MyOrderAdapter;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.google.gson.Gson;
import com.parse.ParseInstallation;

public class MyOrderActivity extends Activity {
    public static final String RETURN_FROM_MY_ORDER = "ReturnFromMyOrder";
    private ArrayList<MyOrderItem> myOrderItemList;
    private CustomerOrderWrapper customerOrderWrapper;
    
    public static final String FOOD_MENU_CATEGORY_ID = "foodMenuCategoryId";
    private int categoryId;
    private String tableId;
    
    protected static final String SUB_ORDER_LIST = "SubOrderList";
    public static ArrayList<CustomerOrderWrapper> subOrdersFromDB;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Order");
        Bundle b = getIntent().getExtras();
        myOrderItemList = (ArrayList<MyOrderItem>) b.getSerializable(FoodMenuActivity.MY_ORDER);
        categoryId = b.getInt(FoodMenuActivity.FOOD_MENU_CATEGORY_ID);
        tableId = b.getString(FoodMenuActivity.TABLE_ID);
        subOrdersFromDB = (ArrayList<CustomerOrderWrapper>) b.getSerializable(SUB_ORDER_LIST);
        setContentView(R.layout.my_order_summary);
        
        if (subOrdersFromDB != null) {
        	Toast.makeText(getApplicationContext(), "Sub Orders MOA: " + subOrdersFromDB.size(), Toast.LENGTH_SHORT).show();	
        }  else {
        	Toast.makeText(getApplicationContext(), "Sub Orders MOA: Size 0", Toast.LENGTH_SHORT).show();
        }
        
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

                        //Clear the Selected Quantities and Start the Food Menu Activity again
                        Intent intent = new Intent(getApplicationContext(), FoodMenuActivity.class);
                        intent.putExtra(FoodMenuActivity.TABLE_ID, tableId);
                        intent.putExtra(SUB_ORDER_LIST, subOrdersFromDB);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);            
                builder.setTitle("Confirm Order");
                builder.setMessage("Are you sure you want to confirm the order ?");
                builder.setPositiveButton(R.string.ok, new OnClickListener() {                  
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	Map<String, Float> dishes = new HashMap<String, Float>();
						for (MyOrderItem myOrderItem : myOrderItemList) {
							dishes.put(myOrderItem.getFoodMenuItem().getDishId(), myOrderItem.getQuantity());
						}
						CharSequence text = ParseInstallation.getCurrentInstallation().getObjectId();						
						CustomerOrder customerOrder = new CustomerOrder(dishes, "R1", "Temp", text.toString(), "T1");
					
						String orderTotalPriceStr = (String) totalAmount.getText();
						if (orderTotalPriceStr.indexOf(OrderNowConstants.INDIAN_RUPEE_UNICODE) != -1){
							orderTotalPriceStr = orderTotalPriceStr.substring(orderTotalPriceStr.indexOf(OrderNowConstants.INDIAN_RUPEE_UNICODE) + 1).trim();
						}
						//Append Confirmed Order to CustomerOrderWrapper Object
						customerOrderWrapper = new CustomerOrderWrapper(customerOrder, myOrderItemList, Float.parseFloat(orderTotalPriceStr));
						
						Gson gs = new Gson();
						String url = "http://ordernow.herokuapp.com/order?order="
								+ gs.toJson(customerOrder) + "&debug=1";
						String response = "";
                        try {
                            response = new asyncNetwork().execute(url).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        //Un-commenting it till we have Push Notifications in place 
						Toast.makeText(getApplicationContext(), "Order has been confirmed.", Toast.LENGTH_LONG).show();
						
                        Intent intent = new Intent(getApplicationContext(), MyParentOrderActivity.class);
                        intent.putExtra(MyParentOrderActivity.CUSTOMER_ORDER_WRAPPER, customerOrderWrapper);
                        intent.putExtra(MyParentOrderActivity.FOOD_MENU_CATEGORY_ID, categoryId);
                        intent.putExtra(MyParentOrderActivity.TABLE_ID, tableId);                
                        intent.putExtra(SUB_ORDER_LIST, subOrdersFromDB);
                        startActivity(intent);
                        
                        finish();
					}
				});
				builder.setNegativeButton(R.string.cancel, null);                               
				AlertDialog alert = builder.create();
				alert.show();
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
			Intent intent = new Intent();
			intent.putExtra(RETURN_FROM_MY_ORDER, myOrderItemList);
			intent.putExtra(FOOD_MENU_CATEGORY_ID, categoryId);
			intent.putExtra(FoodMenuActivity.TABLE_ID, tableId);
			intent.putExtra(SUB_ORDER_LIST, subOrdersFromDB);
			setResult(RESULT_OK, intent); // Activity finished ok, return the data
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}


class asyncNetwork extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String response = "";

		try {
			URL url = new URL(params[0]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String line = null;
			while ((line = br.readLine()) != null) {
				response += line;
			}
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

}
