package com.example.ordernowandroid;

import android.app.Application;

public class ApplicationState extends Application {
    private String tableId;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

}
