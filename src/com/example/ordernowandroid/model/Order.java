package com.example.ordernowandroid.model;

import java.io.Serializable;

import com.util.Utilities;

public class Order implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String orderId;
    private Integer subOrderId;
    
    public static Order NULL_ORDER = new Order("", -1) {
        private static final long serialVersionUID = 1L;

        public String getOrderId() {
            return "";
        };
        
        public Integer getSubOrderId() {
            return -1;
        };
    };

    public Order(String orderId, Integer subOrderId) {
        this.orderId = orderId;
        this.subOrderId = subOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getSubOrderId() {
        return subOrderId;
    }
    
    @Override 
    public boolean equals(Object order) {
        if(order instanceof Order) {
            Order orderObj =  (Order) order;
            Utilities.info("Order equal " + getOrderId() +" " + orderObj.getOrderId() +" " + getSubOrderId() + " "+ orderObj.getSubOrderId());
            Utilities.info("Order equal " + getOrderId().equals(orderObj.getOrderId()) +" " + getSubOrderId().equals(orderObj.getSubOrderId()));
            return getOrderId().equals(orderObj.getOrderId()) && getSubOrderId().equals(orderObj.getSubOrderId());
        }
        return false;
    }
     
    @Override
    public String toString() {
        return "Order Id : " +orderId + " SubOrderId " + subOrderId;
    }

}
