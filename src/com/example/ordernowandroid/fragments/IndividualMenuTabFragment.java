package com.example.ordernowandroid.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.data.menu.FoodType;
import com.example.ordernowandroid.R;
import com.example.ordernowandroid.adapter.FoodMenuItemAdapter;
import com.example.ordernowandroid.model.FoodMenuItem;

public class IndividualMenuTabFragment extends Fragment {

	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
	private String tabTitle;
	private ArrayList<FoodMenuItem> foodMenuItemList;

	public interface numListener {
		
		public float getQuantity(FoodMenuItem foodMenuItem);
		
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
		View foodCategoryView = inflater.inflate(R.layout.category_page, null);
		final FoodMenuItemAdapter foodMenuItemAdapter = new FoodMenuItemAdapter(getActivity(), foodMenuItemList, numCallBack);
		ListView lv = (ListView) foodCategoryView.findViewById(R.id.dish_list);
		final CheckedTextView veg = (CheckedTextView)foodCategoryView.findViewById(R.id.checked_veg);
		final CheckedTextView nonveg = (CheckedTextView)foodCategoryView.findViewById(R.id.checked_nonveg);
		
		veg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				veg.toggle();
				if(veg.isChecked() && nonveg.isChecked()) {
					foodMenuItemAdapter.getFilter().filter("");
				} else if(veg.isChecked()) {
					foodMenuItemAdapter.getFilter().filter(FoodType.Veg.toString());
				} else if(nonveg.isChecked()){
					foodMenuItemAdapter.getFilter().filter(FoodType.NonVeg.toString());
				} else {
					foodMenuItemAdapter.getFilter().filter("");
				}
				
			}
		});
		nonveg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nonveg.toggle();
				if(veg.isChecked() && nonveg.isChecked()) {
					foodMenuItemAdapter.getFilter().filter("");
				} else if(veg.isChecked()) {
					foodMenuItemAdapter.getFilter().filter(FoodType.Veg.toString());
				} else if(nonveg.isChecked()){
					foodMenuItemAdapter.getFilter().filter(FoodType.NonVeg.toString());
				} else {
					foodMenuItemAdapter.getFilter().filter("");
				}
				
			}
		});
		
		lv.setAdapter(foodMenuItemAdapter);
		lv.setOnCreateContextMenuListener(getActivity());
		return foodCategoryView;
	}

}
