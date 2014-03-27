package com.example.ordernowandroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.ordernowandroid.adapter.AllHistoryAdapter;
import com.example.ordernowandroid.model.AllHistoryViewItem;

public class AllCustomerHistoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Past Orders");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.expandable_list_view);

		ExpandableListView allHistoryListView = (ExpandableListView) findViewById(R.id.expandableListView);

		ArrayList<AllHistoryViewItem> allHistoryItems = getHistoryItemLocaly();
		AllHistoryAdapter adapter = new AllHistoryAdapter(
				getApplicationContext(), allHistoryItems);
		allHistoryListView.setAdapter(adapter);
	}

	private ArrayList<AllHistoryViewItem> getHistoryItemLocaly() {
		ArrayList<AllHistoryViewItem> allHistoryItems = new ArrayList<AllHistoryViewItem>();

		ArrayList<String> dishNames = new ArrayList<String>();
		dishNames.add("Chicken Starter");
		dishNames.add("Fish Tikka");
		dishNames.add("Dal Makhni");
		dishNames.add("Coke");

		ArrayList<String> dishNames2 = new ArrayList<String>();
		dishNames2.add("Paneer Tikka");
		dishNames2.add("Rice");
		dishNames2.add("Dal Fry");
		dishNames2.add("Pepsi");

		String date1 = "20-03-2014";
		String date2 = "24-03-2014";
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		AllHistoryViewItem allHistoryItem = null;
		AllHistoryViewItem allHistoryItem2 = null;
		try {
			allHistoryItem = new AllHistoryViewItem("Eat3", "D1042556",
					df.parse(date1), dishNames);
			allHistoryItem2 = new AllHistoryViewItem("Paramuru Grill",
					"O1042556021", df.parse(date2), dishNames2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		allHistoryItems.add(allHistoryItem);
		allHistoryItems.add(allHistoryItem2);
		return allHistoryItems;
	}

}
