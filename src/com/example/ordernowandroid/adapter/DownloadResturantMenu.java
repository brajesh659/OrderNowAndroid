package com.example.ordernowandroid.adapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

import com.data.menu.Restaurant;
import com.google.gson.Gson;

public class DownloadResturantMenu {

    public static Restaurant getResturant(String endpoint, String qrCode) {
        Restaurant resturant = null;
        try {
            URL url = new URL(endpoint + "?tableId=" + qrCode);
            Log.e("DownloadResturantMenu", endpoint + qrCode);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            BufferedReader res = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer sBuffer = new StringBuffer();
            String inputLine;
            while ((inputLine = res.readLine()) != null)
                sBuffer.append(inputLine);

            res.close();
            Gson gs = new Gson();
            resturant = gs.fromJson(sBuffer.toString(), Restaurant.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("DownloadResturantMenu", ex.getMessage() + "got exception");
        }
        return resturant;
    }
}