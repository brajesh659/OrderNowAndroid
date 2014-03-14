package com.example.ordernowandroid.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.support.v4.util.LruCache;
import android.util.Log;

import com.data.database.CustomDbAdapter;
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

		Restaurant resturant = null;
		Restaurant resturantFromDB = null;
		if (lruResturant.get(tableId) == null) {
			String lastUpdatedAt = "";

			// get details from db if present and pass that
			if (restHelper != null) {
				resturantFromDB = restHelper.getRestaurant(restaurantId);
				if (resturantFromDB != null) {
					Utilities.info("DBRestaurant useDB before " + resturantFromDB.getrId() + " " + resturantFromDB.getName());
					lastUpdatedAt = Long.toString(resturantFromDB
							.getLastUpdatedAt());
					//TODO remove this once lastUpdated is implemented on server. 
					return resturantFromDB;
					
				}
			}

			final String urlString = new URLBuilder()
					.addPath(URLBuilder.Path.serveTable)
					.addParam(URLBuilder.URLParam.tableId, tableId)
					.addParam(URLParam.lastUpdatedAt, lastUpdatedAt).build();
			
			try {
				URL url = new URL(urlString);
				Log.e("DownloadResturantMenu", urlString);
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
				resturant = gs.fromJson(sBuffer.toString(), Restaurant.class);
				//pull from DB/update DB if needed
				if(resturant == null && !lastUpdatedAt.isEmpty()) {
					Utilities.info("DBRestaurant useDB " + resturantFromDB.getrId() + " " + resturantFromDB.getName());
					resturant = resturantFromDB;
				} else if(resturant != null && !lastUpdatedAt.isEmpty()) {
					Utilities.info("DBRestaurant add and delete " + resturant.getrId() + " " + resturant.getName());
					restHelper.addAndDeleteRestaurant(resturant);
				} else if(resturant != null && lastUpdatedAt.isEmpty()) {
					Utilities.info("DBRestaurant add " + resturant.getrId() + " " + resturant.getName());
					restHelper.addRestaurant(resturant);
				}
				
				lruResturant.put(tableId, resturant);
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("DownloadResturantMenu", ex.getMessage()
						+ "got exception");
			}
		} else {
			Log.e("DownloadResturantMenu from cache", tableId);
		}
		return lruResturant.get(tableId);
	}
}