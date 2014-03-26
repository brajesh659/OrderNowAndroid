package com.example.ordernowandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.data.menu.CustomerOrderWrapper;
import com.example.ordernowandroid.model.Order;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.example.ordernowandroid.model.OrderStatus;
import com.util.OrderNowUtilities;
import com.util.Utilities;

public class MyCustomReceiver extends BroadcastReceiver {
    private ArrayList<String> unAvailableDishes = null;
    private Integer subOrderId;
    private String orderId;
    private String message;

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
           // Toast toast = Toast.makeText(context, "Here in custom handler", duration);

            String action = intent.getAction();
            OrderStatus orderStatus = OrderStatus.NULL;
            if (action != null && !action.isEmpty()) {
                orderStatus = OrderNowConstants.actionToOrderStatusMap.get(action);
            }
            
//            String channel = intent.getExtras().getString("com.parse.Channel");
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

//            Utilities.info("Channel + "+ channel);
            Utilities.info(json.toString());
			Utilities.info("action recieved from server " +action);
			
            Iterator itr = json.keys();

            String subject = "";
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equals(OrderNowConstants.MESSAGE)) {
                    message = json.getString(key);
				} else if (key.equals(OrderNowConstants.DISH_IDS)) {
                    subject = json.getString(key);
                     List<String> asList = Arrays.asList(subject.split("\\s*,\\s*"));
                     unAvailableDishes = new ArrayList<String>();
                     for (String string : asList) {
                        unAvailableDishes.add(string);
                     }
				} else if (key.equals(OrderNowConstants.ORDER_ID)) {
				    orderId = json.getString(key);
				} else if (key.equals(OrderNowConstants.SUB_ORDER_ID)) {
				    subOrderId = json.getInt(key);
				}
			}
            
            Order order = new Order(orderId, subOrderId);
            
            if (isStatusModificationRequired(action)) {
                ArrayList<CustomerOrderWrapper> subOrderList = OrderNowUtilities.getObjectFromSharedPreferences(context, OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST);

                if(subOrderList == null){
                    return ;
                }

                for (CustomerOrderWrapper iCustomerOrderWrapper : subOrderList) {
                    if (order.getSubOrderId() == 0 && orderStatus.equals(OrderStatus.Complete)) { // Special case for order completion
                        iCustomerOrderWrapper.modifyItemStatus(orderStatus, unAvailableDishes);
                    } else {
                        if (iCustomerOrderWrapper.getOrder().equals(order)) {
                            Utilities.info("Modify order" + order.toString() + " to OrderStatus " + orderStatus);
                            iCustomerOrderWrapper.modifyItemStatus(orderStatus, unAvailableDishes);
                        }
                    }
                }

                OrderNowUtilities.putObjectToSharedPreferences(context, OrderNowConstants.KEY_ACTIVE_SUB_ORDER_LIST, subOrderList);

                Intent i = new Intent(OrderNowConstants.ORDER_STATUS_RESET);
                context.sendOrderedBroadcast(i, null, new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int result = getResultCode();
                        if (result != Activity.RESULT_CANCELED) {
                            Utilities.info("MyParentOrderActivity caught the broadcast, result " + result);
                            return; // Activity caught it
                        }
                        notification(context, message);
                    }
                }, null, Activity.RESULT_CANCELED, null, null);

            } else {
                Utilities.info("Action not implemented yet "+action);
            }

        } catch (JSONException e) {
        }
    }

    private boolean isStatusModificationRequired(String action) {
        return action.equals(OrderNowConstants.ORDER_RECIEVED) ||
               action.equals(OrderNowConstants.ORDER_ACCEPTED) ||
               action.equals(OrderNowConstants.ORDER_COMPLETED) ||
               action.equals(OrderNowConstants.MODIFY_ORDER);
    }

    private void notification(Context context, String message) {

        Intent intent = new Intent(context, MyParentOrderActivity.class);
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
    
}
