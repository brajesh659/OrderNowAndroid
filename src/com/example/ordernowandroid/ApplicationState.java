package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Application;

import com.example.ordernowandroid.model.MyOrderItem;

public class ApplicationState extends Application {
    private String tableId;
    private ArrayList<MyOrderItem> myOrderItems;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public ArrayList<MyOrderItem> getMyOrderItems() {
        return myOrderItems;
    }

    public void setMyOrderItems(ArrayList<MyOrderItem> myOrderItems) {
        this.myOrderItems = myOrderItems;
    }

}
