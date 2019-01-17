package com.data.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biznow.ordernow.ApplicationState;
import com.biznow.ordernow.model.MyOrderItem;
import com.biznow.ordernow.model.Order;
import com.biznow.ordernow.model.OrderNowConstants;
import com.biznow.ordernow.model.OrderStatus;
import com.parse.ParseInstallation;
import com.util.OrderNowUtilities;

/**
 * 
 * @author Rohit Class to wrap Server CustomerOrder object along with fields
 *         required for App UI View
 */

public class CustomerOrderWrapper implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private ArrayList<MyOrderItem> myOrderItemList;
    private String orderNote;
    private Order orderId;
    private boolean resturantacknowledgement = false;
    private String customerNameForOrder = "My order";

    public CustomerOrderWrapper(ArrayList<MyOrderItem> myOrderItemList, String orderNote) {
        super();
        this.myOrderItemList = myOrderItemList;
        this.orderNote = orderNote;
    }

    public CustomerOrder getCustomerOrder(ApplicationState applicationContext) {
        OrderDish orderDish;
        Map<String, OrderDish> dishes = new HashMap<String, OrderDish>();
        for (MyOrderItem myOrderItem : myOrderItemList) {
            if (myOrderItem.getMetaData() != null) {
                orderDish = new OrderDish(myOrderItem.getQuantity(), myOrderItem.getMetaData().get(OrderNowConstants.TEXT_COMMENT), myOrderItem.getMetaData().get(OrderNowConstants.SPICE_LEVEL));
            } else {
                orderDish = new OrderDish(myOrderItem.getQuantity());
            }
            // Clean Dish Ingredient if present
            if (myOrderItem.getFoodMenuItem().isItemCustomizable()) {
                orderDish.setSelectedOptions(myOrderItem.getFoodMenuItem().getCurrentSelectedIngredients());
                ApplicationState.cleanDishSelectedIngredients(applicationContext, myOrderItem.getFoodMenuItem().getItemName());
            }
            dishes.put(myOrderItem.getFoodMenuItem().getDishId(), orderDish);
        }

        String restaurantId = OrderNowUtilities.getKeyFromSharedPreferences(applicationContext.getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_ID);
        String restaurantName = OrderNowUtilities.getKeyFromSharedPreferences(
                applicationContext.getApplicationContext(), OrderNowConstants.KEY_ACTIVE_RESTAURANT_NAME);
        CharSequence customerId = ParseInstallation.getCurrentInstallation().getObjectId();
        String tableId = OrderNowUtilities.getKeyFromSharedPreferences(applicationContext.getApplicationContext(), OrderNowConstants.KEY_ACTIVE_TABLE_ID);
        CustomerOrder customerOrder = new CustomerOrder(dishes, restaurantId, restaurantName, customerId.toString(), tableId, orderNote);
        return customerOrder;
    }

    public ArrayList<MyOrderItem> getMyOrderItemList() {
        return myOrderItemList;
    }

    public void modifyItemStatus(OrderStatus orderStatus, List<String> unAvailableItems) {
        for (MyOrderItem myOrderItem : myOrderItemList) {
            OrderStatus newStatus = OrderStatus.NULL;
            OrderStatus itemCurrentStatus = myOrderItem.getItemStatus();
            if (orderStatus == OrderStatus.ModifiedOrder && unAvailableItems != null) {
                if (!unAvailableItems.contains(myOrderItem.getFoodMenuItem().getDishId())) {
                    newStatus = itemCurrentStatus.newStatus(OrderStatus.Accepted);
                } else {
                    newStatus = itemCurrentStatus.newStatus(orderStatus);
                }
            } else {
                newStatus = itemCurrentStatus.newStatus(orderStatus);
            }
            myOrderItem.setItemStatus(newStatus);
        }
        
        if(orderStatus == OrderStatus.Accepted || orderStatus == OrderStatus.ModifiedOrder || orderStatus == OrderStatus.Complete) {
            setResturantacknowledgement(true);
        }
    }

    public Order getOrder() {
        return orderId;
    }

    public void setOrder(Order orderId) {
        this.orderId = orderId;
    }

    public boolean hasResturantacknowledged() {
        return resturantacknowledgement;
    }

    public void setResturantacknowledgement(boolean resturantacknowledgement) {
        this.resturantacknowledgement = resturantacknowledgement;
    }

    public String getCustomerNameForOrder() {
        return customerNameForOrder ;
    }

    public void setCustomerNameForOrder(String customerNameForOrder) {
        this.customerNameForOrder = customerNameForOrder;
    }

    public String getOrderNote() {
        return orderNote;
    }
}
