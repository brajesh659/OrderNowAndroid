package com.example.ordernowandroid.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ordernowandroid.FoodMenuActivity;
import com.example.ordernowandroid.adapter.FoodMenuItemAdapter;
import com.example.ordernowandroid.model.FoodMenuItem;

public class IndividualMenuTabFragment extends Fragment {

	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
	private String tabTitle;
	private ArrayList<FoodMenuItem> foodMenuItemList;

	public interface numListener {
		
		public Integer getQuantity(FoodMenuItem foodMenuItem);
		
		public void incrementQuantity(FoodMenuItem foodMenuItem);
		
		public void decrementQuantity(FoodMenuItem foodMenuItem);
	}

	numListener numCallBack;	

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try {
			numCallBack = (numListener)activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement Listeners!!");
		};
	};

	public static Fragment newInstance(String categoryName, ArrayList<FoodMenuItem> foodMenuItem) {		
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();		
		Bundle b = new Bundle();
		b.putString(TAB_TITLE, categoryName);
		b.putSerializable(TAB_ITEM_LIST, foodMenuItem);		
		imt.setArguments(b);		
		return imt;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabTitle = getArguments().getString(TAB_TITLE);
		foodMenuItemList = (ArrayList<FoodMenuItem>) getArguments().getSerializable(TAB_ITEM_LIST);		
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		FoodMenuItemAdapter foodMenuItemAdapter = new FoodMenuItemAdapter(getActivity(), foodMenuItemList, numCallBack);
		ListView lv = new ListView(getActivity());
		lv.setAdapter(foodMenuItemAdapter);
		lv.setOnCreateContextMenuListener(getActivity());
		return lv;
	}



}
