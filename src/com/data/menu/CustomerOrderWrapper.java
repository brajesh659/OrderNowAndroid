package com.data.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ordernowandroid.ApplicationState;
import com.example.ordernowandroid.model.MyOrderItem;
import com.example.ordernowandroid.model.OrderNowConstants;
import com.example.ordernowandroid.model.OrderStatus;
import com.parse.ParseInstallation;

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
    private ApplicationState applicationState;
    private String orderNote;
    private OrderStatus orderStatus = OrderStatus.Sent;
    private List<String> unAvailableItems ;
    

    public CustomerOrderWrapper(ArrayList<MyOrderItem> myOrderItemList, ApplicationState applicationState, String orderNote) {
        super();
        this.myOrderItemList = myOrderItemList;
        this.applicationState = applicationState;
        this.orderNote = orderNote;
    }

    public CustomerOrder getCustomerOrder() {
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
                ApplicationState.cleanDishSelectedIngredients(applicationState, myOrderItem.getFoodMenuItem().getItemName());
            }
            dishes.put(myOrderItem.getFoodMenuItem().getDishId(), orderDish);
        }

        String restaurantId = ApplicationState.getRestaurantId(applicationState);
        CharSequence text = ParseInstallation.getCurrentInstallation().getObjectId();
        CustomerOrder customerOrder = new CustomerOrder(dishes, restaurantId, text.toString(), ApplicationState.getTableId(applicationState), orderNote);
        return customerOrder;
    }

    public ArrayList<MyOrderItem> getMyOrderItemList() {
        return myOrderItemList;
    }

    public void setMyOrderItemList(ArrayList<MyOrderItem> myOrderItemList) {
        this.myOrderItemList = myOrderItemList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<String> getUnAvailableItems() {
        if(unAvailableItems==null) {
            unAvailableItems = new ArrayList<String>();
        }
        return unAvailableItems;
    }

    public void setUnAvailableItems(List<String> unAvailableItems) {
        if(unAvailableItems==null) {
            return ;
        }
        this.unAvailableItems = unAvailableItems;
    }

}
