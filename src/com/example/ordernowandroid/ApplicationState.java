package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

import com.example.ordernowandroid.model.MyOrderItem;

public class ApplicationState extends Application {
    private String tableId;
    private ArrayList<MyOrderItem> myOrderItems;
    private int categoryId;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public static int getCategoryId(ApplicationState applicationContext) {
        return applicationContext.getCategoryId();

    }

    public static void setCategoryId(ApplicationState applicationContext, int categoryId) {
        applicationContext.setCategoryId(categoryId);
    }

    public static ArrayList<MyOrderItem> getMyOrderItems(ApplicationState applicationContext) {
        return applicationContext.getMyOrderItems();
    }

}
