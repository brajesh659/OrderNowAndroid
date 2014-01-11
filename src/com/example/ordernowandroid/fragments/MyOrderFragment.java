package com.example.ordernowandroid.fragments;

import java.util.ArrayList;
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
	
	private List<MyOrderItem> myOrders;
	
	public void setMyOrders(List<MyOrderItem> myOrders) {
		this.myOrders = myOrders;
	}

	public MyOrderFragment() {
		myOrders = new ArrayList<MyOrderItem>();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.my_order, container, false);
		ListView myOrderListView = (ListView) rootView.findViewById(R.id.listMyOrder);
		MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getActivity(), myOrders);
		myOrderListView.setAdapter(myOrderAdapter);
		return rootView;
	}
	
}
