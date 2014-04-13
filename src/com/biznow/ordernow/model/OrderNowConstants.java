package com.biznow.ordernow.model;

import java.util.HashMap;

public class OrderNowConstants {
    public static final String ORDER_COMPLETED = "com.example.orderCompleted";

    public static final String MODIFY_ORDER = "com.example.modifyOrder";

    public static final String ORDER_ACCEPTED = "com.example.orderAccepted";

    public final static String INDIAN_RUPEE_UNICODE = "\u20B9";

    public static boolean IS_DEBUG_MODE = false;
    public static boolean IS_LOCAL_RESTURANT_ENABLED = false;

    public static final String ORDER_STATUS_RESET = "com.example.orderstatusreset";
    public static final String TEXT_COMMENT = "TextComment"; // FIXME: Make the
                                                             // Properties names
                                                             // more readable
    public static final String SPICE_LEVEL = "SpiceLevel";
    public static final String ACTION = "Action";

    public static final HashMap<String, OrderStatus> actionToOrderStatusMap = new HashMap<String, OrderStatus>();
    public static final String UNAVAILABLEITEMS = "unAvailableItems";

    public static final String KEY_ACTIVE_TABLE_ID = "ActiveTableID";
    public static final String KEY_ACTIVE_RESTAURANT_ID = "ActiveRestaurantID";
    public static final String KEY_ACTIVE_RESTAURANT_NAME = "ActiveRestaurantName";
    public static final String KEY_ACTIVE_SUB_ORDER_LIST = "ActiveSubOrderList";

    public static final String KEY_INGREDIENTS_SHOW_SWIPTE_TUT = "IngredientsSwipeHelp";

    public static final String FALSE = "false";

    public static final String TRUE = "true";

    public static final String ORDER_SENT = "com.example.orderSent";
    public static final String ORDER_RECIEVED = "com.example.orderReceived";

    public static final String ORDER_ID = "orderId";

    public static final String SUB_ORDER_ID = "suborderId";

    static {
        actionToOrderStatusMap.put(ORDER_SENT, OrderStatus.Sent);
        actionToOrderStatusMap.put(ORDER_RECIEVED, OrderStatus.Recieved);
        actionToOrderStatusMap.put(ORDER_ACCEPTED, OrderStatus.Accepted);
        actionToOrderStatusMap.put(MODIFY_ORDER, OrderStatus.ModifiedOrder);
        actionToOrderStatusMap.put(ORDER_COMPLETED, OrderStatus.Complete);

    }

    public static final String MESSAGE = "message";

    public static final String DISH_IDS = "dishIds";

    public final static int STATUS_CHANGE_NOTIFICATION_ID = 0;
}
