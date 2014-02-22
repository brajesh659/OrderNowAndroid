package com.example.ordernowandroid;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MyCustomReceiver extends BroadcastReceiver {
   // private static final String TAG = "MyCustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            int duration = Toast.LENGTH_LONG;
           // Toast toast = Toast.makeText(context, "Here in custom handler", duration);

            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Iterator itr = json.keys();
           // Toast.makeText(context, "MyCustomerReciver", duration).show();

            while (itr.hasNext()) {
                String key = (String) itr.next();
                String message = null;
                String subject = "";
                if (key.equals("message")) {
                    message = json.getString(key);
                    //toast = Toast.makeText(context, message, duration);
                    //toast.show();
                }
                if (key.equals("dishIds")) {
                    subject = json.getString(key);
                    //toast = Toast.makeText(context, subject, duration);
                    //toast.show();
                }

                notification(context, message, subject);
            }
        } catch (JSONException e) {
        }
    }
    
    
    public void notification(Context context, String message, String subject) {
        Intent intent = new Intent(context, MyParentOrderActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
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
