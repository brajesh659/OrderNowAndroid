package com.biznow.ordernow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.biznow.ordernow.adapter.DeliveryRestaurantAdapter;
import com.biznow.ordernow.model.OrderNowConstants;
import com.data.database.CustomDbAdapter;
import com.data.database.RestaurantHelper;
import com.data.menu.Restaurant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.AsyncNetwork;
import com.util.AsyncURLHandler;
import com.util.DownloadImageTask;
import com.util.OrderNowUtilities;
import com.util.URLBuilder;
import com.util.Utilities;

public class DeliveryRestaurantActivity extends ListActivity implements AsyncURLHandler, SearchView.OnQueryTextListener {
    
    private SearchView searchView;
    private RestaurantHelper dh;
    
    private List<Restaurant> restList;
    private List<Restaurant> allRestList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Restaurants Delivery");
//        setContentView(R.layout.activity_my_order_history);
        
        //restaurantsListView = (ListView) findViewById(R.id.orderHistoryList);
        
        // enabling action bar app icon and behaving it as toggle button
     // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ActionBar actionBar = getActionBar();               
        actionBar.setCustomView(R.layout.search_restaurants_layout); //load your layout
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_TITLE); //show it 
        getActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = (SearchView) actionBar.getCustomView().findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        
        CustomDbAdapter dbManager = CustomDbAdapter.getInstance(getBaseContext());
        dh = new RestaurantHelper(dbManager);
        
        restList = new ArrayList<Restaurant>();
        allRestList = new ArrayList<Restaurant>();
        
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
        allRestList = gs.fromJson(restListJson, type);
        restList = allRestList;       
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                CustomDbAdapter dbManager = CustomDbAdapter.getInstance(getBaseContext());
                RestaurantHelper dh = new RestaurantHelper(dbManager);
                dh.addRestaurants(allRestList);
            }
        }).start();
        for(Restaurant rest : restList) {
            new DownloadImageTask().execute(rest.getImg());
        }
        DeliveryRestaurantAdapter adapter = new DeliveryRestaurantAdapter(getApplicationContext(), restList);
        setListAdapter(adapter);
        
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText == null || newText.isEmpty()) {
            restList = allRestList;
            DeliveryRestaurantAdapter adapter = new DeliveryRestaurantAdapter(getApplicationContext(), restList);
            setListAdapter(adapter);
            
            return false;
        }
        String query = newText;
        if(dh!=null) {
            List<Restaurant> searchDishList = dh.getRestaurantList(query);
            restList = searchDishList;
            DeliveryRestaurantAdapter adapter = new DeliveryRestaurantAdapter(getApplicationContext(), restList);
            setListAdapter(adapter);
            return true;
        }
        return false;
        
    }

    @Override
    public boolean onQueryTextSubmit(String finalText) {
        onQueryTextChange(finalText);
        searchView.clearFocus();
        return true;
    }

}
