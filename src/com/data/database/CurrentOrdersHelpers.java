package com.data.database;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;

import com.util.Utilities;

public class CurrentOrdersHelpers extends SQLHelper {

	public CurrentOrdersHelpers(DatabaseManager dbManager) {
		super(dbManager);
	}

	private static final String TABLE_NAME = "currentOrders";
	private static final String GET_ALL_EVENTS = "SELECT * FROM " + TABLE_NAME;
	private static final String[] columns = { "orderId", "restId", "tableId",
			"dishId", "qty", "state", "timestamp" };

	public void getCurrentOrders() {
		Cursor cursor = dbManager.rawQuery(GET_ALL_EVENTS, null);
		Map<String, String> map = new HashMap<String, String>();
		while(cursor.moveToNext()){
			for (String col:columns)
				map.put(col, cursor.getString(cursor.getColumnIndex(col)));
		}
		Utilities.info(map.toString());
	}
}
