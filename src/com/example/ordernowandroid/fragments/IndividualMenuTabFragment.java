package com.example.ordernowandroid.fragments;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class IndividualMenuTabFragment extends Fragment {
	
	private static final String TAB_TITLE = "tabTitle";
	private static final String TAB_ITEM_LIST = "tabItemList";
	private String tabTitle;
	private List<String> itemList;
	
	public static Fragment newInstance(String string, ArrayList<String> itemList) {
		
		IndividualMenuTabFragment imt = new IndividualMenuTabFragment();
		Bundle b = new Bundle();
		b.putString(TAB_TITLE, string);
		b.putStringArrayList(TAB_ITEM_LIST, itemList);
		imt.setArguments(b);
		return imt;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabTitle = getArguments().getString(TAB_TITLE);
		itemList = getArguments().getStringArrayList(TAB_ITEM_LIST);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		ListView lv = new ListView(getActivity());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemList);
		lv.setAdapter(adapter );
		lv.setOnCreateContextMenuListener(getActivity());
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String item = (String) parent.getItemAtPosition(position);
				Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
				// TODO Auto-generated method stub
				
			}
		});
		return lv;
	}
	
	

}
