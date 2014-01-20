package com.example.ordernowandroid;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyCustomReceiver extends BroadcastReceiver {
private static final String TAG = "MyCustomReceiver";

@Override
public void onReceive(Context context, Intent intent) {
        try {
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context,"Here in custom handler" , duration);
                
              String action = intent.getAction();
              String channel = intent.getExtras().getString("com.parse.Channel");
              JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
         
              Iterator itr = json.keys();
              
              
              while (itr.hasNext()) {
                String key = (String) itr.next();
                toast = Toast.makeText(context,"..." + key + " => " + json.getString(key) , duration);
                        toast.show();
              }
            } catch (JSONException e) {
            }
}
}
