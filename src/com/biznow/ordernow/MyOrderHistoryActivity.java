package com.biznow.ordernow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.biznow.ordernow.R;
import com.biznow.ordernow.adapter.MyOrderHistoryAdapter;
import com.data.restaurant.RestaurantOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseInstallation;
import com.util.AsyncNetwork;
import com.util.URLBuilder;
import com.util.Utilities;

public class MyOrderHistoryActivity extends Activity {

	private ArrayList<RestaurantOrder> myOrderHistoryList = new ArrayList<RestaurantOrder>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Restaurant Past Orders");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_my_order_history);

		ApplicationState applicationContext = (ApplicationState) getApplicationContext();
		if(applicationContext.getMyOrderHistoryList() == null) {
				String url = new URLBuilder()
				.addPath(URLBuilder.Path.serveTable)
				.addAction(URLBuilder.URLAction.custHistory)
				.addParam(URLBuilder.URLParam.customerId, ParseInstallation.getCurrentInstallation().getObjectId().toString())//TODO: Add param for Rest Id
				.build();
				
				Utilities.info("URL: " + url);
				try {
					String myOrderHistoryJson = new AsyncNetwork().execute(url).get();
					Utilities.info("myOrderHistoryJson: " + myOrderHistoryJson);
					
					Gson gs = new Gson();
					Type type = new TypeToken<ArrayList<RestaurantOrder>>(){}.getType();
					myOrderHistoryList = gs.fromJson(myOrderHistoryJson, type);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			ApplicationState.setMyOrderHistoryList(applicationContext, myOrderHistoryList);
		} else {
			myOrderHistoryList = ApplicationState.getMyOrderHistoryList(applicationContext);
		}

		ListView myOrderHistoryListView = (ListView) findViewById(R.id.orderHistoryList);
		MyOrderHistoryAdapter myOrderHistoryAdapter = new MyOrderHistoryAdapter(this, myOrderHistoryList);
		myOrderHistoryListView.setAdapter(myOrderHistoryAdapter);
	}

}
