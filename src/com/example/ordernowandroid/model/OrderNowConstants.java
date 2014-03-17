package com.example.ordernowandroid.model;

import java.util.HashMap;

public class OrderNowConstants {
    public final static String INDIAN_RUPEE_UNICODE = "\u20B9";
    public static final String KEY_ACTIVE_TABLE_ID = "ActiveTableID";
    public static final String KEY_ACTIVE_RESTAURANT_ID = "ActiveRestaurantID";
	public static final String KEY_ACTIVE_RESTAURANT_NAME = "ActiveRestaurantName";
    public static boolean IS_DEBUG_MODE = false ;
    public static boolean IS_LOCAL_RESTURANT_ENABLED = false;
	public static final String TEXT_COMMENT = "TextComment"; //FIXME: Make the Properties names more readable
	public static final String SPICE_LEVEL = "SpiceLevel";
    public static final String ACTION = "Action";
    
    public static final HashMap<String,OrderStatus> actionToOrderStatusMap = new HashMap<String, OrderStatus>();
    public static final String UNAVAILABLEITEMS = "unAvailableItems";
    static {
        actionToOrderStatusMap.put("com.example.orderReceived", OrderStatus.Recieved);
        actionToOrderStatusMap.put("com.example.orderAccepted", OrderStatus.Accepted);
        actionToOrderStatusMap.put("com.example.modifyOrder", OrderStatus.ModifiedOrder);
        actionToOrderStatusMap.put("com.example.orderCompleted", OrderStatus.Complete);
        
    }
}
