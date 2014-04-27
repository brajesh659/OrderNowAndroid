package com.biznow.ordernow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.biznow.ordernow.adapter.DeliveryRestaurantAdapter;
import com.biznow.ordernow.adapter.ImageService;
import com.biznow.ordernow.model.OrderNowConstants;
import com.data.menu.Restaurant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.AsyncNetwork;
import com.util.AsyncURLHandler;
import com.util.DownloadImageTask;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class DeliveryRestaurantActivity extends ListActivity implements AsyncURLHandler {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Restaurants Delivery");
        getActionBar().setDisplayHomeAsUpEnabled(true);
//        setContentView(R.layout.activity_my_order_history);
        
        //restaurantsListView = (ListView) findViewById(R.id.orderHistoryList);
        
        String url = new URLBuilder()
        .addPath(URLBuilder.Path.delivery)
        .addAction(URLBuilder.URLAction.restList)
        .build();
        Utilities.info("URL Delivery: " + url);
        new AsyncNetwork(this, DeliveryRestaurantActivity.this).execute(url);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Restaurant restSelected  = (Restaurant)getListAdapter().getItem(position);
        Toast.makeText(getApplicationContext(),"Restaurant Selected " + restSelected.getName(), Toast.LENGTH_LONG).show();
        //clean order stuff if present
        ApplicationState applicationContext = (ApplicationState)getApplicationContext();
        ApplicationState.cleanFoodMenuItemQuantityMap(applicationContext);
        //clean any previous session if present and start fresh
        OrderNowUtilities.sessionClean(applicationContext);

        //no active table id
        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID, "");
        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID, restSelected.getrId());
        OrderNowUtilities.putKeyToSharedPreferences(getApplicationContext(), OrderNowConstants.KEY_ACTIVE_DELIVERY_SESSION, "true");
        
        ApplicationState.setOpenCategoryDrawer(applicationContext, true);

        Intent intent = new Intent(this, FoodMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleException(Exception e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleSuccess(String result) {
        String restListJson  = result;
        Utilities.info("restListJson: " + restListJson);
        
        Gson gs = new Gson();
        Type type = new TypeToken<ArrayList<Restaurant>>(){}.getType();
        final List<Restaurant> restList = gs.fromJson(restListJson, type);
        for(Restaurant rest : restList) {
            new DownloadImageTask().execute(rest.getImg());
        }
        
        DeliveryRestaurantAdapter adapter = new DeliveryRestaurantAdapter(getApplicationContext(), restList);
        setListAdapter(adapter);
        
    }

}
