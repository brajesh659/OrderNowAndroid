package com.example.ordernowandroid;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.data.restaurant.OrderedDish;
import com.data.restaurant.RestaurantOrder;
import com.example.ordernowandroid.adapter.AllHistoryAdapter;
import com.example.ordernowandroid.model.AllHistoryViewItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseInstallation;
import com.util.AsyncNetwork;
import com.util.AsyncURLHandler;
import com.util.URLBuilder;
import com.util.Utilities;

public class AllCustomerHistoryActivity extends Activity implements AsyncURLHandler {

    private ArrayList<AllHistoryViewItem> allHistoryItems = new ArrayList<AllHistoryViewItem>();
    private AllHistoryAdapter adapter;
    private ExpandableListView allHistoryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Past Orders");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.expandable_list_view);

        allHistoryListView = (ExpandableListView) findViewById(R.id.expandableListView);
        
        String url = new URLBuilder()
        .addPath(URLBuilder.Path.serveTable)
        .addAction(URLBuilder.URLAction.custHistory)
        .addParam(URLBuilder.URLParam.customerId,
                ParseInstallation.getCurrentInstallation().getObjectId().toString())
        .build();
        Utilities.info("URL: " + url);
        new AsyncNetwork(this, AllCustomerHistoryActivity.this).execute(url);

    }


    private ArrayList<AllHistoryViewItem> getHistoryItemLocaly() {
        ArrayList<AllHistoryViewItem> allHistoryItems = new ArrayList<AllHistoryViewItem>();

        ArrayList<String> dishNames = new ArrayList<String>();
        dishNames.add("Chicken Starter");
        dishNames.add("Fish Tikka");
        dishNames.add("Dal Makhni");
        dishNames.add("Coke");

        ArrayList<String> dishNames2 = new ArrayList<String>();
        dishNames2.add("Paneer Tikka");
        dishNames2.add("Rice");
        dishNames2.add("Dal Fry");
        dishNames2.add("Pepsi");

        String date1 = "20-03-2014";
        String date2 = "24-03-2014";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        AllHistoryViewItem allHistoryItem = null;
        AllHistoryViewItem allHistoryItem2 = null;
        try {
            allHistoryItem = new AllHistoryViewItem("Eat3", "D1042556", df.parse(date1), dishNames);
            allHistoryItem2 = new AllHistoryViewItem("Paramuru Grill", "O1042556021", df.parse(date2), dishNames2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        allHistoryItems.add(allHistoryItem);
        allHistoryItems.add(allHistoryItem2);
        return allHistoryItems;
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
    public void handleException(Exception e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleSuccess(String result) {
        ArrayList<RestaurantOrder> allOrderHistoryList = new ArrayList<RestaurantOrder>();
        try {
            String allOrderHistoryJson = result;
            Utilities.info("allOrderHistoryJson: " + allOrderHistoryJson);

            Gson gs = new Gson();
            Type type = new TypeToken<ArrayList<RestaurantOrder>>(){}.getType();
            allOrderHistoryList = gs.fromJson(allOrderHistoryJson, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (RestaurantOrder restOrder : allOrderHistoryList) {
            List<String> dishnames = new ArrayList<String>();
            List<OrderedDish> orderedDishes = restOrder.getDishes();
            if (orderedDishes != null && !orderedDishes.isEmpty()) {
                for (OrderedDish orderedDish : orderedDishes) {
                    dishnames.add(orderedDish.getName());
                }
            }
            AllHistoryViewItem allHistoryItem = new AllHistoryViewItem(restOrder.getRestaurantName(),
                    restOrder.getOrderId(),new Date(restOrder.getCreatedAt()), dishnames);
            allHistoryItems.add(allHistoryItem);
        }
        adapter = new AllHistoryAdapter(getApplicationContext(), allHistoryItems);
        allHistoryListView.setAdapter(adapter);
    }

}
