package com.biznow.ordernow.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.support.v4.util.LruCache;
import android.util.Log;

import com.data.database.RestaurantHelper;
import com.data.menu.Restaurant;
import com.data.menu.RestaurantWrapper;
import com.google.gson.Gson;
import com.util.URLBuilder;
import com.util.URLBuilder.URLAction;
import com.util.URLBuilder.URLParam;
import com.util.Utilities;

public class DownloadResturantMenu {
	private LruCache<String, Restaurant> lruResturant = new LruCache<String, Restaurant>(
			2048);
	private static DownloadResturantMenu downloadResturantMenu = new DownloadResturantMenu();

	public static DownloadResturantMenu getInstance() {
		return downloadResturantMenu;
	}

	private DownloadResturantMenu() {
	}

	public Restaurant getResturant(String tableId, String restaurantId,
			RestaurantHelper restHelper, boolean deliverySession) {

	    Utilities.info("getResturant " + tableId+restaurantId+deliverySession);
		Restaurant restaurant = null;
		RestaurantWrapper restWrapper = null;
		Restaurant restaurantFromDB = null;
		if (lruResturant.get(restaurantId) == null) {
			String lastUpdatedAt = "-1";

			// get details from db if present and pass that
			if (restHelper != null) {
				restaurantFromDB = restHelper.getRestaurant(restaurantId);
				if (restaurantFromDB != null) {
					lastUpdatedAt = Long.toString(restaurantFromDB
							.getLastUpdatedAt());
				}
			}

			String urlString = "";
			if(deliverySession) {
			    urlString = new URLBuilder()
                .addPath(URLBuilder.Path.delivery)
                .addAction(URLAction.restData)
                .addParam(URLBuilder.URLParam.restaurantId, restaurantId)
                .addParam(URLParam.lastUpdatedAt, lastUpdatedAt).build();
			} else {
			    urlString = new URLBuilder()
                .addPath(URLBuilder.Path.serveTable)
                .addParam(URLBuilder.URLParam.tableId, tableId)
                .addParam(URLParam.lastUpdatedAt, lastUpdatedAt).build();
			}
			
			try {
				URL url = new URL(urlString);
				Utilities.info("DownloadResturantMenu " + urlString);
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				InputStream in = connection.getInputStream();
				BufferedReader res = new BufferedReader(new InputStreamReader(
						in, "UTF-8"));
				String inputLine;
				StringBuffer sBuffer = new StringBuffer();
				while ((inputLine = res.readLine()) != null)
					sBuffer.append(inputLine);
				res.close();

				Gson gs = new Gson();
				restWrapper = gs.fromJson(sBuffer.toString(), RestaurantWrapper.class);
				//pull from DB/update DB if needed
				
                if (restWrapper.isUpdated()) {
                    Utilities.info("DBRestaurant restWrapper updated " + restWrapper.getRestaurant());
                    restaurant = restWrapper.getRestaurant();
                    if (!lastUpdatedAt.isEmpty()) {
                        Utilities.info("DBRestaurant add and delete " + restaurant.getrId() + " "
                                + restaurant.getName() + "lastupdatedtime " + restaurant.getLastUpdatedAt());
                        restHelper.addAndDeleteRestaurant(restaurant);
                    } else {
                        Utilities.info("DBRestaurant add " + restaurant.getrId() + " " + restaurant.getName());
                        restHelper.addRestaurant(restaurant);
                    }
                } else if (!restWrapper.isUpdated()) {
                    Utilities.info("DBRestaurant restWrapper notupdated");
                    restaurant = restaurantFromDB;
                }
				
				lruResturant.put(restaurantId, restaurant);
			} catch (Exception ex) {
				ex.printStackTrace();
				Utilities.error("DownloadResturantMenu" + ex.getMessage()
						+ "got exception");
			}
		} else {
		    Utilities.info("DownloadResturantMenu from cache "+ tableId);
		}
		return lruResturant.get(restaurantId);
	}
}