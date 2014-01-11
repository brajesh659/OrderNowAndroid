package com.example.ordernowandroid.fragments;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.MyOrderAdapter;
import com.example.ordernowandroid.model.MyOrderItem;

public class MyOrderFragment extends Fragment {

	public MyOrderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.my_order, container, false);
		ListView myOrderListView = (ListView) rootView.findViewById(R.id.listMyOrder);
		List<MyOrderItem> orders = new LinkedList<MyOrderItem>();
		orders.add(new MyOrderItem("item1", 25));
		orders.add(new MyOrderItem("item2", 26));
		orders.add(new MyOrderItem("item3", 27));
		MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getActivity(), orders);
		myOrderListView.setAdapter(myOrderAdapter);
		return rootView;
	}
	
}
