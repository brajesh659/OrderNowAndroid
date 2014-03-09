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

import com.data.menu.Restaurant;
import com.google.gson.Gson;
import com.util.URLBuilder;

public class DownloadResturantMenu {
    private LruCache<String, Restaurant> lruResturant = new LruCache<String, Restaurant>(2048);
    private static DownloadResturantMenu downloadResturantMenu = new DownloadResturantMenu();

    public static DownloadResturantMenu getInstance() {
        return downloadResturantMenu;
    }

    private DownloadResturantMenu() {
    }

	public Restaurant getResturant(String qrCode) {
		final String urlString = new URLBuilder()
				.addPath(URLBuilder.Path.serveTable)
				.addParam(URLBuilder.URLParam.tableId, qrCode)
				.build();
        Restaurant resturant = null;
        String urlEncodedParam = null;
        try {
            urlEncodedParam = URLEncoder.encode(urlString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (lruResturant.get(urlEncodedParam) == null) {
            try {
                URL url = new URL(urlString);
                Log.e("DownloadResturantMenu", urlString);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                InputStream in = connection.getInputStream();
                BufferedReader res = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String inputLine;
                StringBuffer sBuffer = new StringBuffer();
                while ((inputLine = res.readLine()) != null)
                    sBuffer.append(inputLine);
                res.close();

                Gson gs = new Gson();
                resturant = gs.fromJson(sBuffer.toString(), Restaurant.class);
                lruResturant.put(urlEncodedParam, resturant);
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("DownloadResturantMenu", ex.getMessage() + "got exception");
            }
        } else {
            Log.e("DownloadResturantMenu from cache", urlEncodedParam);
        }
        return lruResturant.get(urlEncodedParam);
    }
}