package com.example.ordernowandroid;

import java.util.ArrayList;

import android.app.Application;

import com.example.ordernowandroid.filter.MenuFilter;
import com.example.ordernowandroid.model.MyOrderItem;

public class ApplicationState extends Application {
    private String tableId;
    private ArrayList<MyOrderItem> myOrderItems;
    private int categoryId;
    private MenuFilter menuFilter;
    
    public ApplicationState() {
        myOrderItems = new ArrayList<MyOrderItem>();
        menuFilter = new MenuFilter();
    }

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

    public static void setMyOrderItems(ApplicationState applicationContext, ArrayList<MyOrderItem> myOrderItemList) {
        applicationContext.setMyOrderItems(myOrderItemList);
    }

    public MenuFilter getMenuFilter() {
        return menuFilter;
    }

    public static MenuFilter getMenuFilter(ApplicationState applicationContext) {
        return applicationContext.getMenuFilter();
    }
    
}
