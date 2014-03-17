package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.ordernowandroid.model.OrderNowConstants;
import com.util.Utilities;

public class MyCustomReceiver extends BroadcastReceiver {
    private ArrayList<String> unAvailableDishes = null;
   // private static final String TAG = "MyCustomReceiver";

	/*
	 *  There are 5 events a restaurant can send to the Server which has to be communicated to the Client:
	 *  	ORDER_RECEIVED("orderReceived"),
	 * 		ORDER_ACCEPTED("orderAccepted"),
	 *		BILL_GENERATED("generateBill"),
	 * 		ORDER_COMPLETED("orderCompleted"),
	 * 		MODIFY_ORDER("modifyOrder");
	 * 
	 * The Restaurant will return the following information:
	 * 1) Client ID
	 * 2) Table ID
	 * 3) RestaurantId
	 * 4) Order ID
	 * 
	 * Order Id is the Channel to publish the notification to the client.
	 * 
	 * Eg URL:
	 * ORDER_RECEIVED
	 *		restOrder?action=orderReceived&orderId=jjoJuiPitd
	 * ORDER_ACCEPTED
	 * 		restOrder?action=orderAccepted&orderId=Oid
	 * BILL_GENERATED
	 * 		restOrder?action=generateBill&orderId=Oid
	 * ORDER_COMPLETED
	 * 		restOrder?action=orderCompleted&orderId=Oid
	 * MODIFY_ORDER
	 * 		restOrder?action=modifyOrder&orderId=Oid
	 */
	
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int duration = Toast.LENGTH_LONG;
           // Toast toast = Toast.makeText(context, "Here in custom handler", duration);

            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Utilities.info(json.toString());
			Utilities.info("action recieved from server " +action);
			Utilities.info(channel);
            Iterator itr = json.keys();
           // Toast.makeText(context, "MyCustomerReciver", duration).show();
			String message = null;
			String subject = "";
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equals("message")) {
                    message = json.getString(key);
                    //toast = Toast.makeText(context, message, duration);
                    //toast.show();
				} else if (key.equals("dishIds")) {
                    subject = json.getString(key);
                    Utilities.info(subject);
                     List<String> asList = Arrays.asList(subject.split("\\s*,\\s*"));
                     unAvailableDishes = new ArrayList<String>();
                     for (String string : asList) {
                        unAvailableDishes.add(string);
                     }
                    //toast = Toast.makeText(context, subject, duration);
                    //toast.show();
				}
			}
			notification(context, message, subject, action);

        } catch (JSONException e) {
        }
    }
    
    
    private void notification(Context context, String message, String subject, String action) {

        Intent intent = new Intent(context, MyParentOrderActivity.class);
        Utilities.info("Utilities + action not null" + action);
        intent.putExtra(OrderNowConstants.ACTION, action);
        if (unAvailableDishes != null) {
            Bundle b = new Bundle();
            b.putSerializable(OrderNowConstants.UNAVAILABLEITEMS, unAvailableDishes);
            intent.putExtras(b);
        }
        
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Notification noti = new NotificationCompat.Builder(context)
        .setContentTitle(message)
        .setContentText(subject)
        .setTicker("Notification!")
        .setWhen(System.currentTimeMillis())
        .setContentIntent(pIntent)
        .setDefaults(Notification.DEFAULT_SOUND)
        .setAutoCancel(true)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
        .setSmallIcon(R.drawable.bb2)
        .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
 
    }
    
}
