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
import com.google.gson.Gson;
import com.util.URLBuilder;
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
			RestaurantHelper restHelper) {

		Restaurant restaurant = null;
		Restaurant restaurantFromDB = null;
		if (lruResturant.get(tableId) == null) {
			String lastUpdatedAt = "";

			// get details from db if present and pass that
			if (restHelper != null) {
				restaurantFromDB = restHelper.getRestaurant(restaurantId);
				if (restaurantFromDB != null) {
					lastUpdatedAt = Long.toString(restaurantFromDB
							.getLastUpdatedAt());
					//TODO remove this once lastUpdated is implemented on server. 
					return restaurantFromDB;
					
				}
			}

			final String urlString = new URLBuilder()
					.addPath(URLBuilder.Path.serveTable)
					.addParam(URLBuilder.URLParam.tableId, tableId)
					.addParam(URLParam.lastUpdatedAt, lastUpdatedAt).build();
			
			try {
				URL url = new URL(urlString);
				Log.i("DownloadResturantMenu", urlString);
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
				restaurant = gs.fromJson(sBuffer.toString(), Restaurant.class);
				//pull from DB/update DB if needed
				if(restaurant == null && !lastUpdatedAt.isEmpty()) {
					restaurant = restaurantFromDB;
				} else if(restaurant != null && !lastUpdatedAt.isEmpty()) {
					Utilities.info("DBRestaurant add and delete " + restaurant.getrId() + " " + restaurant.getName());
					restHelper.addAndDeleteRestaurant(restaurant);
				} else if(restaurant != null && lastUpdatedAt.isEmpty()) {
					Utilities.info("DBRestaurant add " + restaurant.getrId() + " " + restaurant.getName());
					restHelper.addRestaurant(restaurant);
				}
				
				lruResturant.put(tableId, restaurant);
			} catch (Exception ex) {
				ex.printStackTrace();
				Utilities.error("DownloadResturantMenu" + ex.getMessage()
						+ "got exception");
			}
		} else {
			Log.i("DownloadResturantMenu from cache", tableId);
		}
		return lruResturant.get(tableId);
	}
}