package com.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.biznow.ordernow.ApplicationState;
import com.biznow.ordernow.MyParentOrderActivity;
import com.biznow.ordernow.R;
import com.biznow.ordernow.model.FoodMenuItem;
import com.biznow.ordernow.model.OrderNowConstants;
import com.data.menu.CustomerOrderWrapper;
import com.data.menu.Dish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrderNowUtilities {
	private static final String ORDER_NOW = "OrderNow";

	public static ArrayList<FoodMenuItem> getFoodMenuItems(List<Dish> dishes) {
		ArrayList<FoodMenuItem> foodMenuItem = new ArrayList<FoodMenuItem>();
		if (dishes != null) {
			for (Dish dish : dishes) {
				foodMenuItem.add(new FoodMenuItem(dish));
			}
		}
		return foodMenuItem;
	}
	
	
	public static void showActivityOverlay(Context context, int layoutActiviy) {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		//final Dialog dialog = new Dialog(context);
		dialog.setContentView(layoutActiviy);

		LinearLayout layout = (LinearLayout) dialog
				.findViewById(R.id.llOverlay_activity);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				dialog.dismiss();

			}

		});

		dialog.show();

	}
	
	/*
	 * ---------------------------------- Shared Pref Util Stuff --------------------------------------------------------------------	
	 */
	private static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(ORDER_NOW, Context.MODE_PRIVATE);
	}

	private static Editor getSharedPreferencesEditor(Context context) {
		return getSharedPreferences(context).edit();
	}

	public static void putKeyToSharedPreferences(Context context, String key, String value) {
		Editor prefsEditor = getSharedPreferencesEditor(context);
		prefsEditor.putString(key, value);
		prefsEditor.commit();
		Utilities.info("SharedPrefs put key " + key + " value " + value);
	}

	public static String getKeyFromSharedPreferences(Context context, String key) {
		SharedPreferences preferences = getSharedPreferences(context);
		String value =  preferences.getString(key, "");
		Utilities.info("SharedPrefs get key " + key + " value " + value);
		return value;
	}

	public static void removeSharedPreferences(Context context, String key) {
		Editor prefsEditor = getSharedPreferencesEditor(context);
		prefsEditor.remove(key);
		prefsEditor.commit();
	}

	public static void removeSharedPreferences(Context context, ArrayList<String> keyList) {
		if (keyList == null || keyList.size() < 1)
			return;

		Editor prefsEditor = getSharedPreferencesEditor(context);		
		for (int i = 0; i < keyList.size(); i++) {
			prefsEditor.remove(keyList.get(i));	
		}		
		prefsEditor.commit();
	}

	public static void putObjectToSharedPreferences(Context context, String key, ArrayList<CustomerOrderWrapper> subOrderList) {
		Gson gson = new Gson();
		String json = gson.toJson(subOrderList);
		Editor prefsEditor = getSharedPreferencesEditor(context);
		prefsEditor.putString(key, json);
		prefsEditor.commit();
		Utilities.info("SharedPrefs Object put key " + key + " value " + json);
	}

	public static ArrayList<CustomerOrderWrapper> getObjectFromSharedPreferences(Context context, String key) {
		SharedPreferences preferences = getSharedPreferences(context);
		String json = preferences.getString(key, "");
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<CustomerOrderWrapper>>(){}.getType();
		ArrayList<CustomerOrderWrapper> subOrderList = gson.fromJson(json, type);
		Utilities.info("SharedPrefs Object get key " + key + " value "+ json);
		return subOrderList;
	}
	

    public static void generateNotification(Context context, String message, Class<?> activityClass) {

        Intent intent = new Intent(context, activityClass );
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification noti = new NotificationCompat.Builder(context)
        .setContentTitle(message)
        .setTicker("Notification!")
        .setWhen(System.currentTimeMillis())
        .setContentIntent(pIntent)
        .setDefaults(Notification.DEFAULT_SOUND)
        .setAutoCancel(true)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
        .setSmallIcon(R.drawable.ic_category)
        .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(OrderNowConstants.STATUS_CHANGE_NOTIFICATION_ID, noti);
 
    }
    
    public static void orderStatusResetReceiver(Context context, final String message) {
        Intent i = new Intent(OrderNowConstants.ORDER_STATUS_RESET);
        context.sendOrderedBroadcast(i, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                if (result != Activity.RESULT_CANCELED) {
                    Utilities.info("MyParentOrderActivity caught the broadcast, result " + result);
                    return; // Activity caught it
                }
                OrderNowUtilities.generateNotification(context, message, MyParentOrderActivity.class);
            }
        }, null, Activity.RESULT_CANCELED, null, null);
    }
    
    
    public static void sessionClean(final ApplicationState applicationContext) {
        ArrayList<String> sharedPrefsToRemove = new ArrayList<String>();
        sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
        sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_TABLE_ID);
        sharedPrefsToRemove.add(OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);
        OrderNowUtilities.removeSharedPreferences(applicationContext.getApplicationContext(), sharedPrefsToRemove);
        
        //Clear the Cached History List
        ApplicationState.setMyOrderHistoryList(applicationContext, null);
    }

}

